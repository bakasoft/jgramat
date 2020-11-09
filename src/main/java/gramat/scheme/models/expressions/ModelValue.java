package gramat.scheme.models.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelValue implements ModelExpression {

    public String parser;

    public ModelExpression content;

    @Override
    public List<ModelExpression> getChildren() {
        return makeImmutableList(content);
    }

}
