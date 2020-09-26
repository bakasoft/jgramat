package gramat.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class Link {

    public final Node source;
    public final Node target;

    public Link(Node source, Node target) {
        this.source = source;
        this.target = target;
    }

    public static Set<Node> collectTargets(Iterable<? extends Link> links) {
        var targets = new LinkedHashSet<Node>();

        for (var link : links) {
            targets.add(link.target);
        }

        return targets;
    }

    public static Set<Node> collectSources(Iterable<? extends Link> links) {
        var targets = new LinkedHashSet<Node>();

        for (var link : links) {
            targets.add(link.source);
        }

        return targets;
    }
}
