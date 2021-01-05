package gramat.scheme.data.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class LiteralData implements ExpressionData {

    public String value;

    @Override
    public List<ExpressionData> getChildren() {
        return List.of();
    }

}
