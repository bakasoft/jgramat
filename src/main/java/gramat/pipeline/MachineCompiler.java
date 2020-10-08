package gramat.pipeline;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.graph.plugs.*;
import gramat.graph.util.NodeMapper;
import gramat.symbols.Symbol;
import gramat.symbols.SymbolReference;
import gramat.util.Chain;
import gramat.util.NameMap;
import gramat.util.TokenGenerator;

import java.util.*;

public class MachineCompiler extends DefaultComponent {

    public static Machine compile(Component parent, Blueprint blueprint) {
        var compiler = new MachineCompiler(parent, blueprint);

        return new Machine(compiler.graph, compiler.result);
    }

    private final Blueprint blueprint;

    private final Graph graph;
    private final TokenGenerator callTokens;
    private final Root result;

    private final Deque<String> stackRef;
    private final NameMap<Extension> extensions;
    private final Set<String> recursiveReferences;

    private MachineCompiler(Component parent, Blueprint blueprint) {
        super(parent);
        this.blueprint = blueprint;
        this.graph = new Graph();
        this.callTokens = new TokenGenerator();
        this.stackRef = new ArrayDeque<>();
        this.extensions = new NameMap<>();
        this.recursiveReferences = computeRecursiveReferences();
        this.result = compileRoot();
    }

    private Root compileRoot() {
        var mapper = new NodeMapper(graph);

        for (var dependency : blueprint.dependencies.entrySet()) {
            var reference = dependency.getKey();
            var oldRoot = dependency.getValue();
            var extension = makeExtension(mapper, oldRoot);

            extensions.set(reference, extension);
        }

        copyRoot(mapper, blueprint.root, null, null);

        var root = mapper.find(blueprint.root);

        // Resolve recursive references
        for (var link : graph.findLinksBetween(root)) {
            if (link.symbol instanceof SymbolReference) {
                var reference = ((SymbolReference) link.symbol).reference;

                resolveRecursion(mapper, link, reference);

                graph.removeLink(link);
            }
        }

        return root;
    }

    private void copyRoot(NodeMapper mapper, Root baseRoot, Chain<Action> beforeActions, Chain<Action> afterActions) {
        var newLinks = new ArrayList<Link>();

        // Direct copy
        for (var oldLink : blueprint.graph.findLinksBetween(baseRoot)) {
            var newLink = copyLink(mapper, oldLink);

            newLinks.add(newLink);
        }

        // TODO apply wrapping actions
        resolveFlatReferences(mapper, newLinks);
    }

    private void resolveFlatReferences(NodeMapper mapper, ArrayList<Link> newLinks) {
        for (var newLink : newLinks) {
            var reference = tryReferenceOf(newLink.symbol);

            if (reference != null && !recursiveReferences.contains(reference)) {
                resolveFlatReference(mapper, reference, newLink);
            }
        }
    }

    private void resolveFlatReference(NodeMapper mapper, String reference, Link newLink) {
        var rootRef = blueprint.dependencies.find(reference);
        var localMapper = new NodeMapper(mapper);

        // Map the root to be copied to the existing link
        localMapper.set(rootRef.source, newLink.source);
        localMapper.set(rootRef.targets, newLink.target);

        copyRoot(localMapper, rootRef, newLink.preActions, newLink.postActions);

        graph.removeLink(newLink);
    }

    private Link copyLink(NodeMapper mapper, Link oldLink) {
        var newSource = mapper.make(oldLink.source);
        var newTarget = mapper.make(oldLink.target);

        return graph.createLink(
                    newSource, newTarget,
                    oldLink.preActions, oldLink.preActions,
                    oldLink.symbol, oldLink.badge, oldLink.mode);
    }

    private String tryReferenceOf(Symbol symbol) {
        if (symbol instanceof SymbolReference) {
            return ((SymbolReference)symbol).reference;
        }
        return null;
    }

    private void resolveRecursion(NodeMapper mapper, Link newLink, String reference) {
        var newSource = newLink.source;
        var newTarget = newLink.target;
        if (!recursiveReferences.contains(reference)) {
            throw new RuntimeException("this reference must be already resolved: " + reference);
        }
        Badge newBadge;

        if (stackRef.contains(reference)) {
            newBadge = gramat.badges.badge(callTokens.next(reference));
        }
        else {
            newBadge = gramat.badges.empty();
        }

        stackRef.push(reference);

        var extension = extensions.find(reference);

        connectExtensionTo(mapper, newBadge, extension, newSource, newTarget, newLink.preActions, newLink.postActions);

        stackRef.pop();
    }

    private Extension makeExtension(NodeMapper mapper, Root oldRoot) {
        var plugs = new ArrayList<Plug>();
        var newLinks = new ArrayList<Link>();

        for (var oldLink : blueprint.graph.findLinksBetween(oldRoot)) {
            var plug = tryMakePlug(mapper, oldLink, oldRoot);

            if (plug != null) {
                plugs.add(plug);
            }
            else {
                newLinks.add(copyLink(mapper, oldLink));
            }
        }

        resolveFlatReferences(mapper, newLinks);

        return new Extension(plugs);
    }

    private Plug tryMakePlug(NodeMapper mapper, Link oldLink, Root oldRoot) {
        if (oldLink.source == oldRoot.source) {
            // From source
            if (oldRoot.targets.contains(oldLink.target)) {
                // To target
                return new PlugSourceToTarget(oldLink);
            } else {
                // To Node
                var newTarget = mapper.make(oldLink.target);

                return new PlugSourceToNode(oldLink, newTarget);
            }
        }
        else if (oldRoot.targets.contains(oldLink.source)) {
            if (oldLink.target == oldRoot.source) {
                return new PlugTargetToSource(oldLink);
            } else {
                var newTarget = mapper.make(oldLink.target);

                return new PlugTargetToNode(oldLink, newTarget);
            }
        }
        else if (oldLink.target == oldRoot.source) {
            var newSource = mapper.make(oldLink.source);

            return new PlugNodeToSource(oldLink, newSource);
        }
        else if (oldRoot.targets.contains(oldLink.target)) {
            var newSource = mapper.make(oldLink.source);

            return new PlugNodeToTarget(oldLink, newSource);
        }

        return null;
    }

    private void connectExtensionTo(NodeMapper mapper, Badge newBadge, Extension extension, Node source, Node target, Chain<Action> beforeActions, Chain<Action> afterActions) {
        var newLinks = new ArrayList<Link>();

        for (var plug : extension.plugs) {
            var newLink = plug.connectTo(graph, source, target, newBadge, beforeActions, afterActions);

            newLinks.add(newLink);
        }

        resolveFlatReferences(mapper, newLinks);
    }

    private Set<String> computeRecursiveReferences() {
        var references = new LinkedHashSet<String>();
        var stack = new ArrayDeque<String>();

        computeRecursiveReferences(blueprint.root, stack, references);

        return references;
    }

    private void computeRecursiveReferences(Root baseRoot, Deque<String> stack, Set<String> result, String name) {
        stack.push(name);

        computeRecursiveReferences(baseRoot, stack, result);

        stack.pop();
    }

    private void computeRecursiveReferences(Root root, Deque<String> stack, Set<String> result) {
        for (var link : blueprint.graph.findLinksBetween(root)) {
            if (link.symbol instanceof SymbolReference) {
                var reference = ((SymbolReference)link.symbol).reference;

                if (stack.contains(reference)) {
                    result.add(reference);
                }
                else {
                    var refRoot = blueprint.dependencies.find(reference);

                    computeRecursiveReferences(refRoot, stack, result, reference);
                }
            }
        }
    }
}
