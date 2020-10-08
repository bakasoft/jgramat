package gramat.pipeline;

import gramat.eval.Evaluator;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Component;
import gramat.input.Tape;
import gramat.models.expressions.ModelExpression;
import gramat.models.test.ModelEvalPass;
import gramat.models.test.ModelTest;
import gramat.util.NameMap;

import java.util.List;

public class Source {

    public final NameMap<ModelExpression> rules;

    public final List<ModelTest> tests;

    public Source(NameMap<ModelExpression> rules, List<ModelTest> tests) {
        this.rules = rules;
        this.tests = tests;
    }

    public int runTests(Component parent) {
        var count = 0;
        for (var test : tests) {
            if (test instanceof ModelEvalPass) {
                var logger = parent.getLogger();
                var pass = (ModelEvalPass)test;
                var state = Pipeline.toState(parent, new Sentence(pass.expression, rules));
                var tape = new Tape(pass.input);
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
