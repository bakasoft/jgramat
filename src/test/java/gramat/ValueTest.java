package gramat;

import gramat.am.expression.AmExpression;
import gramat.pipeline.Pipeline;
import gramat.pipeline.StateCompiler;
import gramat.am.ExpressionFactory;
import gramat.util.NameMap;
import org.junit.Test;
import util.StateTool;

public class ValueTest {

    @Test
    public void simpleRecursive1() {
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

        grammar.set("string", factory.literal("x"));

        grammar.set(
                "object",
                factory.sequence(
                        factory.literal("("),
                        factory.reference("value"),
                        factory.literal(")")
                )
        );

        grammar.set("root", factory.sequence(
                factory.literal("\u0002"),
                factory.reference("value"),
                factory.literal("\u0003")
        ));

        var state = Pipeline.compile(gramat, "root", grammar);

        StateTool.test(
                state,
                "x",
                "(x)",
                "((x))",
                "(((x)))",
                "((((x))))"
        );
    }

    @Test
    public void simpleRecursive2() {
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

        var state = Pipeline.compile(gramat, "root", grammar);

        StateTool.test(
                state,
                "a",
                "{a:b}",
                "{a:{}}",
                "{a:b,c:d}",
                "{a:{},b:{}}",
                "{a:{b:c},d:{e:f}}"
        );
    }

    @Test
    public void simpleRepetition() {
        var gramat = new Gramat();
        var factory = new ExpressionFactory();
        var grammar = new NameMap<AmExpression>();

        grammar.set(
                "a",
                factory.value(
                        factory.repetition(
                                factory.literal("a")
                        )
                )
        );

        var state = Pipeline.compile(gramat, "a", grammar);

        StateTool.test(
                state,
                "",
                "a",
                "aa",
                "aaa",
                "aaaa"
        );
    }

}
