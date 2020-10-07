package gramat.compilers;

import gramat.actions.*;
import gramat.badges.BadgeMode;
import gramat.models.expressions.*;
import gramat.formatting.NodeFormatter;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.Graph;
import gramat.graph.Segment;
import gramat.graph.NodeSet;
import gramat.util.Count;
import gramat.util.NameMap;

import java.util.Objects;

public class GeneralGraphCompiler extends DefaultComponent {

    private final Count node_ids;
    private final Count trx_ids;

    public GeneralGraphCompiler(Component parent) {
        super(parent);
        this.node_ids = new Count();
        this.trx_ids = new Count();
    }

    public SegmentInput compile(ExpressionInput input) {
        var dependencies = new NameMap<Segment>();
        var main = compile(input.main);

        for (var name : input.dependencies.keySet()) {
            var expr = input.dependencies.find(name);
            var segment = compile(expr);

            dependencies.set(name, segment);
        }

        return new SegmentInput(input.parent, main, dependencies);
    }

    public Segment compile(ModelExpression expression) {
        var graph = new Graph(node_ids);
        var container = graph.segment();

        return compile_expression(graph, container, expression);
    }

    private Segment compile_expression(Graph graph, Segment container, ModelExpression expression) {
        if (expression instanceof ModelLiteral) {
            return compile_literal(graph, container, (ModelLiteral)expression);
        }
        else if (expression instanceof ModelRepetition) {
            return compile_repetition(graph, container, (ModelRepetition)expression);
        }
        else if (expression instanceof ModelValue) {
            return compile_value(graph, container, (ModelValue)expression);
        }
        else if (expression instanceof ModelReference) {
            return compile_reference(graph, container, (ModelReference)expression);
        }
        else if (expression instanceof ModelSequence) {
            return compile_sequence(graph, container, (ModelSequence)expression);
        }
        else if (expression instanceof ModelAlternation) {
            return compile_alternation(graph, container, (ModelAlternation)expression);
        }
        else if (expression instanceof ModelOptional) {
            return compile_optional(graph, container, (ModelOptional)expression);
        }
        else if (expression instanceof ModelWild) {
            return compile_wild(graph, container, (ModelWild)expression);
        }
        else if (expression instanceof ModelArray) {
            return compile_array(graph, container, (ModelArray)expression);
        }
        else if (expression instanceof ModelObject) {
            return compile_object(graph, container, (ModelObject)expression);
        }
        else if (expression instanceof ModelAttribute) {
            return compile_attribute(graph, container, (ModelAttribute)expression);
        }
        else if (expression instanceof ModelName) {
            return compile_name(graph, container, (ModelName)expression);
        }
        else if (expression instanceof ModelRange) {
            return compile_range(graph, container, (ModelRange)expression);
        }
        else {
            throw new RuntimeException("Unsupported expression: " + expression);
        }
    }

