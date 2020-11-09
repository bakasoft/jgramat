package gramat.models.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelObject implements ModelExpression {

    public String type;

    public ModelExpression content;

    @Override
    public List<ModelExpression> getChildren() {
        return makeImmutableList(content);
    }

}
