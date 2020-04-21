package gramat.values;

import gramat.util.ReflectionTool;

import java.util.List;

public class TypedList extends ListValue {

    private final Class<?> type;

    public TypedList(Class<?> type) {
        this.type = type;
    }

    @Override
    public Object build() {
        Object instance = ReflectionTool.newInstance(type);

        if (instance instanceof List) {
            var list = (List<Object>)instance;  // TODO handle warning better

            for (var value : getValues()) {
                list.add(value.build());
            }
        }

        return instance;
    }
}
