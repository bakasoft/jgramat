package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Property;

public class PropertyData extends ExpressionData {

    private String name;
    private PropertyMode mode;
    private ExpressionListData expression;

    public ExpressionListData getExpression() {
        return expression;
    }

    public void setExpression(ExpressionListData expression) {
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyMode getMode() {
        return mode;
    }

    public void setMode(PropertyMode mode) {
        this.mode = mode;
    }

    @Override
    public Element _settle(Grammar grammar) {
        return new Property(grammar, this);
    }
}
