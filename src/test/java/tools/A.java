package tools;

import gramat.nodes.Node;
import gramat.nodes.NodeVertex;

public class A {

    public static void vertexChar(NodeVertex vertex, char value) {
        nodeChar(vertex.getNode(), value);
    }

    public static void nodeChar(Node node, char value) {
        if (!(node instanceof NodeChar)) {
            throw new AssertionError("Expected " + NodeChar.class + " instead of " + node.getClass());
        }

        var nc = (NodeChar) node;

        if (nc.value != value) {
            throw new AssertionError("Expected char '" + value + "' instead of '" + nc.value + "'");
        }
    }

}
