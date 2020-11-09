package gramat.pipeline.compiling;

import gramat.models.expressions.ModelExpression;
import gramat.util.NameMap;

public class Sentence {

    public final ModelExpression expression;
    public final NameMap<ModelExpression> dependencies;

    public Sentence(ModelExpression expression, NameMap<ModelExpression> dependencies) {
        this.expression = expression;
        this.dependencies = dependencies;
    }

}
