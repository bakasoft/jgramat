package gramat.parsing;

import gramat.automata.dfa.DState;
import gramat.eval.Evaluator;
import gramat.eval.RejectedError;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Source;

import java.util.HashMap;

public class CompiledExpression {

    private final DState root;

    public CompiledExpression(DState root) {
        this.root = root;
    }

    public Object eval(String input) throws RejectedError {
        return eval(Source.of(input));
    }

    public Object eval(Source source) throws RejectedError {
        var context = new EvalContext(source, new HashMap<>());
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
