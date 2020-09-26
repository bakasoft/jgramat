package gramat.data;

import java.util.ArrayList;

public class ListData extends ArrayList<Object> {

    private final String typeHint;

    public ListData(String typeHint) {
        this.typeHint = typeHint;
    }

    public String getTypeHint() {
        return typeHint;
    }

    @Override
    public String toString() {
        return (typeHint != null ? typeHint : "") + super.toString();
    }
}
