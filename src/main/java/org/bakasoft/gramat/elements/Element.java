package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.HashSet;
import java.util.Set;

abstract public class Element {

    abstract public void optimize(OptimizationControl control);

    abstract public boolean parse(Tape tape);

    abstract public void codify(CodifyControl control, boolean grouped);

    abstract public Object capture(Tape tape);

    abstract public void replace(CyclicControl control, Element older, Element newer);

    abstract public boolean isCyclic(CyclicControl control);

    abstract public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols);

    public Set<String> collectFirstAllowedSymbol() {
        HashSet<String> symbols = new HashSet<>();

        collectFirstAllowedSymbol(new CyclicControl(), symbols);

        return symbols;
    }

    public boolean isCyclic() {
        return isCyclic(new CyclicControl());
    }

    public Element optimize() {
        OptimizationControl control = new OptimizationControl(this);

//        System.out.println("optimizing...");

        do {
            control.reset();

//            System.out.println(control.getRoot());

            control.optimize();

//            System.out.println("    optimizations: " + control.getCount());
        }
        while (control.hasChanged());

//        System.out.println(control.getRoot());

        return control.getRoot();
    }

    public String captureText(Tape tape) {
        int pos0 = tape.getPosition();

        if (parse(tape)) {
            int posF = tape.getPosition();

            return tape.substring(pos0, posF);
        }

        return null;
    }

    @Override
    public String toString() {
        CodifyControl control = new CodifyControl();

        codify(control, false);

        return control.getCode();
    }
}
