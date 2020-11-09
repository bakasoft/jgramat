package gramat.models.expressions;

import gramat.util.DataUtils;

import java.util.List;

public class ModelRepetition implements ModelExpression {

    public ModelExpression content;
    public ModelExpression separator;
    public int minimum;

    @Override
    public List<ModelExpression> getChildren() {
        return DataUtils.makeImmutableList(content, separator);
    }
}
