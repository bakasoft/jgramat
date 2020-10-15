package gramat.pipeline;

import gramat.actions.Event;
import gramat.badges.Badge;
import gramat.framework.Component;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Root;
import gramat.graph.plugs.Extension;
import gramat.graph.plugs.Plug;
import gramat.graph.plugs.PlugType;
import gramat.graph.util.NodeMapper;
import gramat.symbols.SymbolReference;
import gramat.util.Chain;
import gramat.util.NameMap;

import java.util.*;

public class TemplateCompiler {

    public static Template compile(Component parent, Blueprint oldBlue, Graph newGraph) {
        return new TemplateCompiler(parent, oldBlue, newGraph).run(oldBlue.root);
    }

    private final Component parent;
    private final Graph oldGraph;
    private final Graph newGraph;
    private final Set<String> recursive;
    private final Set<String> processed;
    private final NameMap<Root> newRoots;
    private final NameMap<Root> oldRoots;

    private TemplateCompiler(Component parent, Blueprint oldBlueprint, Graph newGraph) {
        this.parent = parent;
        this.recursive = computeRecursiveReferences(oldBlueprint);
        this.oldGraph = oldBlueprint.graph;
        this.oldRoots = oldBlueprint.dependencies;
        this.newGraph = newGraph;
        this.newRoots = new NameMap<>();
        this.processed = new HashSet<>();
    }

    private Template run(Root oldRoot) {
        var newRoot = standAloneCopyRoot(oldRoot, parent.getGramat().badges.empty());
        var extensions = makeExtensions();
        return new Template(newGraph, newRoot, extensions);
    }

    private Root standAloneCopyRoot(Root oldRoot, Badge badge) {
        var mapper = new NodeMapper(newGraph);

        copyRoot(oldRoot, mapper, badge, null);

        return mapper.find(oldRoot);
    }

    private void copyRoot(Root oldRoot, NodeMapper mapper, Badge badge, Event event) {
        for (var oldLink : oldGraph.findLinksBetween(oldRoot)) {
            if (oldLink.badge != badge) {
                throw new RuntimeException("unexpected badge: " + badge);
            }

            var reference = tryReference(oldLink);

            if (reference != null) {
                var oldRef = oldRoots.find(reference);

                if (recursive.contains(reference)) {
                    // Copy it anyway, another process will resolve it
                    mapper.copy(oldLink);

                    if (processed.add(reference)) {
                        var newRoot = standAloneCopyRoot(oldRef, oldLink.badge);

                        newRoots.set(reference, newRoot);
                    }
                }
                else {
                    copyLink(oldLink, oldRef, mapper);
                }
            }
            else {
                mapper.copy(oldLink);
            }
        }

        if (event != null) {
            distributeActions(mapper, oldRoot, event);
        }
    }

    private void distributeActions(NodeMapper mapper, Root oldRoot, Event event) {
        var newRoot = mapper.find(oldRoot);
        for (var newLink : newGraph.findLinksBetween(newRoot)) {
            var type = PlugType.compute(newRoot, newLink);

            if (type == PlugType.S2T) {
                newLink.eventWrap(event);
            }
            else if (type == PlugType.S2N) {
                newLink.eventPrepend(event.before);
            }
            else if (type == PlugType.N2T) {
                newLink.eventAppend(event.after);
            }
        }
    }

    private void copyLink(Link oldLink, Root oldRoot, NodeMapper mapper) {
        var newSource = mapper.copy(oldLink.source);
        var newTarget = mapper.copy(oldLink.target);

        var localMapper = new NodeMapper(mapper);
        localMapper.set(oldRoot.source, newSource);
        localMapper.set(oldRoot.targets, newTarget);

        copyRoot(oldRoot, localMapper, oldLink.badge, oldLink.getEvent());
    }

    private NameMap<Extension> makeExtensions() {
        var extensions = new NameMap<Extension>();

        for (var entry : newRoots.entrySet()) {
            var rootName = entry.getKey();
            var root = entry.getValue();
            var plugs = new ArrayList<Plug>();
            var id = extensions.size();

            for (var link : newGraph.findLinksBetween(root)) {
                var type = PlugType.compute(root, link);

                if (type != PlugType.N2N) {
                    var plug = Plug.make(link, type);

                    plugs.add(plug);

                    // The link is replaced by the plug
                    newGraph.removeLink(link);
                }
            }

            extensions.set(rootName, new Extension(id, plugs));
        }

        return extensions;
    }

    private static String tryReference(Link link) {
        if (link.symbol instanceof SymbolReference) {
            return ((SymbolReference) link.symbol).reference;
        }

        return null;
    }

    private static Set<String> computeRecursiveReferences(Blueprint blueprint) {
        var references = new LinkedHashSet<String>();
        var stack = new ArrayDeque<String>();

        computeRecursiveReferences(blueprint, blueprint.root, stack, references);

        return references;
    }

    private static void computeRecursiveReferences(Blueprint blueprint, Root baseRoot, Deque<String> stack, Set<String> result, String name) {
        stack.push(name);

        computeRecursiveReferences(blueprint, baseRoot, stack, result);

        stack.pop();
    }

    private static void computeRecursiveReferences(Blueprint blueprint, Root root, Deque<String> stack, Set<String> result) {
        for (var link : blueprint.graph.findLinksBetween(root)) {
            if (link.symbol instanceof SymbolReference) {
                var reference = ((SymbolReference)link.symbol).reference;

                if (stack.contains(reference)) {
                    result.add(reference);
                }
                else {
                    var refRoot = blueprint.dependencies.find(reference);

                    computeRecursiveReferences(blueprint, refRoot, stack, result, reference);
                }
            }
        }
    }

}
