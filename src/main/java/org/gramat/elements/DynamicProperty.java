package org.gramat.elements;

import java.util.Map;
import java.util.Set;

public class DynamicProperty extends Property {

    private Element nameElement;
    private Element separatorElement;
    private Element valueProducer;

    private final boolean appendMode;
    private final boolean invertedMode;

    public DynamicProperty(Element nameElement, Element separatorElement, Element valueProducer, boolean appendMode, boolean invertedMode) {
        this.nameElement = nameElement;
        this.separatorElement = separatorElement;
        this.valueProducer = valueProducer;
        this.appendMode = appendMode;
        this.invertedMode = invertedMode;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        if (invertedMode) {
            if (valueProducer.parse(ctx) && (separatorElement == null || separatorElement.parse(ctx))) {
                String propertyName = parseText(nameElement, ctx);

                if (propertyName != null) {
                    ctx.builder.popValue(propertyName, appendMode);
                    return true;
                }
            }
        }
        else {
            String propertyName = parseText(nameElement, ctx);

            if (propertyName != null
                    && (separatorElement == null || separatorElement.parse(ctx))
                    && valueProducer.parse(ctx)) {
                ctx.builder.popValue(propertyName, appendMode);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return control.add(nameElement) && nameElement.isOptional(control)
                && (separatorElement == null || control.add(separatorElement) && separatorElement.isOptional(control))
                && control.add(valueProducer) && valueProducer.isOptional(control);
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        if (invertedMode) {
            if (control.add(valueProducer)) {
                valueProducer.collectFirstAllowedSymbol(control, symbols);
            }
        }
        else {
            if (control.add(nameElement)) {
                nameElement.collectFirstAllowedSymbol(control, symbols);
            }
        }
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        if (control.add(this)) {
            nameElement = resolveInto(rules, control, nameElement);
            separatorElement = resolveInto(rules, control, separatorElement);
            valueProducer = resolveInto(rules, control, valueProducer);
        }
    }
}
