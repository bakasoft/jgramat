package gramat;

import gramat.eval.StateCompiler;
import gramat.source.ExpressionFactory;
import gramat.source.ExpressionGrammar;
import org.junit.Test;
import util.StateTool;

public class JsonTest {

    public void init(ExpressionGrammar source) {
        var factory = new ExpressionFactory();

        source.addExpression(
                "value",
                factory.sequence(
                        factory.reference("whitespace"),
                        factory.alternation(
                                factory.reference("string"),
                                factory.reference("object")
                        ),
                        factory.reference("whitespace")
                )
        );
        source.addExpression(
                "whitespace",
                factory.repetition(
                        factory.alternation(
                                factory.literal(" "),
                                factory.literal("\t"),
                                factory.literal("\r"),
                                factory.literal("\n")
                        )
                )
        );
        source.addExpression(
                "object",
                factory.object(
                        factory.sequence(
                                factory.literal("{"),
                                factory.reference("whitespace"),
                                factory.repetition(
                                        // repeat:
                                        factory.attribute(
                                                factory.sequence(
                                                        factory.name(
                                                                factory.reference("string")
                                                        ),
                                                        factory.reference("whitespace"),
                                                        factory.literal(":"),
                                                        factory.reference("value")
                                                )
                                        ),
                                        // separator:
                                        factory.sequence(
                                                factory.literal(","),
                                                factory.reference("whitespace")
                                        )
                                ),
                                factory.literal("}")
                        )
                )
        );
        source.addExpression(
                "string",
                factory.sequence(
                        factory.literal("\""),
                        factory.value(
                                factory.repetition(
                                        factory.alternation(
                                                factory.sequence(
                                                        factory.literal("\\"),
                                                        factory.alternation(
                                                                factory.literal("\""),
                                                                factory.literal("\\"),
                                                                factory.literal("/"),
                                                                factory.literal("b"),
                                                                factory.literal("f"),
                                                                factory.literal("n"),
                                                                factory.literal("r"),
                                                                factory.literal("t")
                                                                // TODO unicode escaping
                                                        )
                                                ),
                                                factory.wild()
                                        )
                                )
                        ),
                        factory.literal("\"")
                )
        );
    }

    @Test
    public void test() {
        var gramat = new Gramat();
        var grammar = new ExpressionGrammar(gramat);

        init(grammar);

        var state = StateCompiler.compileExpression(gramat, "value", grammar);

        StateTool.test(state, "\"\"");
        StateTool.test(state, "\"a\"");
        StateTool.test(state, "\"abc\"");
        StateTool.test(state, "{}");
        StateTool.test(state, "{\"a\":\"b\"}");
        StateTool.test(state, "{\"a\":\"b\",\"c\":\"d\"}");
        StateTool.test(state, "{\"a\":{}}");
        StateTool.test(state, "{\"a\":{},\"b\":{}}");
        StateTool.test(state, "{\"a\":{\"b\":\"c\"},\"d\":{\"e\":\"f\"}}");
        StateTool.test(state, " { \"a\" : { \"b\" : \"c\" } , \"d\" : { \"e\" : \"f\" } } ");
    }

}
