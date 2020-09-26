package gramat.source;

import gramat.actions.Action;
import gramat.nodes.Node;
import gramat.nodes.impl.*;
import gramat.source.model.*;
import gramat.symbols.*;

import java.util.*;

public class Decompiler {

    private final MSource source;

    private final Map<Node, MSourceRule> nodeRules;

    private int ruleCount;

    public Decompiler() {
        source = new MSource();
        source.members = new ArrayList<>();

        nodeRules = new HashMap<>();

        ruleCount = 0;
    }

    public MSourceRule decompile(Node node) {
        return decompile(node, null);
    }

    public MSourceRule decompile(Node node, String name) {
        var targetNodes = detect_rules(node);
        var targetRules = new HashMap<Node, MSourceRule>();

        for (var targetNode : targetNodes) {
            var rule = new MSourceRule();

            if (targetNode == node && name != null) {
                rule.name = name;
            }
            else {
                rule.name = "r" + ruleCount;
                ruleCount++;
            }

            nodeRules.put(targetNode, rule);
            source.members.add(rule);

            targetRules.put(targetNode, rule);
        }

        for (var entry : targetRules.entrySet()) {
            var targetNode = entry.getKey();
            var targetRule = entry.getValue();

            targetRule.expression = make_expression(targetNode, false);
        }

        return nodeRules.get(node);
    }

    private List<Node> detect_rules(Node root) {
        var nodeUses = new LinkedHashMap<Node, Integer>();
        var queue = new LinkedList<Node>();

        queue.add(root);

        while (queue.size() > 0) {
            var node = queue.remove();

            if (nodeUses.containsKey(node)) {
                nodeUses.put(node, nodeUses.get(node) + 1);
            }
            else {
                nodeUses.put(node, 1);

                queue.addAll(node.getNodes());
            }
        }

        var result = new ArrayList<Node>();

        // keep only nodes with more than one uses
        // discard symbol nodes
        for (var entry : nodeUses.entrySet()) {
            var node = entry.getKey();
            var count = entry.getValue();

            if (count > 1 && !(node instanceof NodeSymbol)) {
                result.add(node);
            }
        }

        // root node must be in the list
        if (!result.contains(root)) {
            result.add(0, root);
        }

        return result;
    }

    private MExpression make_expression(Node node) {
        return make_expression(node, true);
    }

    private MExpression make_expression(Node node, boolean allowReferences) {
        if (allowReferences) {
            var rule = nodeRules.get(node);

            if (rule == null) {
                return make_expression(node, false);
            }

            var reference = new MExpressionReference();

            reference.name = rule.name;

            return reference;
        }
        else if (node instanceof NodeAlternation) {
            return make_alternation((NodeAlternation)node);
        }
        else if (node instanceof NodeOptional) {
            return make_optional((NodeOptional)node);
        }
        else if (node instanceof NodeRepeat) {
            return make_repeat((NodeRepeat)node);
        }
        else if (node instanceof NodeSequence) {
            return make_sequence((NodeSequence)node);
        }
        else if (node instanceof NodeSymbol) {
            return make_symbol((NodeSymbol)node);
        }
        else {
            throw new RuntimeException("unsupported node: " + node);
        }
    }

    private MExpression make_alternation(NodeAlternation alternation) {
        var result = new MExpressionAlternation();

        result.options = new ArrayList<>();

        for (var node : alternation.getNodes()) {
            var option = make_expression(node);

            result.options.add(option);
        }

        return wrap_with_actions(alternation, result);
    }

    private MExpression make_optional(NodeOptional node) {
        var result = new MExpressionOptional();

        result.content = make_expression(node.getContent());

        return wrap_with_actions(node, result);

    }

    private MExpression make_repeat(NodeRepeat node) {
        var result = new MExpressionRepeat();

        result.content = make_expression(node.getContent());

        return wrap_with_actions(node, result);
    }

    private MExpression make_sequence(NodeSequence sequence) {
        var result = new MExpressionSequence();

        result.items = new ArrayList<>();

        for (var node : sequence.getNodes()) {
            var option = make_expression(node);

            result.items.add(option);
        }

        return wrap_with_actions(sequence, result);
    }

    private MExpression make_symbol(NodeSymbol node) {
        var result = new MExpressionSymbol();

        result.symbol = make_symbol(node.getSymbol());

        return wrap_with_actions(node, result);
    }

    private MExpression wrap_with_actions(Node node, MExpression content) {
        if (node.hasActions()) {
            return content;
        }

        var mSequence = new MExpressionSequence();

        mSequence.items = new ArrayList<>();

        for (var action : node.getPreActions()) {
            mSequence.items.add(make_action(action, "before"));
        }

        mSequence.items.add(content);

        for (var action : node.getPostActions()) {
            mSequence.items.add(make_action(action, "after"));
        }

        return mSequence;
    }

    private MExpressionAction make_action(Action action, String keyword) {
        var result = new MExpressionAction();

        result.keyword = keyword;
        result.attributes = new ArrayList<>();

        var attr = new MActionAttribute();
        attr.key = action.getClass().getSimpleName();
        result.attributes.add(attr);

        for (var attrEntry : action.getAttributes().entrySet()) {
            attr = new MActionAttribute();
            attr.key = attrEntry.getKey();
            attr.value = attrEntry.getValue();
            result.attributes.add(attr);
        }

        return result;
    }

    private MSymbol make_symbol(Symbol symbol) {
        if (symbol instanceof SymbolLiteral) {
            return make_symbol_literal((SymbolLiteral)symbol);
        }
        else if (symbol instanceof SymbolNot) {
            return make_symbol_not((SymbolNot)symbol);
        }
        else if (symbol instanceof SymbolRange) {
            return make_symbol_range((SymbolRange)symbol);
        }
        else if (symbol instanceof SymbolUnion) {
            return make_symbol_union((SymbolUnion)symbol);
        }
        else {
            throw new RuntimeException("unsupported symbol: " + symbol);
        }
    }

    private MSymbol make_symbol_literal(SymbolLiteral symbol) {
        var result = new MSymbolLiteral();

        result.value = symbol.getValue();

        return result;
    }

    private MSymbol make_symbol_not(SymbolNot symbol) {
        var result = new MSymbolNot();

        result.symbol = make_symbol(symbol.getSymbol());

        return result;
    }

    private MSymbol make_symbol_range(SymbolRange symbol) {
        var result = new MSymbolRange();

        result.start = symbol.getStart();
        result.end = symbol.getEnd();

        return result;
    }

    private MSymbol make_symbol_union(SymbolUnion symbol) {
        var result = new MSymbolUnion();

        result.symbols = new ArrayList<>();

        for (var item : symbol.getSymbols()) {
            result.symbols.add(make_symbol(item));
        }

        return result;
    }

    public MSource getSource() {
        return source;
    }

}
