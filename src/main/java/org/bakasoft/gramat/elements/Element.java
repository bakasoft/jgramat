package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

abstract public class Element {

    public static Element eval(String code) {
        return GElement.expectExpression(new Tape(null, code))
                .simplify()
                .compile(new Gramat(), new HashMap<>());
    }

    abstract public boolean parse(Tape tape);

    abstract public void codify(CodifyControl control, boolean grouped);

    abstract public Object capture(Tape tape);

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
