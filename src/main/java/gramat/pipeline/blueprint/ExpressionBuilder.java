package gramat.pipeline.blueprint;

import gramat.badges.Badge;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.framework.Progress;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.Root;
import gramat.graph.plugs.Extension;
import gramat.models.expressions.*;
import gramat.parsers.ValueParser;
import gramat.pipeline.Template;
import gramat.pipeline.blueprint.builders.*;
import gramat.symbols.Alphabet;
import gramat.util.NameMap;
import gramat.graph.sets.NodeSet;

import java.util.*;

public class ExpressionBuilder extends DefaultComponent implements BaseBuilder,
        AlternationBuilder,
        LiteralBuilder,
        OptionalBuilder,
        RangeBuilder,
        ReferenceBuilder,
        RepetitionBuilder,
        SequenceBuilder,
        WildBuilder,
        SpecialBuilder {

    public static Template build(Component parent, Graph graph, ModelExpression main, NameMap<ModelExpression> expressions) {
        try (var progress = new Progress("Building Expression")) {

            progress.log("Analyzing expression...");

            var stats = Stats.compute(main, expressions);

            progress.log(0, stats.count, "Building main expression...");

            var builder = new ExpressionBuilder(parent, progress, expressions, stats);
            var root = builder.build(graph, main);
            var extensions = new NameMap<Extension>();

            for (var reference : stats.recursiveReferences) {
                progress.log("Building %s expression...", reference);

                var refExpr = expressions.find(reference);
                var refRoot = builder.build(graph, refExpr);
                var refExt = ExtensionMaker.make(graph, refRoot, extensions.size());

                extensions.set(reference, refExt);
            }

            return new Template(graph, root, extensions);
        }
    }

    private final Progress progress;
    private final NameMap<ModelExpression> expressions;
    private final Queue<String> dependencyQueue;
    private final Stats stats;

    private final Set<ModelExpression> buildCount;

    private final HashMap<ModelExpression, Integer> trxIds;

    private ExpressionBuilder(Component parent, Progress progress, NameMap<ModelExpression> expressions, Stats stats) {
        super(parent);
        this.progress = progress;
        this.expressions = expressions;
        this.trxIds = new HashMap<>();
        this.dependencyQueue = new LinkedList<>();
        this.stats = stats;
        this.buildCount = new HashSet<>();
    }

    public Root build(Graph graph, ModelExpression expression) {
        var initial = graph.createNode();
        var accepted = compileExpression(graph, initial, expression);
        return new Root(initial, accepted);
    }

    @Override
    public NodeSet compileExpression(Graph graph, Node source, ModelExpression expression) {
        buildCount.add(expression);

        progress.log(buildCount.size());

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
        return gramat.badges.empty();
    }

    @Override
    public Alphabet getAlphabet() {
        return gramat.symbols;
    }

    @Override
    public ValueParser findParser(String parser) {
        return gramat.parsers.findParser(parser);
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
