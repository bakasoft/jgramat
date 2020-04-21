package gramat.values;

import java.util.ArrayList;

public class WildList extends ListValue {

    private final String typeName;

    public WildList(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object build() {
        var result = new ArrayList<Object>();

        // TODO what to do with the typeName? :/

        for (var value : getValues()) {
            result.add(value.build());
        }

        return result;
    }
}
