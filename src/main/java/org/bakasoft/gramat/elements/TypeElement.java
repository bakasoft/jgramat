package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.*;
import org.bakasoft.gramat.handlers.DefaultHandler;
import org.bakasoft.gramat.handlers.ObjectHandler;
import org.bakasoft.gramat.handlers.TypedHandler;
import org.bakasoft.gramat.util.ReflectionHelper;

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

    @Override
    public boolean isCyclic(CyclicControl control) {
        return control.isCyclic(element);
    }

    @Override
    public boolean parse(Tape tape) {
        return element.parse(tape);
    }

    @Override
    public Object capture(Tape tape) {
        ObjectHandler handler;

        if (type != null) {
            Object instance = ReflectionHelper.newInstance(type);
            handler = new TypedHandler(type, instance);

            tape.pushHandler(handler);
        }
        else {
            handler = new DefaultHandler();
            tape.pushHandler(handler);
        }

//        System.out.println("TRAN " + type);

        if (element.parse(tape)) {
//            System.out.println("COMM " + type);
            tape.popHandler();

            return handler.getInstance();
        }

//        System.out.println("ROLL " + type);
        tape.popHandler();
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
