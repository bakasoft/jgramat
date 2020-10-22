package gramat.models.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelSequence implements ModelExpression {

    public List<ModelExpression> items;

    @Override
    public List<ModelExpression> getChildren() {
        return makeImmutableList(items);
    }

}
