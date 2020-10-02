package gramat;

import gramat.compiling.ExpressionCompiler;
import gramat.formatting.NodeFormatter;
import gramat.expressions.ExpressionFactory;
import gramat.expressions.ExpressionMap;
import org.junit.Test;

public class ExpressionCompilerTest {

    @Test
    public void test1() {
        var gramat = new Gramat();
        var exf = new ExpressionFactory();
        var compiler = new ExpressionCompiler(gramat);

        var graph1 = compiler.compile(exf.value(exf.literal("a")));

        new NodeFormatter(System.out).write(graph1);
    }

    @Test
    public void test2() {
        var gramat = new Gramat();
        var factory = new ExpressionFactory();
        var grammar = new ExpressionMap(gramat);

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

        var compiler = new ExpressionCompiler(gramat);

        for (var name : grammar.getExpressionNames()) {
            var expr = grammar.findExpression(name);
            var graph1 = compiler.compile(expr);

            new NodeFormatter(System.out).write(graph1);
        }

    }
}
