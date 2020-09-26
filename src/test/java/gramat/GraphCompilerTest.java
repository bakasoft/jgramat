package gramat;

import gramat.eval.StateCompiler;
import gramat.framework.Component;
import gramat.proto.GraphCompiler;
import gramat.proto.GraphFormatter;
import gramat.proto.GraphGrammar;
import gramat.source.ExpressionFactory;
import gramat.source.ExpressionGrammar;
import org.junit.Test;
import util.StateTool;

public class GraphCompilerTest {

    @Test
    public void test1() {
        var gramat = new Gramat();
        var exprs = new ExpressionGrammar(gramat);
        var exf = new ExpressionFactory();
        var graphs = new GraphGrammar();
        var compiler = new GraphCompiler(gramat, exprs, graphs);

        exprs.addExpression("expr1",
                        exf.value(exf.literal("a"))
        );

        var graph1 = compiler.compile("expr1");

        new GraphFormatter(System.out).write(graph1);
    }

    @Test
    public void test2() {
        var gramat = new Gramat();
        var factory = new ExpressionFactory();
        var grammar = new ExpressionGrammar(gramat);

        grammar.addExpression(
                "value",
                factory.alternation(
                        factory.reference("string"),
                        factory.reference("object")
                )
        );

        grammar.addExpression(
                "string",
                factory.value(
                        factory.range('a', 'z')
                )
        );

        grammar.addExpression(
                "object-impl",
                factory.object(
                        factory.sequence(
                                factory.literal("{"),
                                factory.repetition(
                                        factory.attribute(
                                                factory.sequence(
                                                        factory.name(
                                                                factory.reference("string")
                                                        ),
                                                        factory.literal(":"),
                                                        factory.reference("value")
                                                )
                                        ),
                                        factory.literal(",")
                                ),
                                factory.literal("}")
                        )
                )
        );

        // Test recursive reference of reference
        grammar.addExpression("object", factory.reference("object-impl"));

        grammar.addExpression("root", factory.sequence(
                factory.literal("\u0002"),
                factory.reference("value"),
                factory.literal("\u0003")
        ));

        var graphs = new GraphGrammar();
        var compiler = new GraphCompiler(gramat, grammar, graphs);

        for (var name : grammar.getExpressionNames()) {
            var graph1 = compiler.compile(name);

            new GraphFormatter(System.out).write(graph1);
        }

    }
}
