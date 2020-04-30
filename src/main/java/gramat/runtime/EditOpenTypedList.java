package gramat.runtime;

import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class EditOpenTypedList extends Edit {

    public final Class<?> type;

    public EditOpenTypedList(Source source, int position, Class<?> type) {
        super(source, position);
        this.type = type;
    }
}
