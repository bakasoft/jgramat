package gramat.graph;

import gramat.actions.Event;
import gramat.badges.Badge;
import gramat.graph.sets.NodeSet;
import gramat.symbols.Symbol;
import gramat.util.TokenGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    private final TokenGenerator ids;

    public final List<Link> links;
    public final List<Node> nodes;

    public final Deque<List<Link>> capturings;

    public Graph() {
        this.ids = new TokenGenerator("_");
        this.links = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.capturings = new ArrayDeque<>();
    }

    public Node createNode() {
        var id = ids.next();
        return createNode(id, false);
    }

    public Node createNodeFrom(Node original) {
        return createNode(ids.next(), false);
    }

    public Node createNode(String id, boolean wild) {
        var node = new Node(id, wild);

        nodes.add(node);

        return node;
    }

    public Link createLink(Node source, Node target, Symbol symbol, Badge badge) {
        return createLink(source, target, Event.empty(), symbol, badge);
    }

    public Link createLink(Node source, Node target, Event event, Symbol symbol) {
        return createLink(source, target, event, symbol, null);
    }

    public Link createLink(Node source, Node target, Event event, Symbol symbol, Badge badge) {
        var link = new Link(source, target, event, symbol, badge);
        links.add(link);

        for (var capturing : capturings) {
            capturing.add(link);
        }

        return link;
    }

    public List<Link> findIncomingLinks(NodeSet targets) {
        return links.stream()
                .filter(t -> targets.contains(t.target))
                .collect(Collectors.toList());
    }

    public List<Link> findIncomingLinks(Node target) {
        return links.stream()
                .filter(t -> t.target == target)
                .collect(Collectors.toList());
    }

    public List<Link> findTransitions(NodeSet sources, Symbol symbol, Badge badge) {
        var result = new ArrayList<Link>();
        for (var link : links) {
            // Skip not matching symbols
            if (link.symbol != symbol) {
                continue;
            }

            // Skip links from other source
            if (!sources.contains(link.source)) {
                continue;
            }

            // Skip not maching badges
            if (link.badge != badge) {
                continue;
            }

            result.add(link);
        }
        return result;
    }

    public List<Link> findOutgoingLinks(NodeSet sources) {
        return links.stream()
                .filter(t -> sources.contains(t.source))
                .collect(Collectors.toList());
    }

    public List<Link> findOutgoingLinks(Node source) {
        return links.stream()
                .filter(t -> t.source == source)
                .collect(Collectors.toList());
    }

    public List<Link> walkLinksFrom(Node source) {
        return walkLinksFrom(NodeSet.of(source));
    }

    public List<Link> walkLinksFrom(NodeSet sources) {
        if (sources == null) {
            return List.of();
        }

        var result = new ArrayList<Link>();
        var control = new HashSet<Node>();
        var queue = new LinkedList<Node>();

        queue.addAll(sources.toList());

        while (!queue.isEmpty()) {
            var source = queue.remove();

            if (control.add(source)) {
                for (var link : findOutgoingLinks(source)) {
                    result.add(link);

                    queue.add(link.target);
                }
            }
        }

        return result;
    }

    public List<Link> findLinksBetween(Node source, Node target) {
        return findLinksBetween(source, NodeSet.of(target));
    }

    public List<Link> findLinksBetween(Root root) {
        return findLinksBetween(root.source, root.targets);
    }

    public List<Link> findLinksBetween(Node source, NodeSet targets) {
        return findLinksBetween(NodeSet.of(source), targets);
    }

    public List<Link> findLinksBetween(NodeSet sources, NodeSet targets) {
        var result = new ArrayList<Link>();
        var control = new HashSet<Node>();
        var queue = new LinkedList<Node>();

        queue.addAll(sources.toList());

        while (queue.size() > 0) {
            var node = queue.remove();

            if (control.add(node)) {
                for (var link : findOutgoingLinks(node)) {
                    result.add(link);

                    // We should not find outgoing links from target here
                    //   because we don't know all the nodes from the source yet
                    if (!targets.contains(link.target)) {
                        queue.add(link.target);
                    }
                }
            }
        }

        // At this point it is safe to check outgoing links from target
        //   and filter those which go back to the nodes from source
        for (var link : findOutgoingLinks(targets)) {
            if (control.contains(link.target)) {
                result.add(link);
            }
        }

        return result;
    }

    public Root createRoot() {
        var initial = createNode();
        var accepted = createNode();
        return new Root(initial, NodeSet.of(accepted));
    }

    public void removeLink(Link link) {
        links.remove(link);
    }

    public void mergeNodesInto(NodeSet oldNodes, Node newNode) {
        for (var link : links) {
            if (oldNodes.contains(link.source)) {
                link.source = newNode;
            }
            if (oldNodes.contains(link.target)) {
                link.target = newNode;
            }
        }

        for (var node : oldNodes) {
            this.nodes.remove(node);
        }

        newNode.wild |= oldNodes.anyMatch(n -> n.wild);
    }

}
