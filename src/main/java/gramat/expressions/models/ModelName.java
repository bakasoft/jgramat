package gramat.expressions.models;

import gramat.util.DataUtils;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelName implements ModelExpression {

    public ModelExpression content;

    @Override
    public List<ModelExpression> getChildren() {
        return DataUtils.makeImmutableList(content);
    }
}
