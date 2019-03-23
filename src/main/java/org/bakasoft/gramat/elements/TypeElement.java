package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.ObjectHandle;
import org.bakasoft.gramat.ReflectionHelper;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.ResolvedTypeData;
import org.bakasoft.gramat.parsing.TypeData;

import java.util.Set;

public class TypeElement extends Element implements WrappedElement {

    private final Class<?> type;
    private Element element;

    public TypeElement(Class<?> type, Element element) {
        this.type = type;
        this.element = element;
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

    public TypeElement(Grammar grammar, TypeData data) {
        grammar.addElement(data, this);

        this.type = grammar.resolveType(data.getName());
        this.element = grammar.settle(data.getExpression());
    }

    public TypeElement(Grammar grammar, ResolvedTypeData data) {
        grammar.addElement(data, this);

        this.type = data.getType();
        this.element = grammar.settle(data.getExpression());
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
        Object instance = ReflectionHelper.newInstance(type);

        tape.pushCapture(new ObjectHandle(type, instance));

//        System.out.println("TRAN " + type);

        if (element.parse(tape)) {
//            System.out.println("COMM " + type);
            tape.popCapture();

            return instance;
        }

//        System.out.println("ROLL " + type);
        tape.popCapture();
        return null;
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
