package gramat.runtime;

import gramat.util.parsing.Location;

public class EditOpenWildObject extends Edit {

    public final String typeName;

    public EditOpenWildObject(Location location, String typeName) {
        super(location);
        this.typeName = typeName;
    }
}
