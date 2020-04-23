package gramat.runtime;

import gramat.util.parsing.Location;

public class EditOpenWildList extends Edit {

    public final String typeName;

    public EditOpenWildList(Location location, String typeName) {
        super(location);
        this.typeName = typeName;
    }
}
