package gramat.pipeline;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.graph.plugs.*;
import gramat.graph.util.NodeMapper;
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

        for (var linkRef : graph.findReferencesBetween(root.source, root.targets)) {
            resolveReference(mapper, linkRef);

            graph.removeLink(linkRef);
        }

        return root;
    }

    private void copyRoot(NodeMapper mapper, Root baseRoot, ActionStore beforeActions, ActionStore afterActions) {
        // Direct copy
        for (var oldLink : blueprint.graph.findLinksBetween(baseRoot)) {
            copyLink(mapper, oldLink);
        }

        // TODO apply wrapping actions
    }

    private void copyLink(NodeMapper mapper, Link oldLink) {
        var newSource = mapper.make(oldLink.source);
        var newTarget = mapper.make(oldLink.target);

        if (oldLink instanceof LinkSymbol) {
            var oldLinkSym = (LinkSymbol)oldLink;

            graph.createLink(
                    newSource, newTarget,
                    oldLinkSym.beforeActions, oldLinkSym.afterActions,
                    oldLinkSym.symbol, oldLinkSym.badge, oldLinkSym.mode);
        }
        else if (oldLink instanceof LinkReference) {
            var oldLinkRef = (LinkReference)oldLink;

            graph.createLink(
                    newSource, newTarget,
                    oldLinkRef.beforeActions, oldLinkRef.afterActions,
                    oldLinkRef.reference);
        }
        else {
            throw new UnsupportedValueException(oldLink);
        }
    }

    private void resolveReference(NodeMapper mapper, LinkReference newRef) {
        var reference = newRef.reference;
        var newSource = newRef.source;
        var newTarget = newRef.target;
        if (recursiveReferences.contains(reference)) {
            Badge newBadge;

            if (stackRef.contains(reference)) {
                newBadge = gramat.badges.badge(callTokens.next(reference));
            }
            else {
                newBadge = gramat.badges.empty();
            }

            stackRef.push(reference);

            var extension = extensions.find(reference);

            connectExtensionTo(newBadge, extension, newSource, newTarget, newRef.beforeActions, newRef.afterActions);

            stackRef.pop();
        }
        else {
            var baseRoot = blueprint.dependencies.find(reference);
            var localMapper = new NodeMapper(mapper);

            // Map the root to be copied to the existing link
            localMapper.set(baseRoot.source, newSource);
            localMapper.set(baseRoot.targets, newTarget);

            copyRoot(localMapper, baseRoot, newRef.beforeActions, newRef.afterActions);
        }
    }

    private Extension makeExtension(NodeMapper mapper, Root oldRoot) {
        var plugs = new ArrayList<Plug>();

        for (var oldLink : blueprint.graph.findLinksBetween(oldRoot)) {
            var plug = tryMakePlug(mapper, oldLink, oldRoot);

            if (plug != null) {
                plugs.add(plug);
            }
            else {
                copyLink(mapper, oldLink);
            }
        }

        return new Extension(plugs);
    }

    private Plug tryMakePlug(NodeMapper mapper, Link oldLink, Root oldRoot) {
        if (oldLink instanceof LinkSymbol) {
            var oldLinkSym = (LinkSymbol)oldLink;
            if (oldLink.source == oldRoot.source) {
                // From source
                if (oldRoot.targets.contains(oldLink.target)) {
                    // To target
                    return new PlugSymbolSourceToTarget(oldLinkSym);
                } else {
                    // To Node
                    var newTarget = mapper.make(oldLink.target);

                    return new PlugSymbolSourceToNode(oldLinkSym, newTarget);
                }
            }
            else if (oldRoot.targets.contains(oldLink.source)) {
                if (oldLink.target == oldRoot.source) {
                    return new PlugSymbolTargetToSource(oldLinkSym);
                } else {
                    var newTarget = mapper.make(oldLink.target);

                    return new PlugSymbolTargetToNode(oldLinkSym, newTarget);
                }
            }
            else if (oldLink.target == oldRoot.source) {
                var newSource = mapper.make(oldLink.source);

                return new PlugSymbolNodeToSource(oldLinkSym, newSource);
            }
            else if (oldRoot.targets.contains(oldLink.target)) {
                var newSource = mapper.make(oldLink.source);

                return new PlugSymbolNodeToTarget(oldLinkSym, newSource);
            }

            return null;
        }
        else if (oldLink instanceof LinkReference) {
            // TODO This part is missing....
            throw new RuntimeException();
        }
        else {
            throw new UnsupportedValueException(oldLink);
        }
    }

    private void connectExtensionTo(Badge newBadge, Extension extension, Node source, Node target, ActionStore beforeActions, ActionStore afterActions) {
        for (var plug : extension.plugs) {
            if (plug instanceof PlugSymbol) {
                plug.connectTo(graph, source, target, newBadge, beforeActions, afterActions);
            }
            else {
                throw new UnsupportedValueException(plug);
            }
        }
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
            if (link instanceof LinkReference) {
                var linkRef = (LinkReference)link;
                var nameRef = linkRef.reference;

                if (stack.contains(nameRef)) {
                    result.add(nameRef);
                }
                else {
                    var refRoot = blueprint.dependencies.find(nameRef);

                    computeRecursiveReferences(refRoot, stack, result, nameRef);
                }
            }
        }
    }
}
