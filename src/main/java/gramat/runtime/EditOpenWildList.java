package gramat.runtime;

import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class EditOpenWildList extends Edit {

    public final String typeName;

    public EditOpenWildList(Source source, int position, String typeName) {
        super(source, position);
        this.typeName = typeName;
    }
}
