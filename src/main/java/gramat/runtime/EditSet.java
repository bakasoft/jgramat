package gramat.runtime;

import gramat.util.parsing.Location;

public class EditSet extends Edit {

    public final String name;

    public EditSet(Location location, String name) {
        super(location);
        this.name = name;
    }

    @Override
    public String toString() {
        return "EditSet{" +
                "name='" + name + '\'' +
                '}';
    }
}
