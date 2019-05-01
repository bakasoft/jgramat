package org.gramat.elements;

import java.util.Map;
import java.util.Set;

public class Repetition extends Element {

    private Element element;
    private final int minimum;
    private final int maximum;
    private Element separator;

    public Repetition(Element element, int minimum, int maximum, Element separator) {
        this.element = element;
        this.minimum = minimum;
        this.maximum = maximum;
        this.separator = separator;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        int pos0 = ctx.tape.getPosition();
        boolean expectMore = false;
        int count = 0;

        while (element.parse(ctx)) {
            if (separator != null) {
                if (separator.parse(ctx)) {
                    expectMore = true;
                } else {
                    expectMore = false;
                }
            }
            else {
                expectMore = false;
            }

            count++;
        }

        if (expectMore) {
            // did not match!
            ctx.tape.setPosition(pos0);
            return false;
        }
        else if (count < minimum) {
            // did not match!
            ctx.tape.setPosition(pos0);
            return false;
        }
        else if (maximum > 0 && count > maximum) {
            // did not match!
            ctx.tape.setPosition(pos0);
            return false;
        }

        // perfect match!
        return true;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return minimum == 0 || control.add(element) && element.isOptional(control);
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
            separator = resolveInto(rules, control, separator);
        }
    }
}
