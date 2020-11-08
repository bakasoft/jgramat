package gramat.expressions.models;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelAlternation implements ModelExpression {

    public List<ModelExpression> options;

    @Override
    public List<ModelExpression> getChildren() {
        return makeImmutableList(options);
    }
}
