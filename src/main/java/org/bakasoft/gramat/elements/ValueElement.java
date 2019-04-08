package org.bakasoft.gramat.elements;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ValueElement extends Element {

    private final Function<String, ?> parser;
    private Element element;

    public ValueElement(Function<String, ?> parser, Element element) {
        this.element = element;
        this.parser = parser;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        ctx.capture.beginTransaction();

        if (element.parse(ctx)) {
            String value = ctx.capture.commitTransaction();

            ctx.builder.pushValue(value, parser);
            return true;
        }

        ctx.capture.rollbackTransaction();
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
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        if (control.add(this)) {
            element = resolveInto(rules, control, element);
        }
    }
}


