package gramat.scheme.data.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ValueData implements ExpressionData {

    public String parser;

    public ExpressionData content;

    @Override
    public List<ExpressionData> getChildren() {
        return makeImmutableList(content);
    }

}
