package stone.common;

public class GenericValue {

    private final String type;
    private final Object[] arguments;

    public GenericValue(String type, Object[] arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public String getType() {
        return type;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
