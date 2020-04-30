package gramat.expressions.flat;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;

public class PrintExp extends Expression {

    private final String message;

    public PrintExp(Location location, String message) {
        super(location);
        this.message = message;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return List.of();
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return this;
    }

    @Override
    public boolean eval(EvalContext context) {
        System.out.println(message + " <- " + context.source.getLocation());
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Print message";
    }

}
