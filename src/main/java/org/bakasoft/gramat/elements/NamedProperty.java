package org.bakasoft.gramat.elements;

import java.util.Map;
import java.util.Set;

public class NamedProperty extends Property {

    private final String propertyName;
    private final boolean appendMode;

    private Element producer;

    public NamedProperty(String propertyName, boolean appendMode, Element producer) {
        this.propertyName = propertyName;
        this.appendMode = appendMode;
        this.producer = producer;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        if (producer.parse(ctx)) {
            ctx.builder.popValue(propertyName, appendMode);
            return true;
        }

        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return control.add(producer) && producer.isOptional(control);
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        if (control.add(producer)) {
            producer.collectFirstAllowedSymbol(control, symbols);
        }
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        if (control.add(this)) {
            producer = resolveInto(rules, control, producer);
        }
    }
}
