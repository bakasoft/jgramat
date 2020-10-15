package gramat.formatting;

import gramat.actions.Event;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.graph.*;
import gramat.graph.plugs.*;
import gramat.symbols.Symbol;
import gramat.util.Chain;
import gramat.util.StringUtils;

public class NodeFormatter extends AmFormatter {

    public NodeFormatter(Appendable output) {
        super(output);
    }

    public void write(Graph graph) {
        for (var link : graph.links) {
            write(link);
        }
    }

    public void write(Graph graph, Root root) {
        writeInitial(root.source.id);

        for (var link : graph.findLinksBetween(root)) {
            write(link);
        }

        for (var target : root.targets) {
            writeAccepted(target.id);
        }
    }

    public void write(Graph graph, Extension ext) {
        var plugSource = "E" + ext.id + "S";
        var plugTarget = "E" + ext.id + "T";

        Chain<Node> nodes = null;

        writeInitial(plugSource);
        writeAccepted(plugTarget);

        for (var plug : ext.plugs) {
            String source;
            String target;

            if (plug.getType() == PlugType.N2S) {
                source = plug.getSource().id;
                target = plugSource;

                nodes = Chain.merge(nodes, plug.getSource());
            }
            else if (plug.getType() == PlugType.N2T) {
                source = plug.getSource().id;
                target = plugTarget;

                nodes = Chain.merge(nodes, plug.getSource());
            }
            else if (plug.getType() == PlugType.S2N) {
                source = plugSource;
                target = plug.getTarget().id;

                nodes = Chain.merge(nodes, plug.getTarget());
            }
            else if (plug.getType() == PlugType.S2T) {
                source = plugSource;
                target = plugTarget;
            }
            else if (plug.getType() == PlugType.T2N) {
                source = plugTarget;
                target = plug.getTarget().id;

                nodes = Chain.merge(nodes, plug.getTarget());
            }
            else if (plug.getType() == PlugType.T2S) {
                source = plugTarget;
                target = plugSource;
            }
            else {
                throw new UnsupportedValueException(plug);
            }

            writeLink(source, target, plug.getSymbol(), null, null, plug.getEvent());
        }

        for (var link : graph.walkLinksFrom(nodes)) {
            write(link);
        }
    }

    public void write(Link link) {
        writeLink(link.source.id, link.target.id, link.symbol, link.badge, link.mode, link.event);
    }

    private void writeInitial(String id) {
        raw("->");
        sp();
        raw(id);
        ln();
    }

    private void writeAccepted(String id) {
        raw(id);
        sp();
        raw("<=");
        ln();
    }

    private void writeLink(String source, String target, Symbol symbol, Badge badge, BadgeMode mode, Event event) {
        raw(source);
        sp();
        raw("->");
        sp();
        raw(target);
        sp();
        raw(":");
        sp();
        amstr(symbol.toString());
        if (badge != null) {
            raw("/");
            amstr(badge.toString());
        }
        if (mode != null) {
            raw("(");
            amstr(mode.name());
            raw(")");
        }
        ln();

        var beforeActions = StringUtils.join("\n", event.before);

        if (beforeActions.length() > 0) {
            raw(source);
            sp();
            raw("->");
            sp();
            raw(target);
            sp();
            raw("!<");
            sp();
            amstr(beforeActions);
            ln();
        }

        var afterActions = StringUtils.join("\n", event.after);

        if (afterActions.length() > 0) {
            raw(source);
            sp();
            raw("->");
            sp();
            raw(target);
            sp();
            raw("!>");
            sp();
            amstr(afterActions);
            ln();
        }
    }

}