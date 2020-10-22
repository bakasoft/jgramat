package gramat.pipeline.blueprint;

import gramat.badges.Badge;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.Root;
import gramat.graph.plugs.Extension;
import gramat.models.expressions.*;
import gramat.parsers.ValueParser;
import gramat.pipeline.Template;
import gramat.pipeline.blueprint.builders.*;
import gramat.symbols.Alphabet;
import gramat.util.Chain;
import gramat.util.Count;
import gramat.util.NameMap;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
        var recursiveReferences = RecursiveReferences.compute(main, expressions);
        var builder = new ExpressionBuilder(parent, graph, main, expressions, recursiveReferences);
        var root = builder.build(graph, main);
        var extensions = new NameMap<Extension>();

        for (var reference : recursiveReferences) {
            var refExpr = expressions.find(reference);
            var refRoot = builder.build(graph, refExpr);
            var refExt = ExtensionMaker.make(graph, refRoot, extensions.size());

            extensions.set(reference, refExt);
        }

        return new Template(graph, root, extensions);
    }

    private final NameMap<ModelExpression> expressions;
    private final Count trxIds;
    private final Queue<String> dependencyQueue;
    private final Set<String> recursiveReferences;

    private ExpressionBuilder(Component parent, Graph graph, ModelExpression main, NameMap<ModelExpression> expressions, Set<String> recursiveReferences) {
        super(parent);
        this.expressions = expressions;
        this.trxIds = new Count();
        this.dependencyQueue = new LinkedList<>();
        this.recursiveReferences = recursiveReferences;
    }

    public Root build(Graph graph, ModelExpression expression) {
        var initial = graph.createNode();
        var accepted = compileExpression(graph, initial, graph.createNode(), expression);
        return new Root(initial, accepted);
    }

    @Override
    public Chain<Node> compileExpression(Graph graph, Node source, Node target, ModelExpression expression) {
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

    @Override
    public void registerDependency(String reference) {
        if (!dependencyQueue.contains(reference)) {
            dependencyQueue.add(reference);
        }
    }

    @Override
    public int nextTransactionID() {
        return trxIds.next();
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
        return recursiveReferences;
    }
}
