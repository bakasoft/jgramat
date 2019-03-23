package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Optional;

public class OptionalData extends ExpressionData {
    private ExpressionListData expression;

    public ExpressionListData getExpression() {
        return expression;
    }

    public void setExpression(ExpressionListData expression) {
        this.expression = expression;
    }

    @Override
    public Element _settle(Grammar grammar) {
        return new Optional(grammar, this);
    }
}
