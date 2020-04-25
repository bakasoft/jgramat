package gramat.values;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WildObject extends ObjectValue {

    private final String typeName;

    public WildObject(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object build() {
        var result = new LinkedHashMap<String, Object>();
        var values = getAttributes();

        if (typeName != null) {
            result.put("@type", typeName);
        }

        values.forEach((key, value) -> result.put(key, value.build()));

        return result;
    }
}
