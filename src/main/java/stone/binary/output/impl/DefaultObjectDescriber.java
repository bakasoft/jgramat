package stone.binary.output.impl;

import stone.binary.output.ObjectDescriber;
import stone.binary.schema.IndexedField;
import stone.binary.schema.IndexedType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultObjectDescriber<T> implements ObjectDescriber {

    private final Class<T> typeClass;
    private final Map<IndexedField, Function<T, Object>> getters;

    private IndexedType typeIndex;

    public DefaultObjectDescriber(Class<T> typeClass) {
        this.typeClass = typeClass;
        this.getters = new LinkedHashMap<>();
    }

    @Override
    public boolean accepts(Object any) {
        return typeClass.isInstance(any);
    }

    public IndexedField getField(int index) {
        for (var field : getters.keySet()) {
            if (field.getIndex() == index) {
                return field;
            }
        }
        return null;
    }

    public void addField(IndexedField field, Function<T, Object> getter) {
        if (getField(field.getIndex()) != null) {
            throw new RuntimeException();
        }

        getters.put(field, getter);
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    @Override
    public IndexedType getTypeIndex() {
        return typeIndex;
    }

    @Override
    public Collection<IndexedField> getFields() {
        return getters.keySet();
    }

    @Override
    public Object getValue(Object object, IndexedField field) {
        if (!typeClass.isInstance(object)) {
            throw new RuntimeException();
        }

        var o = typeClass.cast(object);

        var getter = getters.get(field);

        if (getter == null) {
            throw new RuntimeException();
        }

        return getter.apply(o);
    }

    public void setTypeIndex(IndexedType typeIndex) {
        this.typeIndex = typeIndex;
    }
}
