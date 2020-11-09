package gramat.pipeline.assembling;

import gramat.badges.Badge;
import gramat.badges.BadgeSource;
import gramat.exceptions.UnsupportedValueException;
import gramat.models.expressions.*;
import gramat.pipeline.compiling.ExtensionMaker;
import gramat.pipeline.compiling.Stats;
import gramat.pipeline.compiling.Template;
import gramat.models.expressions.*;
import gramat.framework.Context;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.Root;
import gramat.graph.plugs.Extension;
import gramat.parsers.ParserSource;
import gramat.parsers.ValueParser;
import gramat.symbols.Alphabet;
import gramat.util.NameMap;
import gramat.graph.sets.NodeSet;

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

    public static Template build(Context ctx, Graph graph, ModelExpression main, NameMap<ModelExpression> expressions, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
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
    private final NameMap<ModelExpression> expressions;
    private final Queue<String> dependencyQueue;
    private final Stats stats;
    private final Alphabet symbols;
    private final BadgeSource badges;
    private final ParserSource parsers;

    private final Set<ModelExpression> buildCount;

    private final HashMap<ModelExpression, Integer> trxIds;

    private ExpressionAssembler(Context ctx, NameMap<ModelExpression> expressions, Stats stats, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
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

    public Root build(Graph graph, ModelExpression expression) {
        var initial = graph.createNode();
        var accepted = compileExpression(graph, initial, expression);
        return new Root(initial, accepted);
    }

    @Override
    public NodeSet compileExpression(Graph graph, Node source, ModelExpression expression) {
        buildCount.add(expression);

        ctx.setTotal(0, buildCount.size());

        if (expression instanceof ModelOptional) {
            return compileOptional(graph, source, (ModelOptional)expression);
        }
        else if (expression instanceof ModelRepetition) {
            return compileRepetition(graph, source, (ModelRepetition)expression);
        }
        else if (expression instanceof ModelSequence) {
            return compileSequence(graph, source, (ModelSequence)expression);
        }
        else if (expression instanceof ModelAlternation) {
            return compileAlternation(graph, source, (ModelAlternation)expression);
        }
        else if (expression instanceof ModelRange) {
            return compileRange(graph, source, (ModelRange)expression);
        }
        else if (expression instanceof ModelLiteral) {
            return compileLiteral(graph, source, (ModelLiteral)expression);
        }
        else if (expression instanceof ModelReference) {
            return compileReference(graph, source, (ModelReference)expression);
        }
        else if (expression instanceof ModelWild) {
            return compileWild(graph, source, (ModelWild)expression);
        }
        else if (expression instanceof ModelValue) {
            return compileValue(graph, source, (ModelValue)expression);
        }
        else if (expression instanceof ModelArray) {
            return compileArray(graph, source, (ModelArray)expression);
        }
        else if (expression instanceof ModelObject) {
            return compileObject(graph, source, (ModelObject)expression);
        }
        else if (expression instanceof ModelAttribute) {
            return compileAttribute(graph, source, (ModelAttribute)expression);
        }
        else if (expression instanceof ModelName) {
            return compileName(graph, source, (ModelName)expression);
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
    public int nextTransactionID(ModelExpression expression) {
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
    public ModelExpression findExpression(String name) {
        return expressions.find(name);
    }

    @Override
    public Set<String> getRecursiveReferences() {
        return stats.recursiveReferences;
    }
}
