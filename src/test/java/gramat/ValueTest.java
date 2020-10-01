package gramat;

import gramat.compiling.StateCompiler;
import gramat.source.ExpressionMap;
import gramat.source.ExpressionFactory;
import org.junit.Test;
import util.StateTool;

public class ValueTest {

    @Test
    public void simpleRecursive1() {
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

        grammar.addExpression("string", factory.literal("x"));

        grammar.addExpression(
                "object",
                factory.sequence(
                        factory.literal("("),
                        factory.reference("value"),
                        factory.literal(")")
                )
        );

        grammar.addExpression("root", factory.sequence(
                factory.literal("\u0002"),
                factory.reference("value"),
                factory.literal("\u0003")
        ));

        var state = StateCompiler.compileExpression(gramat, "root", grammar);

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

        var state = StateCompiler.compileExpression(gramat, "root", grammar);

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
        var grammar = new ExpressionMap(gramat);

        grammar.addExpression(
                "a",
                factory.value(
                        factory.repetition(
                                factory.literal("a")
                        )
                )
        );

        var state = StateCompiler.compileExpression(gramat, "a", grammar);

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
