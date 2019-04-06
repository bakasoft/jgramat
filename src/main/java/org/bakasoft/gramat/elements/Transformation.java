package org.bakasoft.gramat.elements;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class Transformation extends Element {

    private final Function<String, String> transformation;
    private final Element element;

    public Transformation(Function<String, String> transformation, Element element) {
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
    public Element link() {
        return element.link();
    }
}
