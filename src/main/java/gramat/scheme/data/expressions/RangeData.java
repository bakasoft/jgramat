package gramat.scheme.data.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class RangeData implements ExpressionData {

    public char begin;
    public char end;

    @Override
    public List<ExpressionData> getChildren() {
        return List.of();
    }

}
