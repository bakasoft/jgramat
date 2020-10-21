package gramat.graph.plugs;

import gramat.graph.Link;
import gramat.graph.Node;
import gramat.graph.Root;
import gramat.util.Chain;

public enum PlugType {
    N2N,
    S2T,
    S2N,
    T2S,
    T2N,
    N2S,
    N2T;

    public static PlugType compute(Root root, Link link) {
        return compute(root.source, root.targets, link);
    }

    public static PlugType compute(Node source, Chain<Node> targets, Link link) {
        return compute(Chain.of(source), targets, link);
    }

    public static PlugType compute(Chain<Node> sources, Chain<Node> targets, Link link) {
        if (sources.contains(link.source)) {
            if (targets.contains(link.target)) {
                return PlugType.S2T;
            } else {
                return PlugType.S2N;
            }
        }
        else if (targets.contains(link.source)) {
            if (sources.contains(link.target)) {
                return PlugType.T2S;
            } else {
                return PlugType.T2N;
            }
        }
        else if (sources.contains(link.target)) {
            return PlugType.N2S;
        }
        else if (targets.contains(link.target)) {
            return PlugType.N2T;
        }
        return PlugType.N2N;
    }
}
