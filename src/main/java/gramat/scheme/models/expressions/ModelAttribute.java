package gramat.scheme.models.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelAttribute implements ModelExpression {

    public String name;

    public ModelExpression content;

    @Override
    public List<ModelExpression> getChildren() {
        return makeImmutableList(content);
    }
}
