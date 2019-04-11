package org.bakasoft.gramat.parsing.elements.transforms;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Transformation;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

import java.util.Map;
import java.util.function.Function;

public class GTransform extends GTransformation {

    public final String name;

    public GTransform(LocationRange location, String name, GExpression expression) {
        super(location, expression);
        this.name = name;
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GTransform(location, name, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        if (name == null) {
            throw new GrammarException("Missing transformation name.", location);
        }

        Function<String, String> transformation = gramat.getTransformation(name);

        if (transformation == null) {
            throw new GrammarException("Not implemented transformation: " + name, location);
        }

        return new Transformation(expression.compile(compiled), transformation);
    }

}
