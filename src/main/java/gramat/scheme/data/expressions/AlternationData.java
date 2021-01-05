package gramat.scheme.data.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class AlternationData implements ExpressionData {

    public List<ExpressionData> options;

    @Override
    public List<ExpressionData> getChildren() {
        return makeImmutableList(options);
    }
}
