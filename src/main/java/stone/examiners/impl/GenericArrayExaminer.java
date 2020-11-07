package stone.examiners.impl;

import stone.examiners.ArrayExaminer;

import java.lang.reflect.Array;

public class GenericArrayExaminer implements ArrayExaminer {

    public static final GenericArrayExaminer INSTANCE = new GenericArrayExaminer();

    private GenericArrayExaminer() {}

    @Override
    public int getSizeOf(Object value) {
        return Array.getLength(value);
    }

    @Override
    public Object getValueAt(int index, Object value) {
        return Array.get(value, index);
    }

    @Override
    public String getType() {
        return null;  // Anonymous
    }
}
