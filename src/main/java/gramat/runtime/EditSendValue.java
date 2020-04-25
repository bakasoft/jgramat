package gramat.runtime;

import gramat.util.parsing.Location;

public class EditSendValue extends Edit {

    public final Object value;

    public EditSendValue(Location location, Object value) {
        super(location);
        this.value = value;
    }

}
