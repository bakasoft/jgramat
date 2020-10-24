package gramat.binary;

import gramat.util.ReflectionUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DefaultObjectEditor<T> implements ObjectEditor {

    private final Class<T> type;
    private final IndexedType typeIndexed;

    private final Supplier<T> creator;
    private final Map<IndexedField, BiConsumer<T, Value>> setters;
    private final Map<IndexedField, Function<T, Value>> getters;
    private final List<IndexedField> fields;

    public DefaultObjectEditor(IndexedType typeIndexed, Class<T> type) {
        this(typeIndexed, type, ReflectionUtils.emptyConstructor(type));
    }

    public Class<T> getTypeClass() {
        return type;
    }

    public DefaultObjectEditor(IndexedType typeIndexed, Class<T> type, Supplier<T> creator) {
        this.typeIndexed = typeIndexed;
        this.type = type;
        this.creator = creator;
        this.setters = new HashMap<>();
        this.getters = new HashMap<>();
        this.fields = new ArrayList<>();
    }

    public IndexedField getField(int index) {
        for (var field : fields) {
            if (field.getIndex() == index) {
                return field;
            }
        }
        return null;
    }

    public void addField(int index, BiConsumer<T, Value> setter, Function<T, Value> getter) {
        if (getField(index) != null) {
            throw new RuntimeException();
        }

        var field = new DefaultIndexedField(index);

        fields.add(field);

        setters.put(field, setter);
        getters.put(field, getter);
    }

    @Override
    public IndexedType getType() {
        return typeIndexed;
    }

    @Override
    public Object newInstance() {
        return creator.get();
    }

    @Override
    public void setValue(Object object, IndexedField field, Value value) {
        if (!type.isInstance(object)) {
            throw new RuntimeException();
        }

        var o = type.cast(object);

        var setter = setters.get(field);

        if (setter == null) {
            throw new RuntimeException();
        }

        setter.accept(o, value);
    }

    @Override
    public Value getValue(Object object, IndexedField field) {
        if (!type.isInstance(object)) {
            throw new RuntimeException();
        }

        var o = type.cast(object);

        var getter = getters.get(field);

        if (getter == null) {
            throw new RuntimeException();
        }

        return getter.apply(o);
    }

    @Override
    public Collection<IndexedField> getFields() {
        return Collections.unmodifiableList(fields);
    }
}
