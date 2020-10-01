//package gramat.model;
//
//import gramat.pivot.XState;
//import gramat.pivot.XTransition;
//import gramat.pivot.data.TDSymbol;
//import gramat.symbols.Symbol;
//import gramat.util.Count;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class SectionGrammar {
//
//    private final Map<String, Section> sections;
//
//    public final List<Link> links;
//
//    private final Count nodeIds;
//
//    public SectionGrammar() {
//        this.sections = new HashMap<>();
//        this.links = new ArrayList<>();
//        this.nodeIds = new Count();
//    }
//
//    public Section createSection(String name) {
//        var initial = createNode();
//        var accepted = createNode();
//        var segment = new Section(initial, accepted);
//
//        sections.put(name, segment);
//
//        return segment;
//    }
//
//    public Node createNode() {
//        var state = new Node(nodeIds.nextString());
//
//        return state;
//    }
//
//    public Link createLink(Node source, Node target) {
//        var link = new Link(source, target);
//
//        links.add(link);
//
//        return link;
//    }
//
//    public LinkSymbol createLink(Node source, Node target, Symbol symbol) {
//        var link = new LinkSymbol(source, target, symbol);
//
//        links.add(link);
//
//        return link;
//    }
//
//    public Section searchSection(String name) {
//        return sections.get(name);
//    }
//
//    public List<Link> findLinksTo(Node target) {
//        return links.stream()
//                .filter(t -> t.target == target)
//                .collect(Collectors.toList());
//    }
//
//    public List<Link> findLinksFrom(Node source) {
//        return links.stream()
//                .filter(t -> t.source == source)
//                .collect(Collectors.toList());
//    }
//
//    public List<LinkSymbol> findLinksFrom(Set<Node> sources, Symbol symbol) {
//        var result = new ArrayList<LinkSymbol>();
//
//        for (var link : links) {
//            if (link instanceof LinkSymbol && sources.contains(link.source)) {
//                var linkSymbol = (LinkSymbol)link;
//
//                if (Objects.equals(linkSymbol.symbol, symbol)) {
//                    result.add(linkSymbol);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public Set<LinkSymbol> findForwardSymbols(Node source) {
//        var result = new LinkedHashSet<LinkSymbol>();
//        var control = new HashSet<Node>();
//        var queue = new LinkedList<Node>();
//
//        queue.add(source);
//
//        while (queue.size() > 0) {
//            var state = queue.remove();
//
//            if (control.add(state)) {
//                for (var trn : findLinksFrom(state)) {
//                    if (trn instanceof LinkSymbol) {
//                        result.add((LinkSymbol)trn);
//                    }
//                    else {
//                        queue.add(trn.target);
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public Set<LinkSymbol> findBackwardSymbols(Node target) {
//        var result = new LinkedHashSet<LinkSymbol>();
//        var control = new HashSet<Node>();
//        var queue = new LinkedList<Node>();
//
//        queue.add(target);
//
//        while (queue.size() > 0) {
//            var state = queue.remove();
//
//            if (control.add(state)) {
//                for (var trn : findLinksTo(state)) {
//                    if (trn instanceof LinkSymbol) {
//                        result.add((LinkSymbol)trn);
//                    }
//                    else {
//                        queue.add(trn.source);
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public boolean areForwardLinked(Node source, Node target) {
//        var control = new HashSet<Node>();
//        var queue = new LinkedList<Node>();
//
//        queue.add(source);
//
//        while (queue.size() > 0) {
//            var state = queue.remove();
//
//            if (control.add(state)) {
//                for (var trn : findLinksFrom(state)) {
//                    if (trn.target == target) {
//                        return true;
//                    }
//                    else {
//                        queue.add(trn.target);
//                    }
//                }
//            }
//        }
//
//        return false;
//    }
//
//    public boolean areBackwardLinked(Node target, Node source) {
//        var control = new HashSet<Node>();
//        var queue = new LinkedList<Node>();
//
//        queue.add(target);
//
//        while (queue.size() > 0) {
//            var state = queue.remove();
//
//            if (control.add(state)) {
//                for (var trn : findLinksTo(state)) {
//                    if (trn.source == source) {
//                        return true;
//                    }
//                    else {
//                        queue.add(trn.source);
//                    }
//                }
//            }
//        }
//
//        return false;
//    }
//
//    public Set<Node> computeEmptyClosure(Node node) {
//        return computeEmptyClosure(Set.of(node));
//    }
//
//    public Set<Node> computeEmptyClosure(Set<Node> nodes) {
//        var closure = new LinkedHashSet<Node>();
//        var queue = new LinkedList<Node>();
//
//        queue.addAll(nodes);
//
//        while (queue.size() > 0) {
//            var source = queue.remove();
//
//            if (closure.add(source)) {
//                for (var link : findLinksFrom(source)) {
//                    if (!(link instanceof LinkSymbol)) {
//                        queue.add(link.target);
//                    }
//                }
//            }
//        }
//
//        return closure;
//    }
//
//    public Set<Node> computeEmptyInverseClosure(Set<Node> nodes) {
//        var closure = new LinkedHashSet<Node>();
//        var queue = new LinkedList<Node>();
//
//        queue.addAll(nodes);
//
//        while (queue.size() > 0) {
//            var target = queue.remove();
//
//            if (closure.add(target)) {
//                for (var link : findLinksTo(target)) {
//                    if (!(link instanceof LinkSymbol)) {
//                        queue.add(link.source);
//                    }
//                }
//            }
//        }
//
//        return closure;
//    }
//}