    private Segment compile_value(Graph graph, Segment container, ModelValue value) {
        var parser = gramat.parsers.findParser(value.parser);
        var trxID = trx_ids.next();
        var begin = new ValueBegin(trxID);
        var content = compile_expression(graph, container, value.content);
        var end = new ValueEnd(trxID, parser);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_name(Graph graph, Segment container, ModelName name) {
        var trxID = trx_ids.next();
        var begin = new NameBegin(trxID);
        var content = compile_expression(graph, container, name.content);
        var end = new NameEnd(trxID);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_attribute(Graph graph, Segment container, ModelAttribute attribute) {
        var trxID = trx_ids.next();
        var begin = new AttributeBegin(trxID, attribute.name);
        var content = compile_expression(graph, container, attribute.content);
        var end = new AttributeEnd(trxID, attribute.name);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_object(Graph graph, Segment container, ModelObject object) {
        var trxID = trx_ids.next();
        var begin = new ObjectBegin(trxID);
        var content = compile_expression(graph, container, object.content);
        var end = new ObjectEnd(trxID, object.type);

        return wrap_actions(graph, content, begin, end);
    }

    private Segment compile_array(Graph graph, Segment container, ModelArray array) {
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
                link.beforeActions.prepend(make);
            }

            if (isAfter) {
                link.afterActions.append(halt);
            }
        }

        return container;
    }

    private Segment compile_reference(Graph graph, Segment container, ModelReference reference) {
        graph.createLinks(container.sources, container.targets, reference.name);

        return container;
    }

    private Segment compile_range(Graph graph, Segment container, ModelRange range) {
        var symbol = gramat.symbols.range(range.begin, range.end);

        graph.createLinks(container.sources, container.targets, symbol, gramat.badges.empty(), BadgeMode.NONE);

        return container;
    }

    private Segment compile_literal(Graph graph, Segment container, ModelLiteral literal) {
        var chars = literal.value.toCharArray();
        var last = container.sources;
        var badge = gramat.badges.empty();

        for (var i = 0; i < chars.length; i++) {
            var symbol = gramat.symbols.character(chars[i]);

            if (i == chars.length - 1) {
                graph.createLinks(last, container.targets, symbol, badge, BadgeMode.NONE);
                break;
            }
            else {
                var current = graph.createNodeSet();

                graph.createLinks(last, current, symbol, badge, BadgeMode.NONE);

                last = current;
            }
        }

        return container;
    }

    private Segment compile_repetition(Graph graph, Segment container, ModelRepetition repetition) {
        if (repetition.separator == null && repetition.minimum == 0) {
            var result = container.shallowCopy();

            var segmentFW = compile_expression(graph, container, repetition.content);

            result.add(segmentFW);

            var segmentBW = compile_expression(graph, container.shallowCopyInverse(), repetition.content);

            result.add(segmentBW);

            return result;
        }
        else if (repetition.separator == null && repetition.minimum == 1) {
            var result = container.shallowCopy();

            var segmentOne = compile_expression(graph, container, repetition.content);

            result.add(segmentOne);

            var segmentLoop = compile_expression(graph, graph.segment(segmentOne.targets, segmentOne.targets), repetition.content);

            result.targets.add(segmentLoop.targets);

            return result;
        }
        else if (repetition.separator != null && repetition.minimum == 0) {
            var result = container.shallowCopy();

            var segmentFW = compile_expression(
                    graph, container, repetition.content);

            result.targets.add(segmentFW.sources);
            result.targets.add(segmentFW.targets);

            var segmentSP = compile_expression(
                    graph, graph.segment(segmentFW.targets, graph.createNodeSet()), repetition.separator);

            result.targets.add(segmentSP.sources);

            var segmentBW = compile_expression(
                    graph, graph.segment(segmentSP.targets, segmentSP.sources), repetition.content);

            result.targets.add(segmentBW.targets);
            return result;
        }
        else if (repetition.separator != null && repetition.minimum == 1) {
            var result = container.shallowCopy();

            var segmentFW = compile_expression(
                    graph, container, repetition.content);

            result.targets.add(segmentFW.targets);

            var segmentSP = compile_expression(
                    graph, graph.segment(segmentFW.targets, graph.createNodeSet()), repetition.separator);

            result.targets.add(segmentSP.sources);

            var segmentBW = compile_expression(
                    graph, graph.segment(segmentSP.targets, segmentSP.sources), repetition.content);

            result.targets.add(segmentBW.targets);
            return result;
        }
        else {
            throw new RuntimeException();
        }
    }

    private Segment compile_sequence(Graph graph, Segment container, ModelSequence sequence) {
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

    private Segment compile_alternation(Graph graph, Segment container, ModelAlternation alternation) {
        var alternationSegment = container.shallowCopy();

        for (var option : alternation.options) {
            var optionSegment = compile_expression(graph, container, option);

            alternationSegment.add(optionSegment);
        }

        return alternationSegment;
    }

    private Segment compile_optional(Graph graph, Segment container, ModelOptional optional) {
        var segment = container.shallowCopy();

        compile_expression(graph, segment, optional.content);

        // make it optional
        segment.targets.add(segment.sources);

        return segment;
    }

    private Segment compile_wild(Graph graph, Segment container, ModelWild wild) {
        for (var target : container.targets) {
            target.wild = true;
        }

        for (var source : container.sources) {
            source.wild = true;
        }

        return container;
    }
}
