package gramat.scheme.data.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class AttributeData implements ExpressionData {

    public String name;

    public ExpressionData content;

    @Override
    public List<ExpressionData> getChildren() {
        return makeImmutableList(content);
    }
}
