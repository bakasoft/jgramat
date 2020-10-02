package gramat.compiling;

import gramat.actions.*;
import gramat.expressions.Expression;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.Graph;
import gramat.graph.Segment;
import gramat.graph.NodeSet;
import gramat.expressions.impl.*;
import gramat.graph.Token;
import gramat.util.Count;

import java.util.Objects;

public class ExpressionCompiler extends DefaultComponent {

    private final Count node_ids;
    private final Count trx_ids;

    public ExpressionCompiler(Component parent) {
        super(parent);
        this.node_ids = new Count();
        this.trx_ids = new Count();
    }

    public Segment compile(Expression expression) {
        var graph = new Graph(node_ids);
        var container = graph.segment();

        return compile_expression(graph, container, expression);
    }

    private Segment compile_expression(Graph graph, Segment container, Expression expression) {
        if (expression instanceof LiteralExpression) {
            return compile_literal(graph, container, (LiteralExpression)expression);
        }
        else if (expression instanceof RepetitionExpression) {
            return compile_repetition(graph, container, (RepetitionExpression)expression);
        }
        else if (expression instanceof ValueExpression) {
            return compile_value(graph, container, (ValueExpression)expression);
        }
        else if (expression instanceof ReferenceExpression) {
            return compile_reference(graph, container, (ReferenceExpression)expression);
        }
        else if (expression instanceof SequenceExpression) {
            return compile_sequence(graph, container, (SequenceExpression)expression);
        }
        else if (expression instanceof AlternationExpression) {
            return compile_alternation(graph, container, (AlternationExpression)expression);
        }
        else if (expression instanceof OptionalExpression) {
            return compile_optional(graph, container, (OptionalExpression)expression);
        }
        else if (expression instanceof WildExpression) {
            return compile_wild(graph, container, (WildExpression)expression);
        }
        else if (expression instanceof ArrayExpression) {
            return compile_array(graph, container, (ArrayExpression)expression);
        }
        else if (expression instanceof ObjectExpression) {
            return compile_object(graph, container, (ObjectExpression)expression);
        }
        else if (expression instanceof AttributeExpression) {
            return compile_attribute(graph, container, (AttributeExpression)expression);
        }
        else if (expression instanceof NameExpression) {
            return compile_name(graph, container, (NameExpression)expression);
        }
        else if (expression instanceof RangeExpression) {
            return compile_range(graph, container, (RangeExpression)expression);
        }
        else {
            throw new RuntimeException("Unsupported expression: " + expression);
        }
    }

