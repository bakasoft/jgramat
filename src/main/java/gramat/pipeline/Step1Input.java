package gramat.pipeline;

import gramat.am.expression.AmExpression;
import gramat.framework.Component;
import gramat.util.NameMap;

public class Step1Input {

    public final Component parent;
    public final AmExpression main;
    public final NameMap<AmExpression> dependencies;

    public Step1Input(Component parent, AmExpression main, NameMap<AmExpression> dependencies) {
        this.parent = parent;
        this.main = main;
        this.dependencies = dependencies;
    }

}
