package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.ResolvedValueData;
import org.bakasoft.gramat.parsing.ValueData;

import java.util.Set;
import java.util.function.Function;

public class ValueElement extends Element implements WrappedElement {

    private final Class<?> type;
    private final Function<Object, ?> converter; // TODO change to Function<String, ?>
    private Element element;

    public ValueElement(Class<?> type, Function<Object, ?> converter, Element element) {
        this.type = type;
        this.element = element;
        this.converter = converter;
    }

    public ValueElement(Grammar grammar, ValueData data) {
        grammar.addElement(data, this);

        this.type = grammar.resolveType(data.getName());
        this.element = grammar.settle(data.getExpression());
        this.converter = grammar.getParser(type);
    }

    public ValueElement(Grammar grammar, ResolvedValueData data) {
        grammar.addElement(data, this);

        this.type = data.getType();
        this.element = grammar.settle(data.getExpression());
        this.converter = grammar.getParser(type);
    }

    @Override
    public void replace(CyclicControl control, Element older, Element newer) {
        control.enter(this, () -> {
            if (element == older) {
                element = newer;
            }
            else {
                element.replace(control, older, newer);
            }
        });
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return control.isCyclic(element);
    }

    @Override
    public void optimize(OptimizationControl control) {
        control.enter(this, () -> {
            control.next(element);
        });
    }

    @Override
    public boolean parse(Tape tape) {
        return element.parse(tape);
    }

    @Override
    public Object capture(Tape tape) {
        String raw = element.captureText(tape);

        if (raw == null) {
            return null; // TODO improve how to handle null
        }

        return converter.apply(raw);
    }

    @Override
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            output.append(type.getCanonicalName());
            output.append('=');
            element.codify(control, true);
            output.append(';');
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            element.collectFirstAllowedSymbol(control, symbols);
        });
    }
}


