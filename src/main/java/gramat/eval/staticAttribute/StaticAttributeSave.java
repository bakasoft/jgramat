package gramat.eval.staticAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;

public class StaticAttributeSave extends Action {

    private final String name;

    public StaticAttributeSave(Action beginAction, String name) {
        this.name = name;
    }

    @Override
    public void run(Evaluator evaluator) {
        var assembler = evaluator.popAssembler();

        var value = assembler.popValue();

        assembler.expectEmpty();

        evaluator.peekAssembler().setAttribute(name, value);
    }

    @Override
    public String getDescription() {
        return "COMMIT-ATTR:" + name;
    }
}
