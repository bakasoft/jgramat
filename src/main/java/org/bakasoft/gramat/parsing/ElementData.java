package org.bakasoft.gramat.parsing;

abstract public class ElementData extends ExpressionData {
    protected String name;

    protected ExpressionListData expression;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpressionListData getExpression() {
        return expression;
    }

    public void setExpression(ExpressionListData expression) {
        this.expression = expression;
    }
}
