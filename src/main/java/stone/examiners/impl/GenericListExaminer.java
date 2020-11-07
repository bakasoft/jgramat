package stone.examiners.impl;

import stone.examiners.ArrayExaminer;

import java.util.List;

public class GenericListExaminer implements ArrayExaminer {

    public static final GenericListExaminer ANONYMOUS_INSTANCE = new GenericListExaminer(null);

    private final String type;

    public GenericListExaminer(String type) {
        this.type = type;
    }

    private static List<?> list(Object any) {
        return (List<?>)any;
    }

    @Override
    public int getSizeOf(Object value) {
        return list(value).size();
    }

    @Override
    public Object getValueAt(int index, Object value) {
        return list(value).get(index);
    }

    @Override
    public String getType() {
        return type;
    }
}
