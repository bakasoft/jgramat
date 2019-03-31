package org.bakasoft.gramat.parsing.literals;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.parsing.GLiteral;

import java.util.ArrayList;

public class GArray extends GLiteral {

    private final GLiteral[] array;

    public GArray(GLiteral[] array) {
        this.array = array;
    }

    public int size() {
        return array.length;
    }

    public GLiteral get(int index) {
        return array[index];
    }

    // parsing

    public static GArray expectArray(Tape tape) {
        ArrayList<GLiteral> list = new ArrayList<>();

        GElement.expectSymbol(tape, '[');

        GElement.skipVoid(tape);

        GLiteral literal;

        while ((literal = GLiteral.tryLiteral(tape)) != null) {
            list.add(literal);

            GElement.skipVoid(tape);
        }

        GElement.expectSymbol(tape, ']');

        return new GArray(list.toArray(new GLiteral[0]));
    }
}
