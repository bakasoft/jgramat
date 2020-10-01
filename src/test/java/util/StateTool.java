package util;

import gramat.Gramat;
import gramat.eval.Context;
import gramat.eval.State;
import gramat.input.Tape;

public class StateTool {

    public static void test(State state, String... inputs) {
        var gramat = new Gramat();
        var logger = gramat.getLogger();

        for (var input : inputs) {
            logger.debug("evaluating %s", input);

            state.evalValue(input, logger);
        }
    }
}
