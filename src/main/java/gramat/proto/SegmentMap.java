package gramat.proto;

import java.util.*;

public class SegmentMap {

    private final Map<String, Segment> segments;

    public SegmentMap() {
        this.segments = new LinkedHashMap<>();
    }

    public Segment search(String name) {
        return segments.get(name);
    }

    public Segment find(String name) {
        var graph = segments.get(name);

        if (graph == null) {
            throw new RuntimeException("Not found: " + name);
        }

        return graph;
    }

    public void register(String name, Segment segment) {
        if (segments.containsKey(name)) {
            throw new RuntimeException();
        }

        segments.put(name, segment);
    }

    public boolean isFlat(Segment origin) {
        var control = new HashSet<Segment>();
        var queue = new LinkedList<Segment>();

        queue.add(origin);

        while (queue.size() > 0) {
            var segment = queue.remove();

            if (control.add(segment)) {
                for (var refName : segment.graph.findReferences()) {
                    var refSegment = find(refName);

                    if (refSegment == origin) {
                        return false;
                    }

                    queue.add(refSegment);
                }
            }
        }

        return true;
    }
}
