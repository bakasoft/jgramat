package gramat;

import gramat.engine.Input;
import gramat.engine.deter.DRunner;
import gramat.engine.deter.DState;
import gramat.engine.actions.capturing.CapturingContext;

public class Parser {

    private final DState initial;

    public Parser(DState initial) {
        this.initial = initial;
    }

    public Object eval(String input) throws Rejection {
        return eval(new Input(input));
    }

    public Object eval(Input input) throws Rejection {
        var runner = new DRunner(input);

        runner.capturingContext.pushAssembler();

        var end = runner.eval(initial);

        if(end.accepted) {
            var assembler = runner.capturingContext.popAssembler();

            if (assembler.isEmpty()) {
                return null;
            }

            var result = assembler.popValue();

            assembler.expectEmpty();

            return result;
        }
        else {
            throw new Rejection(end, input.getLocation());
        }
    }

}
