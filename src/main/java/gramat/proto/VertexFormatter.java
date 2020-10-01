package gramat.proto;

import gramat.actions.Action;
import gramat.am.formatting.AmFormatter;

import java.util.stream.Collectors;

public class VertexFormatter extends AmFormatter {

    public VertexFormatter(Appendable output) {
        super(output);
    }

    public void write(Graph graph, Line line) {
        write(graph.segment(line.source, line.target));
    }

    public void write(Segment segment) {
        for (var source : segment.sources) {
            write("->");
            value(source.id);
            newLine();
        }

        for (var edge : segment.graph.edges) {
            write(edge);
        }

        for (var target : segment.targets) {
            value(target.id);
            write("<=");
            newLine();
        }
    }

    public void write(Edge edge) {
        if (edge instanceof EdgeSymbol) {
            var edgeSymbol = (EdgeSymbol) edge;

            write(edge.source.id);
            write(" -> ");
            write(edge.target.id);
            write(" : ");
            write(amEditorString(edgeSymbol.symbol.toString()));
            newLine();
        }
        else if (edge instanceof EdgeReference) {
            var edgeReference = (EdgeReference) edge;

            write(edge.source.id);
            write(" -> ");
            write(edge.target.id);
            write(" : ");
            write(amEditorString(edgeReference.name));
            newLine();
        }
        else {
            throw new RuntimeException();
        }

        var beforeActions = edge.beforeActions.stream().map(Action::toString).collect(Collectors.joining("\n"));

        if (beforeActions.length() > 0) {
            write(edge.source.id);
            write(" -> ");
            write(edge.target.id);
            write(" !< ");
            write(amEditorString(beforeActions));
            newLine();
        }

        var afterActions = edge.afterActions.stream().map(Action::toString).collect(Collectors.joining("\n"));

        if (afterActions.length() > 0) {
            write(edge.source.id);
            write(" -> ");
            write(edge.target.id);
            write(" !> ");
            write(amEditorString(afterActions));
            newLine();
        }
    }

    private String amEditorString(String str) {
        return str
                .replace("\\", "\\\\")
                .replace(",", "\\,")
                .replace(":", "\\:")
                .replace("\n", "\\\n");
    }
}