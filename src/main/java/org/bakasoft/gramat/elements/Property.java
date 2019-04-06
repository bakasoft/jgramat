package org.bakasoft.gramat.elements;

import java.util.Set;

public class Property extends Element {

    private final String propertyName;
    private final boolean appendMode;

    private Element element;

    public Property(String propertyName, boolean appendMode, Element element) {
        this.propertyName = propertyName;
        this.appendMode = appendMode;
        this.element = element;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        boolean result;

        if (element instanceof TypeElement || element instanceof ValueElement) {
            result = element.parse(ctx);
        }
        else {
            int pos0 = ctx.tape.getPosition();

            result = element.parse(ctx);

            if (result) {
                int posF = ctx.tape.getPosition();
                String value = ctx.tape.substring(pos0, posF);
                ctx.builder.pushValue(value);
            }
        }

        if (result) {
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
        return new Property(propertyName, appendMode, element.link());
    }
}
