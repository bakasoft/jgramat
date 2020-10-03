package gramat;

import gramat.am.expression.AmExpression;
import gramat.pipeline.Step1Compiler;
import gramat.formatting.NodeFormatter;
import gramat.am.ExpressionFactory;
import gramat.util.NameMap;
import org.junit.Test;

public class ExpressionCompilerTest {

    @Test
    public void test1() {
        var gramat = new Gramat();
        var exf = new ExpressionFactory();
        var compiler = new Step1Compiler(gramat);

        var graph1 = compiler.compile(exf.value(exf.literal("a")));

        new NodeFormatter(System.out).write(graph1);
    }

    @Test
    public void test2() {
        var gramat = new Gramat();
        var factory = new ExpressionFactory();
        var grammar = new NameMap<AmExpression>();

        grammar.set(
                "value",
                factory.alternation(
                        factory.reference("string"),
                        factory.reference("object")
                )
        );

        grammar.set(
                "string",
                factory.value(
                        factory.range('a', 'z')
                )
        );

        grammar.set(
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
        grammar.set("object", factory.reference("object-impl"));

        grammar.set("root", factory.sequence(
                factory.literal("\u0002"),
                factory.reference("value"),
                factory.literal("\u0003")
        ));

        var compiler = new Step1Compiler(gramat);

        for (var name : grammar.keySet()) {
            var expr = grammar.find(name);
            var graph1 = compiler.compile(expr);

            new NodeFormatter(System.out).write(graph1);
        }

    }
}
