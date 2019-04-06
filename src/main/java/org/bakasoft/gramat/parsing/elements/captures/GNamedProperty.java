package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Property;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.elements.GElement;

import java.util.Map;
import java.util.Objects;

public class GNamedProperty extends GCapture {

    public final String propertyName;
    public final boolean appendMode;
    public final GElement expression;

    public GNamedProperty(String propertyName, boolean appendMode, GElement expression) {
        this.propertyName = propertyName;
        this.appendMode = appendMode;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public GElement simplify() {
        GElement simplified = expression.simplify();

        if (simplified == null) {
            return null;
        }

        return new GNamedProperty(propertyName, appendMode, simplified);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Property(
                propertyName,
                appendMode,
                expression.compile(gramat, compiled));
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        // property conditions are not plain by definition
        return false;
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return expression.isOptional(gramat);
    }

    public static GNamedProperty expectProperty(Tape tape) {
        expectSymbol(tape, '<');

        skipVoid(tape);

        String propertyName = expectName(tape, "property name");

        skipVoid(tape);

        boolean appendMode;

        if (trySymbols(tape, "+:")) {
            appendMode = true;
        }
        else if (trySymbol(tape, ':')) {
            appendMode = false;
        }
        else {
            throw new GrammarException("Expected property mode: " + inspect("+:") + " or " + inspect(':'), tape.getLocation());
        }

        skipVoid(tape);

        GElement expression = expectExpression(tape);

        skipVoid(tape);

        expectSymbol(tape, '>');

        return new GNamedProperty(propertyName, appendMode, expression);
    }
}
