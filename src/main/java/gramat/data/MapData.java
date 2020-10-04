package gramat.data;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapData extends LinkedHashMap<String, Object> {

    private final String typeHint;

    public MapData(String typeHint) {
        this.typeHint = typeHint;
    }

    public MapData(String typeHint, Map<String, ?> map) {
        this(typeHint);
        this.putAll(map);
    }

    public String getTypeHint() {
        return typeHint;
    }

    @Override
    public String toString() {
        return (typeHint != null ? typeHint : "") + super.toString();
    }

}
