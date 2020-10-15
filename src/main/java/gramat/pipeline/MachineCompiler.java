package gramat.pipeline;

import gramat.badges.Badge;
import gramat.formatting.NodeFormatter;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.graph.plugs.*;
import gramat.symbols.SymbolReference;
import gramat.util.Chain;
import gramat.util.NameMap;
import gramat.util.TokenGenerator;

import java.util.*;

public class MachineCompiler extends DefaultComponent {

    public static Machine compile(Component parent, Template template) {
        return new MachineCompiler(parent, template.graph, template.extensions).compileRoot(template.main);
    }

    private final Graph graph;
    private final TokenGenerator callTokens;

    private final Deque<String> stackRef;
    private final NameMap<Extension> extensions;

    private MachineCompiler(Component parent, Graph graph, NameMap<Extension> extensions) {
        super(parent);
        this.graph = graph;
        this.extensions = extensions;
        this.callTokens = new TokenGenerator();
        this.stackRef = new ArrayDeque<>();
    }

    private Machine compileRoot(Root root) {
        connectBetween(root.source, root.targets);

        return new Machine(graph, root);
    }

    private void connectBetween(Node source, Chain<Node> targets) {
        for (var link : graph.findLinksBetween(source, targets)) {
            if (link.symbol instanceof SymbolReference) {
                var reference = ((SymbolReference) link.symbol).reference;
                var extension = extensions.find(reference);

                connectLink(extension, reference, link);
            }
        }
    }

    private void connectLink(Extension extension, String reference, Link link) {
        Badge newBadge;

        if (stackRef.contains(reference)) {
            newBadge = gramat.badges.badge(callTokens.next(reference));
        }
        else {
            newBadge = gramat.badges.empty();
        }

        stackRef.push(reference);

        for (var plug : extension.plugs) {
            plug.connectTo(graph, link.source, link.target, newBadge, link.event);
        }

        graph.removeLink(link);

        connectBetween(link.source, Chain.of(link.target));

        stackRef.pop();
    }

}
