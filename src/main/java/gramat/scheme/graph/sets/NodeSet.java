package gramat.scheme.graph.sets;

import gramat.scheme.graph.Node;
import gramat.util.DataUtils;
import gramat.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;

public interface NodeSet extends Iterable<Node> {

    static NodeSet of() {
        return new NodeSetEmpty();
    }

    static NodeSet of(Node node) {
        return new NodeSetSingleton(node);
    }

    static NodeSet of(List<Node> nodes) {
        return new NodeSetReadOnly(nodes);
    }

    static NodeSet of(Node n1, Node n2) {
        return new NodeSetReadOnly(List.of(n1, n2));
    }

    static NodeSet of(NodeSet ns1, NodeSet ns2) {
        var nodes = new LinkedHashSet<Node>();
        nodes.addAll(ns1.toList());
        nodes.addAll(ns2.toList());
        return new NodeSetReadOnly(nodes);
    }

    static NodeSet of(Node node, NodeSet ns1, NodeSet ns2) {
        var nodes = new LinkedHashSet<Node>();
        nodes.add(node);
        nodes.addAll(ns1.toList());
        nodes.addAll(ns2.toList());
        return new NodeSetReadOnly(nodes);
    }

    static NodeSet of(Node node, NodeSet nodeSet) {
        var nodes = new LinkedHashSet<Node>();
        nodes.add(node);
        nodes.addAll(nodeSet.toList());
        return new NodeSetReadOnly(nodes);
    }

    default String computeID() {
        var ids = DataUtils.map(this, n -> n.id);

        ids.sort(Comparator.naturalOrder());

        return StringUtils.join("_", ids);
    }

    boolean contains(Node node);

    List<Node> toList();

    boolean containsAny(NodeSet nodes);

    boolean anyMatch(Predicate<Node> condition);

    int size();

}


