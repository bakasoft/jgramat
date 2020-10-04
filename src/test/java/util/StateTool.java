package util;

import gramat.Gramat;
import gramat.eval.Evaluator;
import gramat.input.Tape;
import gramat.machine.State;

public class StateTool {

    public static void test(State state, String... inputs) {
        var gramat = new Gramat();
        var logger = gramat.getLogger();

        for (var input : inputs) {
            var evalutaor = new Evaluator(gramat, new Tape(input), logger);

            logger.debug("evaluating %s", input);

            var result = evalutaor.evalValue(state);

            logger.debug("result: %s", result);
        }
    }
}
