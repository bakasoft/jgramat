package org.bakasoft.gramat.parsing.literals;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<GLiteral> getList() {
        return Arrays.asList(array); // TODO refactor to toArray
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
