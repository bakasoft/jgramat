package gramat.scheme.data.expressions;

import gramat.util.DataUtils;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class NameData implements ExpressionData {

    public ExpressionData content;

    @Override
    public List<ExpressionData> getChildren() {
        return DataUtils.makeImmutableList(content);
    }
}
