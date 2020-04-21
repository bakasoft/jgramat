package gramat.runtime;

public class EditOpenTypedList extends Edit {

    public final Class<?> type;

    public EditOpenTypedList(Class<?> type) {
        this.type = type;
    }
}
