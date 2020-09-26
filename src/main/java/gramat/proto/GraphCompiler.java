package gramat.proto;

import gramat.actions.*;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.source.ExpressionGrammar;
import gramat.source.expressions.*;
import gramat.util.Count;

import java.util.Objects;

public class GraphCompiler extends DefaultComponent {

    private final Count trxIDs;
    private final GraphGrammar graphGrammar;
    private final ExpressionGrammar expressionGrammar;

    public GraphCompiler(Component parent, ExpressionGrammar expressionGrammar, GraphGrammar graphGrammar) {
        super(parent);
        this.trxIDs = new Count();
        this.expressionGrammar = expressionGrammar;
        this.graphGrammar = graphGrammar;
    }
    
    public Graph compile(String name) {
        return make_graph(name);
    }

    private Graph make_graph(String name) {
        var graph = graphGrammar.searchGraph(name);

        if (graph != null) {
            return graph;
        }

        graph = graphGrammar.createGraph(name);

        var expression = expressionGrammar.findExpression(name);

        graph.entryPoint = compile_expression(graph, graph.entryPoint, expression);

        return graph;
    }

    private Segment compile_expression(Graph graph, Segment container, Expression expression) {
        if (expression instanceof LiteralExpression) {
            return compile_literal((LiteralExpression)expression, graph, container);
        }
        else if (expression instanceof RepetitionExpression) {
            return compile_repetition((RepetitionExpression)expression, graph, container);
        }
        else if (expression instanceof ValueExpression) {
            return compile_value((ValueExpression)expression, graph, container);
        }
        else if (expression instanceof ReferenceExpression) {
            return compile_reference((ReferenceExpression)expression, graph, container);
        }
        else if (expression instanceof SequenceExpression) {
            return compile_sequence((SequenceExpression)expression, graph, container);
        }
        else if (expression instanceof AlternationExpression) {
            return compile_alternation((AlternationExpression)expression, graph, container);
        }
        else if (expression instanceof OptionalExpression) {
            return compile_optional((OptionalExpression)expression, graph, container);
        }
        else if (expression instanceof WildExpression) {
            return compile_wild((WildExpression)expression, graph, container);
        }
        else if (expression instanceof ArrayExpression) {
            return compile_array((ArrayExpression)expression, graph, container);
        }
        else if (expression instanceof ObjectExpression) {
            return compile_object((ObjectExpression)expression, graph, container);
        }
        else if (expression instanceof AttributeExpression) {
            return compile_attribute((AttributeExpression)expression, graph, container);
        }
        else if (expression instanceof NameExpression) {
            return compile_name((NameExpression)expression, graph, container);
        }
        else if (expression instanceof RangeExpression) {
            return compile_range((RangeExpression)expression, graph, container);
        }
        else {
            throw new RuntimeException("Unsupported expression: " + expression);
        }
    }

