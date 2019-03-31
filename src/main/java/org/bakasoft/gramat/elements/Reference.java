package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Map;
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
    public boolean parse(Tape tape) {
        return elementResolver.get().parse(tape);
    }

    @Override
    public Object capture(Tape tape) {
        return elementResolver.get().capture(tape);
    }

    @Override
    public Element link() {
        return elementResolver.get();
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            elementResolver.get().collectFirstAllowedSymbol(control, symbols);
        });
    }
}
