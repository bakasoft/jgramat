package stone.errors;

public class WriterNotFoundException extends StoneException {
    public WriterNotFoundException(Class<?> targetType) {
        super(String.format("Not writer found for: %s", targetType));
    }
}
