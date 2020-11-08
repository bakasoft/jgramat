package gramat.pipeline;

import gramat.actions.Event;
import gramat.actions.RecursionEnter;
import gramat.actions.RecursionExit;
import gramat.badges.Badge;
import gramat.badges.BadgeSource;
import gramat.badges.BadgeToken;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Context;
import gramat.graph.*;
import gramat.graph.plugs.*;
import gramat.expressions.Template;
import gramat.symbols.SymbolReference;
import gramat.util.NameMap;
import gramat.graph.sets.NodeSet;
import gramat.util.TokenGenerator;

import java.util.*;

public class MachineCompiler {

    public static Machine compile(Context ctx, Template template, BadgeSource badges) {
        return new MachineCompiler(ctx, template.graph, template.extensions, badges).compileRoot(template.main);
    }

    private final Context ctx;
    private final Graph graph;
    private final TokenGenerator callTokens;
    private final BadgeSource badges;
    private final Deque<String> stackRef;
    private final NameMap<Extension> extensions;

    private MachineCompiler(Context ctx, Graph graph, NameMap<Extension> extensions, BadgeSource badges) {
        this.ctx = ctx;
        this.graph = graph;
        this.extensions = extensions;
        this.callTokens = new TokenGenerator();
        this.stackRef = new ArrayDeque<>();
        this.badges = badges;
    }

    private Machine compileRoot(Root root) {
        int count = countReferences(root);

        ctx.setTotal(0, count);

        connectBetween(root.source, root.targets);

        return new Machine(graph, root);
    }

    private void connectBetween(Node source, NodeSet targets) {
        for (var link : graph.findLinksBetween(source, targets)) {
            if (link.symbol instanceof SymbolReference) {
                var reference = ((SymbolReference) link.symbol).reference;
                var extension = extensions.find(reference);

                connectLink(extension, reference, link);
            }
        }
    }

    private void connectLink(Extension extension, String reference, Link link) {
        ctx.setStatus("Connecting %s...", reference);
        ctx.addProgress(1);

        Badge newBadge;

        if (stackRef.contains(reference)) {
            var uid = Objects.hash(extension.id, link.source.id, link.target.id);

            newBadge = badges.badge(reference + "-" + uid);
        }
        else {
            newBadge = badges.empty();
        }

        stackRef.push(reference);

        for (var plug : extension.plugs) {
            connectPlug(plug, graph, link.source, link.target, newBadge, link.event);
        }

        graph.removeLink(link);

        connectBetween(link.source, NodeSet.of(link.target));

        stackRef.pop();
    }

    private void connectPlug(Plug plug, Graph graph, Node newSource, Node newTarget, Badge newBadge, Event wrapper) {
        var type = plug.getType();

        Node linkSource;
        Node linkTarget;
        Badge linkBadge;
        Event linkEvent = Event.copy(plug.event);

        // Recursion not affected:
        if (type == PlugType.SOURCE_TO_TARGET) {
            linkSource = newSource;
            linkTarget = newTarget;
            linkBadge = badges.empty();
            linkEvent.wrap(wrapper);
        }
        else if (type == PlugType.TARGET_TO_SOURCE) {
            linkSource = newTarget;
            linkTarget = newSource;
            linkBadge = badges.empty();
            // TODO test ignored wrapping actions
        }

        // Loops:
        else if (type == PlugType.SOURCE_TO_SOURCE) {
            linkSource = newSource;
            linkTarget = newSource;
            linkBadge = badges.empty();
            linkEvent.prepend(wrapper.before);  // TODO really?
        }

        // Entering to recursion:
        else if (type == PlugType.SOURCE_TO_NODE) {
            linkSource = newSource;
            linkTarget = plug.getTarget();
            linkBadge = badges.empty();
            linkEvent.prepend(wrapper.before);
            if (newBadge != badges.empty()) {
                linkEvent.prepend(new RecursionEnter((BadgeToken)newBadge));
            }
        }
        else if (type == PlugType.TARGET_TO_NODE) {
            linkSource = newTarget;
            linkTarget = plug.getTarget();
            linkBadge = badges.empty();
            if (newBadge != badges.empty()) {
                linkEvent.prepend(new RecursionEnter((BadgeToken)newBadge));
            }
            // TODO test ignored wrapping actions
        }

        // Exiting from recursion:
        else if (type == PlugType.NODE_TO_SOURCE) {
            linkSource = plug.getSource();
            linkTarget = newSource;
            linkBadge = newBadge;
            if (newBadge != badges.empty()) {
                linkEvent.append(new RecursionExit((BadgeToken)newBadge));
            }
        }
        else if (type == PlugType.NODE_TO_TARGET) {
            linkSource = plug.getSource();
            linkTarget = newTarget;
            linkBadge = newBadge;
            linkEvent.append(wrapper.after);
            if (newBadge != badges.empty()) {
                linkEvent.append(new RecursionExit((BadgeToken)newBadge));
            }
        }
        else {
            throw new UnsupportedValueException(type);
        }

        graph.createLink(linkSource, linkTarget, linkEvent, plug.getSymbol(), linkBadge);
    }

    public int countReferences(Root root) {
        int count = 0;
        for (var link : graph.links) {
            if (link.symbol instanceof SymbolReference) {
                count++;
            }
        }
        return count;
    }

}
