package org.bakasoft.gramat.parsing.elements.transforms;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Transformation;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

import java.util.Map;

public class GReplaceMap extends GTransformation {

    public final Map<String, String> replacements;

    public GReplaceMap(LocationRange location, Map<String, String> replacements, GExpression expression) {
        super(location, expression);
        this.replacements = replacements;
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GReplaceMap(location, replacements, expression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Transformation(expression.compile(compiled), input -> {
            String output = replacements.get(input);

            if (output != null) {
                return output;
            }

            return replacements.get(null);
        });
    }

}
