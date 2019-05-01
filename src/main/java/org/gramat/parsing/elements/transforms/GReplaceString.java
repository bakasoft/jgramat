package org.gramat.parsing.elements.transforms;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Transformation;
import org.gramat.parsing.GExpression;

import java.util.Map;

public class GReplaceString extends GTransformation {

    public final String replacement;

    public GReplaceString(LocationRange location, String replacement, GExpression expression) {
        super(location, expression);
        this.replacement = replacement;
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GReplaceString(location, replacement, expression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Transformation(expression.compile(compiled), input -> replacement);
    }

}
