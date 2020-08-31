package tools;

import gramat.nodes.NodeVertex;

import java.util.ArrayList;
import java.util.List;

public class GraphBuilder {

    private final ArrayList<NodeVertex> vertices;

    public GraphBuilder() {
        vertices = new ArrayList<>();
    }

    public List<NodeVertex> getVertices() {
        return vertices;
    }

    public void line(char... items) {
        var nodes = new ArrayList<NodeChar>();

        for (var item : items) {
            nodes.add(new NodeChar(item));
        }

        var vertex = NodeVertex.fromSequence(nodes);

        vertices.add(vertex);
    }

}
