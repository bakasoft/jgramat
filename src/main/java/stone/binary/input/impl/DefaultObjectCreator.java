package stone.binary.input.impl;

import gramat.util.PP;
import stone.binary.input.ObjectCreator;
import stone.binary.input.Value;
import stone.binary.schema.IndexedField;
import stone.binary.schema.IndexedType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class DefaultObjectCreator<T> implements ObjectCreator {

    private final Class<T> typeClass;
    private final Map<IndexedField, BiConsumer<T, Value>> setters;

    private IndexedType typeIndex;
    private Supplier<T> instantiator;

    public DefaultObjectCreator(Class<T> typeClass) {
        this.typeClass = typeClass;
        this.setters = new LinkedHashMap<>();
    }

    public IndexedField getField(int index) {
        for (var field : setters.keySet()) {
            if (field.getIndex() == index) {
                return field;
            }
        }
        return null;
    }

    public void addField(IndexedField field, BiConsumer<T, Value> setter) {
        if (getField(field.getIndex()) != null) {
            throw new RuntimeException();
        }
        setters.put(field, setter);
    }

    @Override
    public IndexedType getTypeIndex() {
        return typeIndex;
    }

    @Override
    public Object newInstance() {
        return instantiator.get();
    }

    @Override
    public IndexedField findField(int index) {
        for (var field : setters.keySet()) {
            if (field.getIndex() == index) {
                return field;
            }
        }

        throw new RuntimeException("Field not found: " + PP.hex(index));
    }

    @Override
    public void setValue(Object object, IndexedField field, Value value) {
        if (!typeClass.isInstance(object)) {
            throw new RuntimeException();
        }

        var o = typeClass.cast(object);

        var setter = setters.get(field);

        if (setter == null) {
            throw new RuntimeException();
        }

        setter.accept(o, value);
    }

    public Collection<IndexedField> getFields() {
        return setters.keySet();
    }

    public void setTypeIndex(IndexedType typeIndex) {
        this.typeIndex = typeIndex;
    }

    public void setInstantiator(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public Supplier<T> getInstantiator() {
        return instantiator;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }
}
