package org.bakasoft.gramat.elements;

import java.util.Set;
import java.util.function.Supplier;

public class Reference extends Element {

    private final String name;

    private final Supplier<Element> elementResolver;

    public Reference(String name, Supplier<Element> elementResolver) {
        this.name = name;
        this.elementResolver = elementResolver;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        return elementResolver.get().parse(ctx);
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        Element element = elementResolver.get();

        return control.add(element) && element.isOptional(control);
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        Element element = elementResolver.get();
        if (control.add(element)) {
            element.collectFirstAllowedSymbol(control, symbols);
        }
    }

    @Override
    public Element link() {
        return elementResolver.get();
    }
}
