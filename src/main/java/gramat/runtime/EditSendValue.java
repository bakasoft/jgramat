package gramat.runtime;

import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class EditSendValue extends Edit {

    public final Object value;

    public EditSendValue(Source source, int position, Object value) {
        super(source, position);
        this.value = value;
    }

}
