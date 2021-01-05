package gramat.scheme.data.expressions;

import java.util.List;

public class ReferenceData implements ExpressionData {

    public String name;

    @Override
    public List<ExpressionData> getChildren() {
        return List.of();
    }
}
