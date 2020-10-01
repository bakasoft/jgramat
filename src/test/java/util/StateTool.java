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
            var tape = new Tape(input);
            var context = new Context(tape, gramat.getLogger());

            logger.debug("evaluating {}", input);

            state.eval(context);
        }
    }
}
