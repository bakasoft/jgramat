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

}