    private Segment compile_value(ValueExpression value, Graph graph, Segment container) {
        var parser = gramat.parsers.findParser(value.parser);
        var trxID = trxIDs.next();
        var begin = new ValueKeep(trxID);
        var content = compile_expression(graph, container, value.content);
        var end = new ValueHalt(trxID, parser);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_name(NameExpression name, Graph graph, Segment container) {
        var trxID = trxIDs.next();
        var begin = new NameKeep(trxID);
        var content = compile_expression(graph, container, name.content);
        var end = new NameHalt(trxID);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_attribute(AttributeExpression attribute, Graph graph, Segment container) {
        var trxID = trxIDs.next();
        var begin = new AttributeKeep(trxID);
        var content = compile_expression(graph, container, attribute.content);
        var end = new AttributeHalt(trxID, attribute.name);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_object(ObjectExpression object, Graph graph, Segment container) {
        var trxID = trxIDs.next();
        var begin = new ObjectKeep(trxID);
        var content = compile_expression(graph, container, object.content);
        var end = new ObjectHalt(trxID, object.type);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_array(ArrayExpression array, Graph graph, Segment container) {
        var trxID = trxIDs.next();
        var begin = new ArrayKeep(trxID);
        var content = compile_expression(graph, container, array.content);
        var end = new ArrayHalt(trxID, array.type);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment wrap_actions(Graph graph, Segment container, Action make, Action halt) {
        var beginEdges = graph.findEdgesFrom(container.sources);
        var afterEdges = graph.findEdgesTo(container.targets);

        if (beginEdges.isEmpty() || afterEdges.isEmpty()) {
            throw new RuntimeException();
        }

        for (var edge : graph.listEdgesFrom(container.sources)) {
            var isBegin = beginEdges.contains(edge);
            var isAfter = afterEdges.contains(edge);

            if (isBegin || !isAfter) {
                edge.beforeActions.add(0, make);
            }

            if (isAfter) {
                edge.afterActions.add(halt);
            }
        }

        return container;
    }

    private Segment compile_reference(ReferenceExpression reference, Graph graph, Segment container) {
        graph.createEdge(container.sources, container.targets, reference.name);

        return container;
    }

    private Segment compile_range(RangeExpression range, Graph graph, Segment container) {
        var symbol = gramat.symbols.makeRange(range.begin, range.end);

        graph.createEdge(container.sources, container.targets, symbol);

        return container;
    }

    private Segment compile_literal(LiteralExpression literal, Graph graph, Segment container) {
        var chars = literal.value.toCharArray();
        var last = container.sources;

        for (var i = 0; i < chars.length; i++) {
            var symbol = gramat.symbols.makeChar(chars[i]);

            if (i == chars.length - 1) {
                graph.createEdge(last, container.targets, symbol);
                break;
            }
            else {
                var current = graph.createVertexSet();

                graph.createEdge(last, current, symbol);

                last = current;
            }
        }

        return container;
    }

    private Segment compile_repetition(RepetitionExpression repetition, Graph graph, Segment container) {
        if (repetition.separator == null && repetition.minimum == 0) {
            var segmentFW = compile_expression(graph, container, repetition.content);
            var segmentBW = compile_expression(graph, container.copyInverse(), repetition.content);
            var segment = container.copy();

            segment.add(segmentFW);
            segment.add(segmentBW);

            return segment;
        }
        else if (repetition.separator == null && repetition.minimum == 1) {
            var segmentOne = compile_expression(graph, container, repetition.content);
            var segmentLoop = compile_expression(graph, new Segment(container.targets, container.targets), repetition.content);
            var segment = container.copy();

            segment.add(segmentOne);
            segment.targets.add(segmentLoop.targets);

            return segment;
        }
        else if (repetition.separator != null && repetition.minimum == 0) {
            var segmentFW = compile_expression(graph, container, repetition.content);
            var segmentSP = compile_expression(
                    graph, new Segment(segmentFW.targets, graph.createVertexSet()), repetition.separator);
            var segmentBW = compile_expression(
                    graph, new Segment(segmentSP.targets, segmentFW.targets), repetition.content);
            var segment = container.copy();

            segment.add(segmentFW);
            segment.targets.add(segmentFW.sources);
            segment.targets.add(segmentBW.targets);

            return segment;
        }
        else if (repetition.separator != null && repetition.minimum == 1) {
            var segmentFW = compile_expression(graph, container, repetition.content);
            var segmentSP = compile_expression(
                    graph, new Segment(segmentFW.targets, graph.createVertexSet()), repetition.separator);
            var segmentBW = compile_expression(
                    graph, new Segment(segmentSP.targets, segmentFW.targets), repetition.content);
            var segment = container.copy();

            segment.add(segmentFW);
            segment.targets.add(segmentBW.targets);

            return segment;
        }
        else {
            throw new RuntimeException();
        }
    }

    private Segment compile_sequence(SequenceExpression sequence, Graph graph, Segment container) {
        VertexSet first = null;
        var last = container.sources;

        for (var i = 0; i < sequence.items.size(); i++) {
            VertexSet next;

            if (i != sequence.items.size() - 1) {
                next = graph.createVertexSet();
            }
            else {
                // only for last item
                next = container.targets;
            }

            var segment = new Segment(last, next);

            segment = compile_expression(graph, segment, sequence.items.get(i));

            last = segment.targets;

            if (first == null) {
                first = segment.sources;
            }
        }

        Objects.requireNonNull(first);

        return new Segment(first, last);
    }

    private Segment compile_alternation(AlternationExpression alternation, Graph graph, Segment container) {
        var alternationSegment = container.copy();

        for (var option : alternation.options) {
            var optionSegment = compile_expression(graph, container, option);

            alternationSegment.add(optionSegment);
        }

        return alternationSegment;
    }

    private Segment compile_optional(OptionalExpression optional, Graph graph, Segment container) {
        var segment = container.copy();

        compile_expression(graph, segment, optional.content);

        // make it optional
        segment.targets.add(segment.sources);

        return segment;
    }

    private Segment compile_wild(WildExpression wild, Graph graph, Segment container) {
        for (var target : container.targets) {
            target.wild = true;
        }

        for (var source : container.sources) {
            source.wild = true;
        }

        return container;
    }
}
