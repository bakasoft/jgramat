package stone.binary.input;

public interface ReferenceRepository {
    Value get(long reference);

    boolean contains(long reference);  // TODO this might not be necessary

    void set(long reference, Value value);
}
