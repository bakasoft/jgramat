package stone.binary.output.impl;

import stone.binary.output.ReferenceStrategy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Predicate;

public class HashReferenceStrategy implements ReferenceStrategy {

    private final Predicate<Object> condition;
    private final Map<Class<?>, Map<Object, Long>> references;

    private long nextRef;

    public HashReferenceStrategy(Predicate<Object> condition) {
        this(condition, 1);
    }

    public HashReferenceStrategy(Predicate<Object> condition, long initialRef) {
        this.condition = condition;
        this.references = new LinkedHashMap<>();
        this.nextRef = initialRef;
    }

    @Override
    public boolean accepts(Object any) {
        return condition.test(any);
    }

    @Override
    public OptionalLong lookUp(Object value) {
        var map = loadMapFor(value);
        var ref = map.get(value);
        if (ref == null) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(ref);
    }

    @Override
    public long save(Object value) {
        var map = loadMapFor(value);
        var reference = nextRef;

        nextRef++;

        map.put(value, reference);

        return reference;
    }

    private Map<Object, Long> loadMapFor(Object value) {
        var valueClass = value != null ? value.getClass() : null;

        return references.computeIfAbsent(valueClass, k -> new LinkedHashMap<>());
    }

}
