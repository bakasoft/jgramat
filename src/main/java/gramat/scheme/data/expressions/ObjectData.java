package gramat.scheme.data.expressions;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class ObjectData implements ExpressionData {

    public String type;

    public ExpressionData content;

    @Override
    public List<ExpressionData> getChildren() {
        return makeImmutableList(content);
    }

}
