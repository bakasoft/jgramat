package gramat.graph;

import gramat.actions.Action;
import gramat.actions.ActionStore;
import gramat.symbols.Symbol;
import gramat.util.Count;

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

    public Link createLinkFrom(Join join, Node newSource, Node newTarget) {
        return createLink(newSource, newTarget, join.token, join.beforeActions, join.afterActions);
    }

    public void createLinks(NodeSet sources, NodeSet targets, Token token) {
        for (var source : sources) {
            for (var target : targets) {
                createLink(source, target, token);
            }
        }
    }

    public Link createLink(Node source, Node target, Token token) {
        return createLink(source, target, token, null, null);
    }

    public Link createLink(Node source, Node target, Token token, ActionStore beforeActions, ActionStore afterActions) {
        var link = new Link(source, target, token, new ActionStore(beforeActions), new ActionStore(afterActions));
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

    public List<Link> findOutgoingLinks(NodeSet sources, Symbol symbol) {
        var result = new ArrayList<Link>();
        for (var link : links) {
            if (sources.contains(link.source) && link.token.isSymbol() && Objects.equals(link.token.getSymbol(), symbol)) {
                result.add(link);
            }
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

    public Set<String> findReferences() {
        var refs = new LinkedHashSet<String>();

        for (var link : links) {
            if (link.token.isReference()) {
                refs.add(link.token.getReference());
            }
        }

        return refs;
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
