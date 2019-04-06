package org.bakasoft.gramat.elements;

import java.util.Set;

public class Negation extends Element {

    private Element element;

    public Negation(Element element) {
        this.element = element;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        int pos0 = ctx.tape.getPosition();

        if (element.parse(ctx)) {
            // did not match!
            ctx.tape.setPosition(pos0);
            return false;
        }

        if (ctx.tape.alive()) {
            // perfect match!
            char c = ctx.tape.peek();
            ctx.capture.append(c);
            ctx.tape.moveForward();
            return true;
        }

        ctx.tape.setPosition(pos0);
        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return control.add(element) && !element.isOptional(control);
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        // no symbols to collect
    }

    @Override
    public Element link() {
        return new Negation(element.link());
    }

}
