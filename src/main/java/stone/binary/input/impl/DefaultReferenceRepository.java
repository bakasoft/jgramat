package stone.binary.input.impl;

import stone.binary.input.ReferenceRepository;
import stone.binary.input.Value;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultReferenceRepository implements ReferenceRepository {
    private final Map<Long, Value> references;

    public DefaultReferenceRepository() {
        this.references = new LinkedHashMap<>();
    }

    @Override
    public Value get(long reference) {
        return references.get(reference);
    }

    @Override
    public boolean contains(long reference) {
        return references.containsKey(reference);
    }

    @Override
    public void set(long reference, Value value) {
        if (references.containsKey(reference)) {
            throw new RuntimeException();
        }
        references.put(reference, value);
    }
}
