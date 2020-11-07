package stone.ref;

public interface StoneReferenceStore {

    Object getReference(String type, Object value);

    Object getValue(String type, Object reference);

    void set(String type, Object value, Object reference);

    Object add(String type, Object value);

    boolean containsReference(String type, Object reference);
}
