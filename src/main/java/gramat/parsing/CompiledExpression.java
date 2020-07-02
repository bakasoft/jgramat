package gramat.parsing;

import gramat.epsilon.Input;
import gramat.epsilon.Machine;
import gramat.eval.Evaluator;
import gramat.eval.RejectedError;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Source;

import java.util.HashMap;

public class CompiledExpression {

    private final Machine machine;

    public CompiledExpression(Machine machine) {
        this.machine = machine;
    }

    public Object eval(String input) throws RejectedError {
        return eval(Input.of(input));
    }

    public Object eval(Input input) throws RejectedError {
        var context = new EvalContext(input, new HashMap<>());
        var evaluator = new Evaluator(context);

        evaluator.pushAssembler();

        var end = evaluator.eval(root);

        if(end.accepted) {
            var assembler = evaluator.popAssembler();

            if (assembler.isEmpty()) {
                return null;
            }

            var result = assembler.popValue();

            assembler.expectEmpty();

            return result;
        }
        else {
            throw new RejectedError(end, source.getLocation());
        }
    }

}
