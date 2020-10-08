package gramat.pipeline;

import gramat.actions.*;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.graph.Node;
import gramat.graph.Root;
import gramat.models.expressions.*;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.Graph;
import gramat.util.Chain;
import gramat.util.Count;
import gramat.util.NameMap;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BlueprintCompiler extends DefaultComponent {

    public static Blueprint compile(Component parent, Sentence sentence) {
        var compiler = new BlueprintCompiler(parent, sentence);

        return new Blueprint(compiler.graph, compiler.result, compiler.dependencies);
    }

    private final Graph graph;
    private final Count trxIds;
    private final Sentence sentence;
    private final NameMap<Root> dependencies;
    private final Queue<String> dependencyQueue;

    private final Root result;

    private BlueprintCompiler(Component parent, Sentence sentence) {
        super(parent);
        this.trxIds = new Count();
        this.graph = new Graph();
        this.sentence = sentence;
        this.dependencies = new NameMap<>();
        this.dependencyQueue = new LinkedList<>();

        this.result = compile(sentence.expression);

        while (!dependencyQueue.isEmpty()) {
            var reference = dependencyQueue.remove();

            if (!dependencies.containsKey(reference)) {
                var expr = sentence.dependencies.find(reference);
                var root = compile(expr);

                dependencies.set(reference, root);
            }
        }
    }

    private Root compile(ModelExpression expression) {
        var initial = graph.createNode();
        var accepted = compileExpression(graph, initial, graph.createNode(), expression);
        return new Root(initial, accepted);
    }

    private Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Node target, ModelExpression expression) {
        var targets = Chain.of(target);

        for (var initial : sources) {
            targets = targets.merge(compileExpression(graph, initial, target, expression));
        }

        return targets;
    }

    private Chain<Node> compileExpression(Graph graph, Node source, Chain<Node> targets, ModelExpression expression) {
        for (var accepted : targets) {
            targets = targets.merge(compileExpression(graph, source, accepted, expression));
        }

        return targets;
    }

    private Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Chain<Node> targets, ModelExpression expression) {
        for (var initial : sources) {
            for (var accepted : targets) {
                targets = targets.merge(compileExpression(graph, initial, accepted, expression));
            }
        }

        return targets;
    }

    private Chain<Node> compileExpression(Graph graph, Node source, Node target, ModelExpression expression) {
        if (expression instanceof ModelLiteral) {
            return compileLiteral(graph, source, target, (ModelLiteral)expression);
        }
        else if (expression instanceof ModelOptional) {
            return compileOptional(graph, source, target, (ModelOptional)expression);
        }
        else if (expression instanceof ModelRepetition) {
            return compileRepetition(graph, source, target, (ModelRepetition)expression);
        }
        else if (expression instanceof ModelValue) {
            return compileValue(graph, source, target, (ModelValue)expression);
        }
        else if (expression instanceof ModelReference) {
            return compileReference(graph, source, target, (ModelReference)expression);
        }
        else if (expression instanceof ModelSequence) {
            return compileSequence(graph, source, target, (ModelSequence)expression);
        }
        else if (expression instanceof ModelAlternation) {
            return compileAlternation(graph, source, target, (ModelAlternation)expression);
        }
        else if (expression instanceof ModelWild) {
            return compileWild(graph, source, target, (ModelWild)expression);
        }
        else if (expression instanceof ModelArray) {
            return compileArray(graph, source, target, (ModelArray)expression);
        }
        else if (expression instanceof ModelObject) {
            return compileObject(graph, source, target, (ModelObject)expression);
        }
        else if (expression instanceof ModelAttribute) {
            return compileAttribute(graph, source, target, (ModelAttribute)expression);
        }
        else if (expression instanceof ModelName) {
            return compileName(graph, source, target, (ModelName)expression);
        }
        else if (expression instanceof ModelRange) {
            return compileRange(graph, source, target, (ModelRange)expression);
        }
        else {
            throw new UnsupportedValueException(expression);
        }
    }

    private Chain<Node> compileValue(Graph graph, Node source, Node target, ModelValue value) {
        var parser = gramat.parsers.findParser(value.parser);
        var trxID = trxIds.next();
        var begin = new ValueBegin(trxID);
        var content = compileExpression(graph, source, target, value.content);
        var end = new ValueEnd(trxID, parser);

        return wrapActions(graph, source, content, begin, end);
    }

    private Chain<Node> compileName(Graph graph, Node source, Node target, ModelName name) {
        var trxID = trxIds.next();
        var begin = new NameBegin(trxID);
        var content = compileExpression(graph, source, target, name.content);
        var end = new NameEnd(trxID);

        return wrapActions(graph, source, content, begin, end);
    }

    private Chain<Node> compileAttribute(Graph graph, Node source, Node target, ModelAttribute attribute) {
        var trxID = trxIds.next();
        var begin = new AttributeBegin(trxID, attribute.name);
        var content = compileExpression(graph, source, target, attribute.content);
        var end = new AttributeEnd(trxID, attribute.name);

        return wrapActions(graph, source, content, begin, end);
    }

    private Chain<Node> compileObject(Graph graph, Node source, Node target, ModelObject object) {
        var trxID = trxIds.next();
        var begin = new ObjectBegin(trxID);
        var content = compileExpression(graph, source, target, object.content);
        var end = new ObjectEnd(trxID, object.type);

        return wrapActions(graph, source, content, begin, end);
    }

    private Chain<Node> compileArray(Graph graph, Node source, Node target, ModelArray array) {
        var trxID = trxIds.next();
        var begin = new ArrayBegin(trxID);
        var content = compileExpression(graph, source, target, array.content);
        var end = new ArrayEnd(trxID, array.type);

        return wrapActions(graph, source, content, begin, end);
    }

    private Chain<Node> wrapActions(Graph graph, Node source, Chain<Node> targets, Action make, Action halt) {
        var beginLinks = graph.findOutgoingLinks(source);
        var afterLinks = graph.findIncomingLinks(targets);

        if (beginLinks.isEmpty() || afterLinks.isEmpty()) {
            throw new RuntimeException();
        }

        for (var link : graph.findLinksBetween(source, targets)) {
            var isBegin = beginLinks.contains(link);
            var isAfter = afterLinks.contains(link);

            if (isBegin) {
                link.beforeActions.prepend(make);
            }

            if (isAfter) {
                link.afterActions.append(halt);
            }
        }

        return targets;
    }

    private Chain<Node> compileReference(Graph graph, Node source, Node target, ModelReference reference) {
        var symbol = gramat.symbols.reference(reference.name);
        var badge = gramat.badges.empty();

        graph.createLink(source, target, symbol, badge, BadgeMode.NONE);

        if (!dependencyQueue.contains(reference.name)) {
            dependencyQueue.add(reference.name);
        }

        return Chain.of(target);
    }

    private Chain<Node> compileRange(Graph graph, Node source, Node target, ModelRange range) {
        var symbol = gramat.symbols.range(range.begin, range.end);

        graph.createLink(source, target, symbol, gramat.badges.empty(), BadgeMode.NONE);

        return Chain.of(target);
    }

    private Chain<Node> compileLiteral(Graph graph, Node source, Node target, ModelLiteral literal) {
        var chars = literal.value.toCharArray();
        var badge = gramat.badges.empty();
        Node last = null;

        for (var i = 0; i < chars.length; i++) {
            Node itemSource;
            Node itemTarget;

            if (i == 0) {
                itemSource = source;
            }
            else {
                itemSource = last;
            }

            if (i == chars.length - 1) {
                // only for last item
                itemTarget = target;
            }
            else {
                itemTarget = graph.createNode();
            }

            var symbol = gramat.symbols.character(chars[i]);

            graph.createLink(itemSource, itemTarget, symbol, badge, BadgeMode.NONE);

            last = itemTarget;
        }

        if (last == null) {
            return Chain.of(source, target);
        }

        return Chain.of(last);
    }

    private Chain<Node> compileRepetition(Graph graph, Node source, Node target, ModelRepetition repetition) {
        if (repetition.separator == null && repetition.minimum == 0) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedBack = compileExpression(graph, acceptedOne, source, repetition.content);
            return acceptedOne.merge(acceptedBack);
        }
        else if (repetition.separator == null && repetition.minimum == 1) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedLoop = compileExpression(graph, acceptedOne, acceptedOne, repetition.content);
            return acceptedOne.merge(acceptedLoop);
        }
        else if (repetition.separator != null && repetition.minimum == 0) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedSep = compileExpression(graph, acceptedOne, graph.createNode(), repetition.separator);
            var acceptedLoop = compileExpression(graph, acceptedSep, acceptedOne, repetition.content);
            return acceptedOne.merge(acceptedLoop).merge(source);
        }
        else if (repetition.separator != null && repetition.minimum == 1) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedSep = compileExpression(graph, acceptedOne, graph.createNode(), repetition.separator);
            var acceptedLoop = compileExpression(graph, acceptedSep, acceptedOne, repetition.content);
            return acceptedOne.merge(acceptedLoop);
        }
        else {
            throw new RuntimeException();
        }
    }

    private Chain<Node> compileSequence(Graph graph, Node source, Node target, ModelSequence sequence) {
        Chain<Node> last = null;

        for (var i = 0; i < sequence.items.size(); i++) {
            Chain<Node> itemSource;
            Chain<Node> itemTarget;

            if (i == 0) {
                itemSource = Chain.of(source);
            }
            else {
                itemSource = last;
            }

            if (i == sequence.items.size() - 1) {
                // only for last item
                itemTarget = Chain.of(target);
            }
            else {
                itemTarget = Chain.of(graph.createNode());
            }

            last = compileExpression(graph, itemSource, itemTarget, sequence.items.get(i));
        }

        if (last == null) {
            return Chain.of(source, target);
        }

        return last;
    }

    private Chain<Node> compileAlternation(Graph graph, Node source, Node target, ModelAlternation alternation) {
        var targets = Chain.of(target);

        for (var option : alternation.options) {
            var optionAccepted = compileExpression(graph, source, target, option);

            targets = targets.merge(optionAccepted);
        }

        return targets;
    }

    private Chain<Node> compileOptional(Graph graph, Node source, Node target, ModelOptional optional) {
        var contentAccepted = compileExpression(graph, source, target, optional.content);

        return contentAccepted.merge(source);
    }

    private Chain<Node> compileWild(Graph graph, Node source, Node target, ModelWild wild) {
        source.wild = true;
        target.wild = true;
        return Chain.of(source, target);
    }
}
