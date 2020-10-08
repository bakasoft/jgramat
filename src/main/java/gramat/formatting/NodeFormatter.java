package gramat.formatting;

import gramat.graph.*;
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
        raw("->");
        sp();
        raw(root.source.id);
        ln();

        for (var link : graph.findLinksBetween(root)) {
            write(link);
        }

        for (var target : root.targets) {
            raw(target.id);
            sp();
            raw("<=");
            ln();
        }
    }

    public void write(Link link) {
        raw(link.source.id);
        sp();
        raw("->");
        sp();
        raw(link.target.id);
        sp();
        raw(":");
        sp();
        amstr(link.symbol.toString());
        raw("/");
        amstr(link.badge.toString());
        raw("(");
        amstr(link.mode.name());
        raw(")");
        ln();

        var beforeActions = StringUtils.join("\n", link.beforeActions);

        if (beforeActions.length() > 0) {
            raw(link.source.id);
            sp();
            raw("->");
            sp();
            raw(link.target.id);
            sp();
            raw("!<");
            sp();
            amstr(beforeActions);
            ln();
        }

        var afterActions = StringUtils.join("\n", link.afterActions);

        if (afterActions.length() > 0) {
            raw(link.source.id);
            sp();
            raw("->");
            sp();
            raw(link.target.id);
            sp();
            raw("!>");
            sp();
            amstr(afterActions);
            ln();
        }
    }

}