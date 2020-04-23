package gramat.runtime;

import gramat.util.parsing.Location;

public class EditOpenTypedObject extends Edit {

    public final Class<?> type;

    public EditOpenTypedObject(Location location, Class<?> type) {
        super(location);
        this.type = type;
    }
}
