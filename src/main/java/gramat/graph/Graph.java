package gramat.graph;

import gramat.actions.Action;
import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;
import gramat.util.Chain;
import gramat.util.Count;
import gramat.util.TokenGenerator;

import javax.naming.LinkRef;
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

    public void createLinks(Chain<Node> sources, Chain<Node> targets, Symbol symbol, Badge badge, BadgeMode mode) {
        for (var source : sources) {
            for (var target : targets) {
                createLink(source, target, symbol, badge, mode);
            }
        }
    }

    public void createLinks(Chain<Node> sources, Chain<Node> targets, String reference) {
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

    public List<LinkSymbol> findTransitions(Chain<Node> sources, Symbol symbol, Badge badge) {
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
        var result = new ArrayList<Link>();
        var control = new HashSet<Node>();
        var queue = new LinkedList<Node>();

        queue.addAll(sources.toList());

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

    public List<Link> findLinksBetween(Node source, Node target) {
        return findLinksBetween(source, Chain.of(target));
    }

    public List<Link> findLinksBetween(Root root) {
        return findLinksBetween(root.source, root.targets);
    }

    public List<Link> findLinksBetween(Node source, Chain<Node> targets) {
        var result = new ArrayList<Link>();
        var control = new HashSet<Node>();
        var queue = new LinkedList<Node>();

        queue.add(source);

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
            if (link instanceof LinkReference && control.contains(link.target)) {
                result.add(link);
            }
        }

        return result;
    }

    public List<LinkReference> findReferencesBetween(Node source, Chain<Node> target) {
        // TODO optimize this filter
        return findLinksBetween(source, target).stream()
                .filter(l -> l instanceof LinkReference)
                .map(l -> (LinkReference)l).collect(Collectors.toList());
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
