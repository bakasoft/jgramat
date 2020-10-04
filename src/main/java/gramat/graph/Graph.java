package gramat.graph;

import gramat.actions.Action;
import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;
import gramat.util.Count;

import javax.naming.LinkRef;
import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    private final Count ids;

    public final List<Link> links;
    public final List<Node> nodes;

    public Graph() {
        this(new Count());
    }

    public Graph(Count ids) {
        this.ids = ids;
        this.links = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public Node createNode() {
        var id = ids.nextString();
        return createNode(id, false);
    }

    public Node createNodeFrom(Node original) {
        var id = ids.nextString();
        return createNode(original.id + "_" + id, false);
    }

    public Node createNode(String id, boolean wild) {
        var node = new Node(id, wild);

        nodes.add(node);

        return node;
    }

    public NodeSet createNodeSet() {
        var node = createNode();

        return new NodeSet(node);
    }

    public void createLinks(NodeSet sources, NodeSet targets, Symbol symbol, Badge badge, BadgeMode mode) {
        for (var source : sources) {
            for (var target : targets) {
                createLink(source, target, symbol, badge, mode);
            }
        }
    }

    public void createLinks(NodeSet sources, NodeSet targets, String reference) {
        for (var source : sources) {
            for (var target : targets) {
                createLink(source, target, reference);
            }
        }
    }

    public Link createLink(Node source, Node target, Symbol symbol, Badge badge, BadgeMode mode) {
        return createLink(source, target, null, null, symbol, badge, mode);
    }

    public Link createLink(Node source, Node target, String reference) {
        return createLink(source, target, null, null, reference);
    }

    public Link createLink(Node source, Node target, ActionStore beforeActions, ActionStore afterActions, Symbol symbol, Badge badge, BadgeMode mode) {
        var link = new LinkSymbol(source, target, new ActionStore(beforeActions), new ActionStore(afterActions), symbol, badge, mode);
        links.add(link);
        return link;
    }

    public Link createLink(Node source, Node target, ActionStore beforeActions, ActionStore afterActions, String reference) {
        var link = new LinkReference(source, target, new ActionStore(beforeActions), new ActionStore(afterActions), reference);
        links.add(link);
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

    public List<LinkSymbol> findTransitions(NodeSet sources, Symbol symbol, Badge badge) {
        var result = new ArrayList<LinkSymbol>();
        for (var link : links) {
            // Check for only links with symbols
            if (!(link instanceof LinkSymbol)) {
                throw new RuntimeException("only symbol links allowed: " + link);
            }

            var linkSymbol = (LinkSymbol) link;

            // Skip not matching symbols
            if (linkSymbol.symbol != symbol) {
                continue;
            }

            // Skip links from other source
            if (!sources.contains(link.source)) {
                continue;
            }

            // Skip not maching badges
            if (linkSymbol.badge != badge) {
                continue;
            }

            result.add(linkSymbol);
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
        return walkLinksFrom(new NodeSet(source));
    }

    public List<Link> walkLinksFrom(NodeSet sources) {
        var result = new ArrayList<Link>();
        var control = new HashSet<Node>();
        var queue = new LinkedList<Node>();

        queue.addAll(sources.toCollection());

        while (queue.size() > 0) {
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

    public Segment segment() {
        return new Segment(this, createNodeSet(), createNodeSet());
    }

    public Segment segment(Node source, Node target) {
        return new Segment(this, new NodeSet(source), new NodeSet(target));
    }

    public Segment segment(NodeSet sources, NodeSet targets) {
        return new Segment(this, sources, targets);
    }

    public Line createLine() {
        return new Line(this, createNode(), createNode());
    }

    public void removeLink(Link link) {
        links.remove(link);
    }

}
