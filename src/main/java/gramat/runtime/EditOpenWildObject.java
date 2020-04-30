package gramat.runtime;

import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class EditOpenWildObject extends Edit {

    public final String typeName;

    public EditOpenWildObject(Source source, int position, String typeName) {
        super(source, position);
        this.typeName = typeName;
    }
}
