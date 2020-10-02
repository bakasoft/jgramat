package gramat.graph;

public class Line {

    public final Graph graph;
    public Node source;
    public Node target;

    public Line(Graph graph, Node source, Node target) {
        this.graph = graph;
        this.source = source;
        this.target = target;
    }

}
