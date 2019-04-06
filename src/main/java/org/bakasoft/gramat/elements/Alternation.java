package org.bakasoft.gramat.elements;

import java.util.Set;

public class Alternation extends Element {

    private final Element[] elements;

    public Alternation(Element[] elements) {
        this.elements = elements;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        int pos0 = ctx.tape.getPosition();

        for (Element element : elements) {
            if (element.parse(ctx)) {
                // perfect match!
                return true;
            }
            else {
                // try with another!
                ctx.tape.setPosition(pos0);
            }
        }

        // did not match!
        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        for (Element element : elements) {
            if (control.add(element) && element.isOptional(control)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        for (Element element : elements) {
            if (control.add(element)) {
                element.collectFirstAllowedSymbol(control, symbols);
            }
        }
    }

    @Override
    public Element link() {
        return new Alternation(linkAll(elements));
    }
}
