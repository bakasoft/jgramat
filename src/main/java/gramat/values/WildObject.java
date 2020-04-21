package gramat.values;

import java.util.HashMap;

public class WildObject extends ObjectValue {

    private final String typeName;

    public WildObject(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object build() {
        var result = new HashMap<String, Object>();
        var values = getAttributes();

        values.forEach((key, value) -> result.put(key, value.build()));

        if (typeName != null) {
            result.put("@type", typeName);
        }

        return result;
    }
}
