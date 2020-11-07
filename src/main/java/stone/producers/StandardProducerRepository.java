package stone.producers;

import stone.errors.StoneException;

import java.util.LinkedHashMap;
import java.util.Map;

public class StandardProducerRepository implements ProducerRepository {

    private final Map<String, Object> makers;

    private ObjectProducer genericObjectProducer;
    private ArrayProducer genericArrayProducer;
    private ValueProducer genericValueProducer;

    public StandardProducerRepository() {
        this.makers = new LinkedHashMap<>();
        this.genericObjectProducer = new GenericObjectProducer();
        this.genericArrayProducer = new GenericListProducer();
        this.genericValueProducer = new GenericValueProducer();
    }

    public void addObjectMakerFor(Class<?> type) {
        addObjectMakerFor(type, type.getSimpleName());
    }

    public void addObjectMakerFor(Class<?> type, String name) {
        makers.put(name, new ClassObjectProducer(type));
    }

    private void addMakerG(String type, Object maker) {
        if (makers.containsKey(type)) {
            throw new RuntimeException();
        }
        makers.put(type, maker);
    }

    private <T> T findMakerG(Class<T> typeClass, String typeName, T defaultValue) {
        var maker = makers.get(typeName);

        if (maker != null) {
            if (typeClass.isInstance(maker)) {
                return typeClass.cast(maker);
            }
            else {
                throw new StoneException();
            }
        }
        else if (defaultValue == null) {
            throw new StoneException("No maker for " + typeName);
        }
        else {
            return defaultValue;
        }
    }

    public void addMaker(String type, ObjectProducer maker) {
        addMakerG(type, maker);
    }

    public void addMaker(String type, ArrayProducer maker) {
        addMakerG(type, maker);
    }

    public void addMaker(String type, ValueProducer maker) {
        addMakerG(type, maker);
    }

    @Override
    public ObjectProducer findObjectMaker(String type) {
        return findMakerG(ObjectProducer.class, type, genericObjectProducer);
    }

    @Override
    public ArrayProducer findArrayMaker(String type) {
        return findMakerG(ArrayProducer.class, type, genericArrayProducer);
    }

    @Override
    public ValueProducer findValueMaker(String type) {
        return findMakerG(ValueProducer.class, type, genericValueProducer);
    }

    public ObjectProducer getGenericObjectMaker() {
        return genericObjectProducer;
    }

    public void setGenericObjectMaker(ObjectProducer genericObjectProducer) {
        this.genericObjectProducer = genericObjectProducer;
    }

    public ArrayProducer getGenericArrayMaker() {
        return genericArrayProducer;
    }

    public void setGenericArrayMaker(ArrayProducer genericArrayProducer) {
        this.genericArrayProducer = genericArrayProducer;
    }

    public ValueProducer getGenericValueMaker() {
        return genericValueProducer;
    }

    public void setGenericValueMaker(ValueProducer genericValueProducer) {
        this.genericValueProducer = genericValueProducer;
    }
}
