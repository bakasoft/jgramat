package gramat.pipeline;

import gramat.actions.*;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.graph.Node;
import gramat.graph.Root;
import gramat.models.expressions.*;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.Graph;
import gramat.parsers.ValueParser;
import gramat.pipeline.expressions.ExpressionFactory;
import gramat.symbols.Alphabet;
import gramat.symbols.Symbol;
import gramat.util.Chain;
import gramat.util.Count;
import gramat.util.NameMap;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BlueprintCompiler extends DefaultComponent implements ExpressionFactory {

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
        return null;
    }
}
