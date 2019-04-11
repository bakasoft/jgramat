package org.bakasoft.gramat.parsing.elements.transforms;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Transformation;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

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
