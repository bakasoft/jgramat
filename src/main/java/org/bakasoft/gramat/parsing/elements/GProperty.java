package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Property;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.Gramat;

import java.util.Map;
import java.util.Objects;

public class GProperty extends GElement {

    public final String propertyName;
    public final boolean appendMode;
    public final GElement expression;

    public GProperty(String propertyName, boolean appendMode, GElement expression) {
        this.propertyName = Objects.requireNonNull(propertyName);
        this.appendMode = appendMode;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public GElement simplify() {
        return null;
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
        // property elements are not plain by definition
        return false;
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return expression.isOptional(gramat);
    }

    public static GProperty expectProperty(Tape tape) {
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

        return new GProperty(propertyName, appendMode, expression);
    }
}
