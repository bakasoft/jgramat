package gramat.scheme.models.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelRange implements ModelExpression {

    public char begin;
    public char end;

    @Override
    public List<ModelExpression> getChildren() {
        return List.of();
    }

}
