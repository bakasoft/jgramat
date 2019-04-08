package org.bakasoft.gramat.elements;

import java.util.Map;
import java.util.Set;

public class Optional extends Element {

    private Element element;

    public Optional(Element element) {
        this.element = element;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        element.parse(ctx);

        // perfect match!
        return true;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return true;
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        if (control.add(element)) {
            element.collectFirstAllowedSymbol(control, symbols);
        }
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        if (control.add(this)) {
            element = resolveInto(rules, control, element);
        }
    }
}
