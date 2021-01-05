package gramat.scheme.data.expressions;

import java.util.List;

public class WildData implements ExpressionData {
    @Override
    public List<ExpressionData> getChildren() {
        return List.of();
    }
}
