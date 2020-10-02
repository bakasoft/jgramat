package gramat.formatting;

import gramat.graph.Line;
import gramat.graph.Link;
import gramat.graph.Segment;
import gramat.util.StringUtils;

public class NodeFormatter extends AmFormatter {

    public NodeFormatter(Appendable output) {
        super(output);
    }

    public void write(Line line) {
        write(line.graph.segment(line.source, line.target));
    }

    public void write(Segment segment) {
        for (var source : segment.sources) {
            raw("->");
            sp();
            raw(source.id);
            ln();
        }

        for (var link : segment.graph.links) {
            write(link);
        }

        for (var target : segment.targets) {
            raw(target.id);
            sp();
            raw("<=");
            ln();
        }
    }

    public void write(Link link) {
        if (link.token.isSymbol()) {
            raw(link.source.id);
            sp();
            raw("->");
            sp();
            raw(link.target.id);
            sp();
            raw(":");
            sp();
            amstr(link.token.getSymbol().toString());
            ln();
        }
        else if (link.token.isReference()) {
            raw(link.source.id);
            sp();
            raw("->");
            sp();
            raw(link.target.id);
            sp();
            raw(":");
            sp();
            amstr(link.token.getReference());
            ln();
        }
        else {
            throw new RuntimeException();
        }

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