    private Segment compile_value(Graph graph, Segment container, ValueExpression value) {
        var parser = gramat.parsers.findParser(value.parser);
        var trxID = trx_ids.next();
        var begin = new ValueBegin(trxID);
        var content = compile_expression(graph, container, value.content);
        var end = new ValueEnd(trxID, parser);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_name(Graph graph, Segment container, NameExpression name) {
        var trxID = trx_ids.next();
        var begin = new NameBegin(trxID);
        var content = compile_expression(graph, container, name.content);
        var end = new NameEnd(trxID);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_attribute(Graph graph, Segment container, AttributeExpression attribute) {
        var trxID = trx_ids.next();
        var begin = new AttributeBegin(trxID, attribute.name);
        var content = compile_expression(graph, container, attribute.content);
        var end = new AttributeEnd(trxID, attribute.name);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_object(Graph graph, Segment container, ObjectExpression object) {
        var trxID = trx_ids.next();
        var begin = new ObjectBegin(trxID);
        var content = compile_expression(graph, container, object.content);
        var end = new ObjectEnd(trxID, object.type);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_array(Graph graph, Segment container, ArrayExpression array) {
        var trxID = trx_ids.next();
        var begin = new ArrayBegin(trxID);
        var content = compile_expression(graph, container, array.content);
        var end = new ArrayEnd(trxID, array.type);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment wrap_actions(Graph graph, Segment container, Action make, Action halt) {
        var beginLinks = graph.findOutgoingLinks(container.sources);
        var afterLinks = graph.findIncomingLinks(container.targets);

        if (beginLinks.isEmpty() || afterLinks.isEmpty()) {
            throw new RuntimeException();
        }

        for (var link : graph.walkLinksFrom(container.sources)) {
            var isBegin = beginLinks.contains(link);
            var isAfter = afterLinks.contains(link);

            if (isBegin) {
                link.beforeActions.addTop(make);
            }

            if (isAfter) {
                link.afterActions.add(halt);
            }
        }

        return container;
    }

    private Segment compile_reference(Graph graph, Segment container, ReferenceExpression reference) {
        graph.createLinks(container.sources, container.targets, Token.of(reference.name));

        return container;
    }

    private Segment compile_range(Graph graph, Segment container, RangeExpression range) {
        var symbol = gramat.symbols.makeRange(range.begin, range.end);

        graph.createLinks(container.sources, container.targets, Token.of(symbol));

        return container;
    }

    private Segment compile_literal(Graph graph, Segment container, LiteralExpression literal) {
        var chars = literal.value.toCharArray();
        var last = container.sources;

        for (var i = 0; i < chars.length; i++) {
            var token = Token.of(gramat.symbols.makeChar(chars[i]));

            if (i == chars.length - 1) {
                graph.createLinks(last, container.targets, token);
                break;
            }
            else {
                var current = graph.createNodeSet();

                graph.createLinks(last, current, token);

                last = current;
            }
        }

        return container;
    }

    private Segment compile_repetition(Graph graph, Segment container, RepetitionExpression repetition) {
        if (repetition.separator == null && repetition.minimum == 0) {
            var segmentFW = compile_expression(graph, container, repetition.content);
            var segmentBW = compile_expression(graph, container.shallowCopyInverse(), repetition.content);
            var segment = container.shallowCopy();

            segment.add(segmentFW);
            segment.add(segmentBW);

            return segment;
        }
        else if (repetition.separator == null && repetition.minimum == 1) {
            var segmentOne = compile_expression(graph, container, repetition.content);
            var segmentLoop = compile_expression(graph, graph.segment(container.targets, container.targets), repetition.content);
            var segment = container.shallowCopy();

            segment.add(segmentOne);
            segment.targets.add(segmentLoop.targets);

            return segment;
        }
        else if (repetition.separator != null && repetition.minimum == 0) {
            var segmentFW = compile_expression(graph, container, repetition.content);
            var segmentSP = compile_expression(
                    graph, graph.segment(segmentFW.targets, graph.createNodeSet()), repetition.separator);
            var segmentBW = compile_expression(
                    graph, graph.segment(segmentSP.targets, segmentFW.targets), repetition.content);
            var segment = container.shallowCopy();

            segment.add(segmentFW);
            segment.targets.add(segmentFW.sources);
            segment.targets.add(segmentBW.targets);

            return segment;
        }
        else if (repetition.separator != null && repetition.minimum == 1) {
            var segmentFW = compile_expression(graph, container, repetition.content);
            var segmentSP = compile_expression(
                    graph, graph.segment(segmentFW.targets, graph.createNodeSet()), repetition.separator);
            var segmentBW = compile_expression(
                    graph, graph.segment(segmentSP.targets, segmentFW.targets), repetition.content);
            var segment = container.shallowCopy();

            segment.add(segmentFW);
            segment.targets.add(segmentBW.targets);

            return segment;
        }
        else {
            throw new RuntimeException();
        }
    }

    private Segment compile_sequence(Graph graph, Segment container, SequenceExpression sequence) {
        NodeSet first = null;
        var last = container.sources;

        for (var i = 0; i < sequence.items.size(); i++) {
            NodeSet next;

            if (i != sequence.items.size() - 1) {
                next = graph.createNodeSet();
            }
            else {
                // only for last item
                next = container.targets;
            }

            var segment = graph.segment(last, next);

            segment = compile_expression(graph, segment, sequence.items.get(i));

            last = segment.targets;

            if (first == null) {
                first = segment.sources;
            }
        }

        Objects.requireNonNull(first);

        return graph.segment(first, last);
    }

    private Segment compile_alternation(Graph graph, Segment container, AlternationExpression alternation) {
        var alternationSegment = container.shallowCopy();

        for (var option : alternation.options) {
            var optionSegment = compile_expression(graph, container, option);

            alternationSegment.add(optionSegment);
        }

        return alternationSegment;
    }

    private Segment compile_optional(Graph graph, Segment container, OptionalExpression optional) {
        var segment = container.shallowCopy();

        compile_expression(graph, segment, optional.content);

        // make it optional
        segment.targets.add(segment.sources);

        return segment;
    }

    private Segment compile_wild(Graph graph, Segment container, WildExpression wild) {
        for (var target : container.targets) {
            target.wild = true;
        }

        for (var source : container.sources) {
            source.wild = true;
        }

        return container;
    }
}
