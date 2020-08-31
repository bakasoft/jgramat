package gramat.nodes;

import tools.A;
import tools.GraphBuilder;
import org.junit.Test;

public class NodeVertexTest {

    @Test
    public void test() {
        var graph = new GraphBuilder();
        graph.line('0', '0', '2');
        graph.line('0', '0', '3');
        graph.line('0', '1', '4');

        var result = NodeVertex.stackVertices(graph.getVertices());

        assert result.size() == 1;
        var n0 = result.get(0);

        A.vertexChar(n0, '0');

        assert n0.getEdges().size() == 2;
        var n1 = n0.getEdges().get(0);
        var n2 = n0.getEdges().get(1);

        A.vertexChar(n1, '0');
        A.vertexChar(n2, '1');

        assert n1.getEdges().size() == 2;
        var n3 = n1.getEdges().get(0);
        var n4 = n1.getEdges().get(1);

        A.vertexChar(n3, '2');
        A.vertexChar(n4, '3');

        assert n3.getEdges().isEmpty();
        assert n4.getEdges().isEmpty();

        assert n2.getEdges().size() == 1;
        var n5 = n2.getEdges().get(0);

        A.vertexChar(n5, '4');

        assert n5.getEdges().isEmpty();
    }

}
