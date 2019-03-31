package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.*;
import org.bakasoft.gramat.handlers.ObjectHandler;

import java.util.Map;
import java.util.Set;

public class Property extends Element {

    private final String propertyName;
    private final boolean appendMode;

    private Element element;

    public Property(String propertyName, boolean appendMode, Element element) {
        this.propertyName = propertyName;
        this.appendMode = appendMode;
        this.element = element;
    }

    @Override
    public boolean parse(Tape tape) {
        int pos0 = tape.getPosition();
        Location loc0 = tape.getLocation();
        Object value = element.capture(tape);

        if (value == null) {
            // did not match!
            tape.setPosition(pos0);
            return tape.no(this);
        }

        ObjectHandler entity = tape.peekHandler();

        if (value instanceof GrammarElement) {
            ((GrammarElement) value).setBeginLocation(loc0);
            ((GrammarElement) value).setEndLocation(tape.getLocation());
        }

//        System.out.println(" SET " + propertyName + ": <" + Json.stringify(value, 2) + ">");

        if (appendMode) {
            entity.addValue(propertyName, value);
        }
        else {
            entity.setValue(propertyName, value);
        }

        // perfect match!
        return tape.ok(this);
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public Element link() {
        return new Property(propertyName, appendMode, element.link());
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            element.collectFirstAllowedSymbol(control, symbols);
        });
    }
}
