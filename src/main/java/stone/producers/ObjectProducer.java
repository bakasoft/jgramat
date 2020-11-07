package stone.producers;

public interface ObjectProducer {
    Object newInstance(String type);

    void set(Object obj, String key, Object value);
}
