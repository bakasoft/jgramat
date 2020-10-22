package gramat.models.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelName implements ModelExpression {

    public ModelExpression content;

    @Override
    public List<ModelExpression> getChildren() {
        return makeImmutableList(content);
    }
}
