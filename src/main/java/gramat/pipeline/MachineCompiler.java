package gramat.pipeline;

import gramat.actions.Event;
import gramat.actions.RecursionEnter;
import gramat.actions.RecursionExit;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
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
            connectPlug(plug, graph, link.source, link.target, newBadge, link.event);
        }

        graph.removeLink(link);

        connectBetween(link.source, Chain.of(link.target));

        stackRef.pop();
    }

    private void connectPlug(Plug plug, Graph graph, Node newSource, Node newTarget, Badge newBadge, Event wrapper) {
        var type = plug.getType();

        Node linkSource;
        Node linkTarget;
        Badge linkBadge;
        Event linkEvent = Event.copy(plug.event);

        // Recursion not affected:
        if (type == PlugType.S2T) {
            linkSource = newSource;
            linkTarget = newTarget;
            linkBadge = gramat.badges.empty();
            linkEvent.wrap(wrapper);
        }
        else if (type == PlugType.T2S) {
            linkSource = newTarget;
            linkTarget = newSource;
            linkBadge = gramat.badges.empty();
            // TODO test ignored wrapping actions
        }

        // Entering to recursion:
        else if (type == PlugType.S2N) {
            linkSource = newSource;
            linkTarget = plug.getTarget();
            linkBadge = gramat.badges.empty();
            linkEvent.prepend(wrapper.before);
            if (newBadge != gramat.badges.empty()) {
                linkEvent.prepend(new RecursionEnter(newBadge));
            }
        }
        else if (type == PlugType.T2N) {
            linkSource = newTarget;
            linkTarget = plug.getTarget();
            linkBadge = gramat.badges.empty();
            if (newBadge != gramat.badges.empty()) {
                linkEvent.prepend(new RecursionEnter(newBadge));
            }
            // TODO test ignored wrapping actions
        }

        // Exiting from recursion:
        else if (type == PlugType.N2S) {
            linkSource = plug.getSource();
            linkTarget = newSource;
            linkBadge = newBadge;
            if (newBadge != gramat.badges.empty()) {
                linkEvent.append(new RecursionExit(newBadge));
            }
        }
        else if (type == PlugType.N2T) {
            linkSource = plug.getSource();
            linkTarget = newTarget;
            linkBadge = newBadge;
            linkEvent.append(wrapper.after);
            if (newBadge != gramat.badges.empty()) {
                linkEvent.append(new RecursionExit(newBadge));
            }
        }
        else {
            throw new UnsupportedValueException(type);
        }

        graph.createLink(linkSource, linkTarget, linkEvent, plug.getSymbol(), linkBadge);
    }

}
