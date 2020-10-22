package gramat.models.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ModelLiteral implements ModelExpression {

    public String value;

    @Override
    public List<ModelExpression> getChildren() {
        return List.of();
    }

}
