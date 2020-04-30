package gramat.runtime;

import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class EditOpenTypedObject extends Edit {

    public final Class<?> type;

    public EditOpenTypedObject(Source source, int position, Class<?> type) {
        super(source, position);
        this.type = type;
    }
}
