package gramat.runtime;

import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class Edit {

    public final Source source;
    public final int position;

    public Edit(Source source, int position) {
        this.source = source;
        this.position = position;
    }

    public Location getLocation() {
        return source.locationOf(position);
    }

}
