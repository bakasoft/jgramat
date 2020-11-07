package gramat.pipeline;

import gramat.badges.BadgeSource;
import gramat.eval.Evaluator;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Context;
import gramat.input.Tape;
import gramat.machine.State;
import gramat.models.expressions.ModelExpression;
import gramat.models.formatters.ExpressionFormatter;
import gramat.models.test.ModelEvalPass;
import gramat.models.test.ModelTest;
import gramat.parsers.ParserSource;
import gramat.symbols.Alphabet;
import gramat.util.NameMap;

import java.util.LinkedHashMap;
import java.util.List;

public class Source {

    public final NameMap<ModelExpression> rules;

    public final List<ModelTest> tests;

    public final ModelExpression main;

    public Source(NameMap<ModelExpression> rules, List<ModelTest> tests, ModelExpression main) {
        this.rules = rules;
        this.tests = tests;
        this.main = main;
    }

    public int runTests(Context ctx, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var cache = new LinkedHashMap<String, State>();
        var count = 0;
        for (var test : tests) {
            if (test instanceof ModelEvalPass) {
                var pass = (ModelEvalPass)test;
                var expressionStr = ExpressionFormatter.format(pass.expression);
                var state = cache.computeIfAbsent(expressionStr, k ->
                    Pipeline.toState(ctx, new Sentence(pass.expression, rules), alphabet, badges, parsers)
                );
                var tape = new Tape(pass.input);

                ctx.debug("evaluating: %s", pass.input);

                var evaluator = new Evaluator(ctx, tape, badges);
                var result = evaluator.evalValue(state);

                ctx.debug("result: %s", result);
                count++;
            }
            else {
                throw new UnsupportedValueException(test);
            }
        }
        return count;
    }

}
