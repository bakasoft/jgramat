package gramat.scheme.data.expressions;

import gramat.util.DataUtils;

import java.util.List;

import static gramat.util.DataUtils.makeImmutableList;

public class SequenceData implements ExpressionData {

    public List<ExpressionData> items;

    @Override
    public List<ExpressionData> getChildren() {
        return DataUtils.makeImmutableList(items);
    }

}
