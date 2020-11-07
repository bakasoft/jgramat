package stone.binary.output.impl;

public interface Describers {
    static <T> DescriberBuilder<T> builder(Class<T> typeClass) {
        return new DescriberBuilder<>(typeClass);
    }
}
