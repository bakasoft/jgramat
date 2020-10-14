package gramat.graph.plugs;

import gramat.graph.Link;
import gramat.graph.Root;

public enum PlugType {
    N2N,
    S2T,
    S2N,
    T2S,
    T2N,
    N2S,
    N2T;

    public static PlugType compute(Root root, Link link) {
        if (link.source == root.source) {
            if (root.targets.contains(link.target)) {
                return PlugType.S2T;
            } else {
                return PlugType.S2N;
            }
        }
        else if (root.targets.contains(link.source)) {
            if (link.target == root.source) {
                return PlugType.T2S;
            } else {
                return PlugType.T2N;
            }
        }
        else if (link.target == root.source) {
            return PlugType.N2S;
        }
        else if (root.targets.contains(link.target)) {
            return PlugType.N2T;
        }
        return PlugType.N2N;
    }
}
