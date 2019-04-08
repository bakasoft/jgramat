package org.bakasoft.gramat.elements;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class Transformation extends Element {

    private final Function<String, String> transformation;
    private Element element;

    public Transformation(Element element, Function<String, String> transformation) {
        this.transformation = Objects.requireNonNull(transformation);
        this.element = element;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        ctx.capture.beginTransaction();

        if (element.parse(ctx)) {
            ctx.capture.commitTransaction(transformation);
            return true;
        }

        ctx.capture.rollbackTransaction();
        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return element.isOptional();
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        element.collectFirstAllowedSymbol(control, symbols);
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        if (control.add(this)) {
            element = resolveInto(rules, control, element);
        }
    }
}
