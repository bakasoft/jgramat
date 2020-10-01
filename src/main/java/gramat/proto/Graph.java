package gramat.proto;

import gramat.symbols.Symbol;
import gramat.util.Count;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static gramat.util.Validations.isAny;

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

    public Node merge(Node left, Node right) {
        if (left == right) {
            return left;
        }
        var id = String.join("_", left.id, right.id);
        var result = createNode(id);

        for (var link : links) {
            if (isAny(link.source, left, right)) {
                link.source = result;
            }
            if (isAny(link.target, left, right)) {
                link.target = result;
            }
        }

        if (!nodes.remove(left)) {
            throw new RuntimeException();
        }
        if (!nodes.remove(right)) {
            throw new RuntimeException();
        }

        return result;
    }

    public Node createNode() {
        var id = ids.nextString();
        return createNode(id, false);
    }

    public Node createNode(String id) {
        return createNode(id, false);
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

    public Link createLink(Node source, Node target, Symbol symbol) {
        var link = new LinkSymbol(source, target, symbol);
        links.add(link);
        return link;
    }

    public void createLinks(NodeSet sources, NodeSet targets, Symbol symbol) {
        create_links(sources, targets, (source, target) -> new LinkSymbol(source, target, symbol));
    }

    public void createLinks(NodeSet sources, NodeSet targets, String reference) {
        create_links(sources, targets, (source, target) -> new LinkReference(source, target, reference));
    }

    private void create_links(Iterable<Node> sources, Iterable<Node> targets, BiFunction<Node, Node, Link> linkMaker) {
        for (var source : sources) {
            for (var target : targets) {
                var link = linkMaker.apply(source, target);

                links.add(link);
            }
        }
    }

    public List<Link> findLinksTo(NodeSet targets) {
        return links.stream()
                .filter(t -> targets.contains(t.target))
                .collect(Collectors.toList());
    }

    public List<Link> findLinksTo(Node target) {
        return links.stream()
                .filter(t -> t.target == target)
                .collect(Collectors.toList());
    }

    public List<Link> findLinksFrom(NodeSet sources, Symbol symbol) {
        return links.stream()
                .filter(e -> sources.contains(e.source))
                .map(e -> (LinkSymbol)e)  // TODO add validation for this casting
                .filter(e -> Objects.equals(e.symbol, symbol))
                .collect(Collectors.toList());
    }

    public List<Link> findLinksFrom(NodeSet sources) {
        return links.stream()
                .filter(t -> sources.contains(t.source))
                .collect(Collectors.toList());
    }

    public List<Link> findLinksFrom(Node source) {
        return links.stream()
                .filter(t -> t.source == source)
                .collect(Collectors.toList());
    }

    public Set<String> findReferences() {
        var refs = new LinkedHashSet<String>();

        for (var link : links) {
            if (link instanceof LinkReference) {
                refs.add(((LinkReference)link).name);
            }
        }

        return refs;
    }

    public List<Link> listLinksFrom(Node source) {
        return listLinksFrom(new NodeSet(source));
    }

    public List<Link> listLinksTo(Node target) {
        return listLinksTo(new NodeSet(target));
    }

    public List<Link> listLinksFrom(NodeSet sources) {
        var result = new ArrayList<Link>();
        var control = new HashSet<Node>();
        var queue = new LinkedList<Node>();

        queue.addAll(sources.toCollection());

        while (queue.size() > 0) {
            var source = queue.remove();

            if (control.add(source)) {
                for (var link : findLinksFrom(source)) {
                    result.add(link);

                    queue.add(link.target);
                }
            }
        }

        return result;
    }

    public List<Link> listLinksTo(NodeSet targets) {
        var result = new ArrayList<Link>();
        var control = new HashSet<Node>();
        var queue = new LinkedList<Node>();

        queue.addAll(targets.toCollection());

        while (queue.size() > 0) {
            var target = queue.remove();

            if (control.add(target)) {
                for (var link : findLinksTo(target)) {
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
        return new Line(createNode(), createNode());
    }
}
