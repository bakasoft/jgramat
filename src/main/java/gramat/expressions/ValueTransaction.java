package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ValueTransaction extends Expression {

    private Expression expression;

    public ValueTransaction(Location location, Expression expression) {
        super(location);
        this.expression = expression;
    }

    @Override
    public boolean eval(EvalContext context) {
        context.begin(this);

        if (expression.eval(context)) {
            context.commit(this);
            return true;
        }
        else {
            context.rollback(this);
            return false;
        }
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return this;
    }

    @Override
    public String getDescription() {
        return "Value-Transaction";
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "value-transation")) {
            writer.write(expression);
            writer.close();
        }
    }

}
