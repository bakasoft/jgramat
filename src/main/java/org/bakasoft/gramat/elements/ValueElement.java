package org.bakasoft.gramat.elements;

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
        int pos0 = ctx.tape.getPosition();

        if (element.parse(ctx)) {
            int posF = ctx.tape.getPosition();
            String value = ctx.tape.substring(pos0, posF);

            ctx.builder.pushValue(value, parser);
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
        return new ValueElement(parser, element.link());
    }
}


