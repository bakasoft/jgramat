package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;

public class GroupData extends ExpressionData {
    private ExpressionListData expression;

    public ExpressionListData getExpression() {
        return expression;
    }

    public void setExpression(ExpressionListData expression) {
        this.expression = expression;
    }

    @Override
    public Element _settle(Grammar grammar) {
        Element element = grammar.settle(getExpression());

        grammar.addElement(this, element);

        return element;
    }
}
