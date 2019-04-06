package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
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

    public GDynamicProperty(GElement nameExpression, GElement separatorExpression, boolean appendMode, GElement valueExpression, boolean invertedMode) {
        this.nameExpression = nameExpression;
        this.separatorExpression = separatorExpression;
        this.appendMode = appendMode;
        this.valueExpression = valueExpression;
        this.invertedMode = invertedMode;
    }

    @Override
    public GElement simplify() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        throw new UnsupportedOperationException();
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
