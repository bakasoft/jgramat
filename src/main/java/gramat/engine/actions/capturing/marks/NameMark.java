package gramat.engine.actions.capturing.marks;

import gramat.util.ClassUtils;

public class NameMark implements Mark {

    public final String name;

    public NameMark(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ClassUtils.prettyString(this, name);
    }
}
