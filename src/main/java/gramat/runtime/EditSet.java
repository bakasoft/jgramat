package gramat.runtime;

import gramat.util.parsing.Source;

public class EditSet extends Edit {

    public final String name;

    public EditSet(Source source, int position, String name) {
        super(source, position);
        this.name = name;
    }

    @Override
    public String toString() {
        return "EditSet{" +
                "name='" + name + '\'' +
                '}';
    }
}
