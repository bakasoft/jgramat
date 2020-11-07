package stone.binary.schema.impl;

import stone.binary.schema.IndexedType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IndexedTypeRepository {

    private final List<IndexedType> types;

    public IndexedTypeRepository() {
        types = new ArrayList<>();
    }

    public IndexedType get(int index) {
        for (var type : types) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

    public boolean contains(int index) {
        return get(index) != null;
    }

    public IndexedType create(int index) {
        if (contains(index)) {
            throw new RuntimeException();
        }

        var indexedType = new DefaultIndexedType(index);

        types.add(indexedType);

        return indexedType;
    }

    public void add(IndexedType type) {
        Objects.requireNonNull(type);

        if (contains(type.getIndex())) {
            throw new RuntimeException();
        }

        types.add(type);
    }

    public List<IndexedType> getTypes() {
        return Collections.unmodifiableList(types);
    }

}
