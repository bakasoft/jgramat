package gramat.pipeline.assembling;

import gramat.scheme.common.badges.Badge;
import gramat.scheme.common.badges.BadgeSource;
import gramat.exceptions.UnsupportedValueException;
import gramat.pipeline.compiling.ExtensionMaker;
import gramat.pipeline.compiling.Stats;
import gramat.scheme.models.Template;
import gramat.framework.Context;
import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.models.Root;
import gramat.scheme.models.plugs.Extension;
import gramat.scheme.common.parsers.ParserSource;
import gramat.scheme.common.parsers.ValueParser;
import gramat.scheme.data.expressions.*;
import gramat.scheme.common.symbols.Alphabet;
import gramat.util.NameMap;
import gramat.scheme.models.sets.NodeSet;

import java.util.*;

public class ExpressionAssembler implements BaseAssembler,
        AlternationAssembler,
        LiteralAssembler,
        OptionalAssembler,
        RangeAssembler,
        ReferenceAssembler,
        RepetitionAssembler,
        SequenceAssembler,
        WildAssembler,
        SpecialAssembler {

    public static Template build(Context ctx, Graph graph, ExpressionData main, NameMap<ExpressionData> expressions, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        try (var ignore = ctx.pushLayer("Building Expression");) {

            ctx.info("Analyzing expression...");

            var stats = Stats.compute(main, expressions);

            ctx.info("Building main expression...");
            ctx.setTotal(0, stats.count);

            var builder = new ExpressionAssembler(ctx, expressions, stats, alphabet, badges, parsers);
            var root = builder.build(graph, main);
            var extensions = new NameMap<Extension>();

            for (var reference : stats.recursiveReferences) {
                ctx.info("Building %s expression...", reference);

                var refExpr = expressions.find(reference);
                var refRoot = builder.build(graph, refExpr);
                var refExt = ExtensionMaker.make(graph, refRoot, extensions.size());

                extensions.set(reference, refExt);
            }

            return new Template(graph, root, extensions);
        }
    }

    private final Context ctx;
    private final NameMap<ExpressionData> expressions;
    private final Queue<String> dependencyQueue;
    private final Stats stats;
    private final Alphabet symbols;
    private final BadgeSource badges;
    private final ParserSource parsers;

    private final Set<ExpressionData> buildCount;

    private final HashMap<ExpressionData, Integer> trxIds;

    private ExpressionAssembler(Context ctx, NameMap<ExpressionData> expressions, Stats stats, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        this.ctx = ctx;
        this.expressions = expressions;
        this.trxIds = new LinkedHashMap<>();
        this.dependencyQueue = new LinkedList<>();
        this.stats = stats;
        this.buildCount = new HashSet<>();
        this.symbols = alphabet;
        this.badges = badges;
        this.parsers = parsers;
    }

    public Root build(Graph graph, ExpressionData expression) {
        var initial = graph.createNode();
        var accepted = compileExpression(graph, initial, expression);
        return new Root(initial, accepted);
    }

    @Override
    public NodeSet compileExpression(Graph graph, Node source, ExpressionData expression) {
        buildCount.add(expression);

        ctx.setTotal(0, buildCount.size());

        if (expression instanceof OptionalData) {
            return compileOptional(graph, source, (OptionalData)expression);
        }
        else if (expression instanceof RepetitionData) {
            return compileRepetition(graph, source, (RepetitionData)expression);
        }
        else if (expression instanceof SequenceData) {
            return compileSequence(graph, source, (SequenceData)expression);
        }
        else if (expression instanceof AlternationData) {
            return compileAlternation(graph, source, (AlternationData)expression);
        }
        else if (expression instanceof RangeData) {
            return compileRange(graph, source, (RangeData)expression);
        }
        else if (expression instanceof LiteralData) {
            return compileLiteral(graph, source, (LiteralData)expression);
        }
        else if (expression instanceof ReferenceData) {
            return compileReference(graph, source, (ReferenceData)expression);
        }
        else if (expression instanceof WildData) {
            return compileWild(graph, source, (WildData)expression);
        }
        else if (expression instanceof ValueData) {
            return compileValue(graph, source, (ValueData)expression);
        }
        else if (expression instanceof ArrayData) {
            return compileArray(graph, source, (ArrayData)expression);
        }
        else if (expression instanceof ObjectData) {
            return compileObject(graph, source, (ObjectData)expression);
        }
        else if (expression instanceof AttributeData) {
            return compileAttribute(graph, source, (AttributeData)expression);
        }
        else if (expression instanceof NameData) {
            return compileName(graph, source, (NameData)expression);
        }
        else {
            throw new UnsupportedValueException(expression);
        }
    }

    @Override
    public void registerDependency(String reference) {
        if (!dependencyQueue.contains(reference)) {
            dependencyQueue.add(reference);
        }
    }

    @Override
    public int nextTransactionID(ExpressionData expression) {
        return trxIds.computeIfAbsent(expression, k -> trxIds.size());
    }

    @Override
    public Badge getEmptyBadge() {
        return badges.empty();
    }

    @Override
    public Alphabet getAlphabet() {
        return symbols;
    }

    @Override
    public ValueParser findParser(String parser) {
        return parsers.findParser(parser);
    }

    @Override
    public ExpressionData findExpression(String name) {
        return expressions.find(name);
    }

    @Override
    public Set<String> getRecursiveReferences() {
        return stats.recursiveReferences;
    }
}
