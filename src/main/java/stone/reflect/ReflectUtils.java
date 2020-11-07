package stone.reflect;

public class ReflectUtils {
    public static Object convertTo(Object value, Class<?> type) {
        if (value == null) {
            if (type.isPrimitive()) {
                throw new RuntimeException("Cannot convert null to a primitive.");
            }
            return null;
        }
        else if (isCompatible(value, type)) {
            return value;
        }
        else if (type == Character.class && value instanceof String) {
            var str = (String)value;

            if (str.length() != 1) {
                throw new RuntimeException();
            }

            return str.charAt(0);
        }
        else if (value instanceof Number && type == Integer.class) {
            return ((Number)value).intValue();
        }
        else {
            throw new RuntimeException("Cannot convert " + value.getClass() + " to " + type);
        }
    }

    public static boolean isCompatible(Object value, Class<?> type) {
        return type.isInstance(value)
                || type == boolean.class && value instanceof Boolean;
    }
}
