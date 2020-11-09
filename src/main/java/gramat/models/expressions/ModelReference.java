package gramat.models.expressions;

import java.util.List;

public class ModelReference implements ModelExpression {

    public String name;

    @Override
    public List<ModelExpression> getChildren() {
        return List.of();
    }
}
