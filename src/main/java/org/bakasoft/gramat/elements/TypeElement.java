package org.bakasoft.gramat.elements;

import java.util.Set;

public class TypeElement extends Element {

    private final Class<?> type;
    private Element element;

    public TypeElement(Class<?> type, Element element) {
        this.type = type;
        this.element = element;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        ctx.builder.openObject(type);

        if (element.parse(ctx)) {
            ctx.builder.pushObject();
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
        return new TypeElement(type, element.link());
    }
}
