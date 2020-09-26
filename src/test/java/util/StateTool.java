package util;

import gramat.Gramat;
import gramat.am.formatting.AmFormatter;
import gramat.eval.AmNodeWriter;
import gramat.eval.Context;
import gramat.eval.State;
import gramat.input.Tape;

public class StateTool {

    public static void test(State state, String... inputs) {
        var gramat = new Gramat();
        var logger = gramat.getLogger();

        AmNodeWriter.write(state, new AmFormatter(System.out));

        for (var input : inputs) {
            var tape = new Tape(input);
            var context = new Context(tape, gramat.getLogger());

            logger.debug("evaluating {}", input);

            state.eval(context);
        }
    }
}
