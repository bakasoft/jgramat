package gramat.scheme.graph.util;

import gramat.scheme.graph.Link;
import gramat.scheme.graph.Node;
import gramat.scheme.graph.Root;
import gramat.scheme.graph.sets.NodeSet;

import java.util.ArrayList;
import java.util.List;

public enum DirType {

    FROM_SOURCE,
    FROM_TARGET,
    TO_SOURCE,
    TO_TARGET;

    public static List<DirType> compute(Root root, Link link) {
        return compute(root.source, root.targets, link);
    }

    public static List<DirType> compute(Node source, NodeSet targets, Link link) {
        return compute(NodeSet.of(source), targets, link);
    }

    public static List<DirType> compute(NodeSet sources, NodeSet targets, Link link) {
        var result = new ArrayList<DirType>(4);
        if (sources.contains(link.source)) {
            result.add(FROM_SOURCE);
        }
        if (targets.contains(link.source)) {
            result.add(FROM_TARGET);
        }
        if (sources.contains(link.target)) {
            result.add(TO_SOURCE);
        }
        if (targets.contains(link.target)) {
            result.add(TO_TARGET);
        }
        return result;
    }
}

