package gramat.graph;

import gramat.actions.Action;
import gramat.actions.Event;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;
import gramat.util.Chain;
import gramat.util.TokenGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    private final TokenGenerator ids;

    public final List<Link> links;
    public final List<Node> nodes;

    public Graph() {
        this.ids = new TokenGenerator("_");
        this.links = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public Node createNode() {
        var id = ids.next();
        return createNode(id, false);
    }

    public Node createNodeFrom(Node original) {
        return createNode(ids.next(original.id), false);
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
        return link;
    }

    public List<Link> findIncomingLinks(Chain<Node> targets) {
        return links.stream()
                .filter(t -> targets.contains(t.target))
                .collect(Collectors.toList());
    }

    public List<Link> findIncomingLinks(Node target) {
        return links.stream()
                .filter(t -> t.target == target)
                .collect(Collectors.toList());
    }

    public List<Link> findTransitions(Chain<Node> sources, Symbol symbol, Badge badge) {
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

    public List<Link> findOutgoingLinks(Chain<Node> sources) {
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
        return walkLinksFrom(Chain.of(source));
    }

    public List<Link> walkLinksFrom(Chain<Node> sources) {
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
        return findLinksBetween(source, Chain.of(target));
    }

    public List<Link> findLinksBetween(Root root) {
        return findLinksBetween(root.source, root.targets);
    }

    public List<Link> findLinksBetween(Node source, Chain<Node> targets) {
        return findLinksBetween(Chain.of(source), targets);
    }

    public List<Link> findLinksBetween(Chain<Node> sources, Chain<Node> targets) {
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
        return new Root(initial, Chain.of(accepted));
    }

    public void removeLink(Link link) {
        links.remove(link);
    }

}
