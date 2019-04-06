package org.bakasoft.gramat.elements;

import java.util.Set;

public class NamedProperty extends Property {

    private final String propertyName;
    private final boolean appendMode;

    private Element element;

    public NamedProperty(String propertyName, boolean appendMode, Element element) {
        this.propertyName = propertyName;
        this.appendMode = appendMode;
        this.element = element;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        if (parsePushValue(element, ctx)) {
            ctx.builder.popValue(propertyName, appendMode);
            return true;
        }

        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return control.add(element) && element.isOptional(control);
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        if (control.add(element)) {
            element.collectFirstAllowedSymbol(control, symbols);
        }
    }

    @Override
    public Element link() {
        return new NamedProperty(propertyName, appendMode, element.link());
    }
}
