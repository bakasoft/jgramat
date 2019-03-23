package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Repetition;

public class RepetitionData extends ExpressionData {

    private Integer minimum;
    private Integer maximum;
    private ExpressionListData expression;
    private ExpressionListData separator;

    public ExpressionListData getExpression() {
        return expression;
    }

    public void setExpression(ExpressionListData expression) {
        this.expression = expression;
    }

    public ExpressionListData getSeparator() {
        return separator;
    }

    public void setSeparator(ExpressionListData separator) {
        this.separator = separator;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    @Override
    public Element _settle(Grammar grammar) {
        // TODO optimize repetition with custom classes
        return new Repetition(grammar, this);
    }
}
