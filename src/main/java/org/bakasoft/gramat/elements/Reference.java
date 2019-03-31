package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;
import java.util.function.Supplier;

public class Reference extends Element implements WrappedElement {

    private final String name;

    private final Supplier<Element> elementResolver;

    public Reference(String name, Supplier<Element> elementResolver) {
        this.name = name;
        this.elementResolver = elementResolver;
    }

    @Override
    public void replace(CyclicControl control, Element older, Element newer) {
        // nothing to be replaced
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        Element element = elementResolver.get();

        return control.isCyclic(element);
    }

    @Override
    public void optimize(OptimizationControl control) {
        control.enter(this, () -> {
            Element element = elementResolver.get();

            control.apply("resolve reference", this, element);
        });
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
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            output.append(name);
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            elementResolver.get().collectFirstAllowedSymbol(control, symbols);
        });
    }
}
