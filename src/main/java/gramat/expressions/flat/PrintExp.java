package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

public class PrintExp extends Expression {

    private final String message;

    public PrintExp(Location location, String message) {
        super(location);
        this.message = message;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        System.out.println(message + " <- " + context.source.getLocation());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Expression optimize() {
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        return this;
    }

    @Override
    public String getDescription() {
        return "Print message";
    }

}
