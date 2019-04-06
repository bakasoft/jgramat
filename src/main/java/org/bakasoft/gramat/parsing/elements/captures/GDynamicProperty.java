package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.DynamicProperty;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GElement;

import java.util.Map;

public class GDynamicProperty extends GCapture {

    public final GElement nameExpression;
    public final GElement separatorExpression;
    public final GElement valueExpression;
    public final boolean appendMode;

    // Normal mode: key-value, inverted mode: value-key
    public final boolean invertedMode;

    public GDynamicProperty(GElement nameExpression, GElement separatorExpression, GElement valueExpression, boolean appendMode, boolean invertedMode) {
        this.nameExpression = nameExpression;
        this.separatorExpression = separatorExpression;
        this.appendMode = appendMode;
        this.valueExpression = valueExpression;
        this.invertedMode = invertedMode;
    }

    @Override
    public GElement simplify() {
        return new GDynamicProperty(
                nameExpression.simplify(),
                separatorExpression != null ? separatorExpression.simplify() : null,
                valueExpression.simplify(),
                appendMode,
                invertedMode
        );
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new DynamicProperty(
                nameExpression.compile(gramat, compiled),
                separatorExpression != null ? separatorExpression.compile(gramat, compiled) : null,
                valueExpression.compile(gramat, compiled),
                appendMode,
                invertedMode
        );
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        throw new UnsupportedOperationException();
    }
}
