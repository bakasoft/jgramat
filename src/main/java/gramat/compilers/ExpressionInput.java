package gramat.compilers;

import gramat.models.expressions.ModelExpression;
import gramat.framework.Component;
import gramat.util.NameMap;

public class ExpressionInput {

    public final Component parent;
    public final ModelExpression main;
    public final NameMap<ModelExpression> dependencies;

    public ExpressionInput(Component parent, ModelExpression main, NameMap<ModelExpression> dependencies) {
        this.parent = parent;
        this.main = main;
        this.dependencies = dependencies;
    }

}
