package gramat.pipeline;

import gramat.eval.Evaluator;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Component;
import gramat.input.Tape;
import gramat.machine.State;
import gramat.models.expressions.ModelExpression;
import gramat.models.formatters.ExpressionFormatter;
import gramat.models.test.ModelEvalPass;
import gramat.models.test.ModelTest;
import gramat.util.NameMap;

import java.util.HashMap;
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

    public int runTests(Component parent) {
        var cache = new LinkedHashMap<String, State>();
        var count = 0;
        for (var test : tests) {
            if (test instanceof ModelEvalPass) {
                var logger = parent.getLogger();
                var pass = (ModelEvalPass)test;
                var expressionStr = ExpressionFormatter.format(pass.expression);
                var state = cache.computeIfAbsent(expressionStr, k ->
                    Pipeline.toState(parent, new Sentence(pass.expression, rules))
                );
                var tape = new Tape(pass.input);

                logger.debug("evaluating: %s", pass.input);

                var evaluator = new Evaluator(parent, tape, logger);
                var result = evaluator.evalValue(state);

                logger.debug("result: %s", result);
                count++;
            }
            else {
                throw new UnsupportedValueException(test);
            }
        }
        return count;
    }

}
