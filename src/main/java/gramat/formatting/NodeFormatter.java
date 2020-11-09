package gramat.formatting;

import gramat.scheme.core.actions.Event;
import gramat.scheme.core.badges.Badge;
import gramat.exceptions.UnsupportedValueException;
import gramat.scheme.graph.*;
import gramat.scheme.graph.plugs.*;
import gramat.scheme.graph.sets.NodeSetMutable;
import gramat.scheme.core.symbols.Symbol;
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

        for (var link : graph.links) {
            write(link);
        }

        for (var target : root.targets) {
            writeAccepted(target.id);
        }
    }

    public void write(Graph graph, Extension ext) {
        var plugSource = "E" + ext.id + "S";
        var plugTarget = "E" + ext.id + "T";

        var nodes = new NodeSetMutable();

        writeInitial(plugSource);
        writeAccepted(plugTarget);

        for (var plug : ext.plugs) {
            String source;
            String target;

            if (plug.getType() == PlugType.NODE_TO_SOURCE) {
                source = plug.getSource().id;
                target = plugSource;

                nodes.add(plug.getSource());
            }
            else if (plug.getType() == PlugType.NODE_TO_TARGET) {
                source = plug.getSource().id;
                target = plugTarget;

                nodes.add(plug.getSource());
            }
            else if (plug.getType() == PlugType.SOURCE_TO_NODE) {
                source = plugSource;
                target = plug.getTarget().id;

                nodes.add(plug.getTarget());
            }
            else if (plug.getType() == PlugType.SOURCE_TO_TARGET) {
                source = plugSource;
                target = plugTarget;
            }
            else if (plug.getType() == PlugType.TARGET_TO_NODE) {
                source = plugTarget;
                target = plug.getTarget().id;

                nodes.add(plug.getTarget());
            }
            else if (plug.getType() == PlugType.TARGET_TO_SOURCE) {
                source = plugTarget;
                target = plugSource;
            }
            else {
                throw new UnsupportedValueException(plug);
            }

            writeLink(source, target, plug.getSymbol(), null, plug.event);
        }

        for (var link : graph.walkLinksFrom(nodes.build())) {
            write(link);
        }
    }

    public void write(Link link) {
        writeLink(link.source.id, link.target.id, link.symbol, link.badge, link.event);
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

    private void writeLink(String source, String target, Symbol symbol, Badge badge, Event event) {
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