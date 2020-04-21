package gramat.runtime;

public class EditOpenTypedObject extends Edit {

    public final Class<?> type;

    public EditOpenTypedObject(Class<?> type) {
        this.type = type;
    }
}
