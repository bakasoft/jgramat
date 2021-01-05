package gramat.scheme.data.expressions;

import gramat.util.DataUtils;

import java.util.List;

public class RepetitionData implements ExpressionData {

    public ExpressionData content;
    public ExpressionData separator;
    public int minimum;

    @Override
    public List<ExpressionData> getChildren() {
        return DataUtils.makeImmutableList(content, separator);
    }
}
