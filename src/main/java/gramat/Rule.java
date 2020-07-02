package gramat;

import gramat.engine.Input;
import gramat.engine.deter.DRunner;
import gramat.engine.deter.DState;
import gramat.expressions.capturing.ValueRuntime;

public class Rule {

    private final DState root;

    public Rule(DState root) {
        this.root = root;
    }

    public Object eval(String input) throws Rejection {
        return eval(new Input(input));
    }

    public Object eval(Input input) throws Rejection {
        var runtime = new ValueRuntime(input);
        var runner = new DRunner(input, runtime);

        runtime.pushAssembler();

        var end = runner.eval(root);

        if(end.accepted) {
            var assembler = runtime.popAssembler();

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
