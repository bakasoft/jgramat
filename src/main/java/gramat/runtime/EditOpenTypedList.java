package gramat.runtime;

import gramat.util.parsing.Location;

public class EditOpenTypedList extends Edit {

    public final Class<?> type;

    public EditOpenTypedList(Location location, Class<?> type) {
        super(location);
        this.type = type;
    }
}
