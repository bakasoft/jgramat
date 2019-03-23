package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.ValueElement;

public class ResolvedValueData extends ExpressionData {

    private Class<?> type;

    private ExpressionListData expression;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ExpressionListData getExpression() {
        return expression;
    }

    public void setExpression(ExpressionListData expression) {
        this.expression = expression;
    }

    @Override
    public Element _settle(Grammar grammar) {
        return new ValueElement(grammar, this);
    }

}
