package gramat.models.expressions;

import java.util.List;

public class ModelWild implements ModelExpression {
    @Override
    public List<ModelExpression> getChildren() {
        return List.of();
    }
}
