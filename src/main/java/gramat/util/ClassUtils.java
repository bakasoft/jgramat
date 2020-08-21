package gramat.util;

import java.util.ArrayList;

public class ClassUtils {

    public static String prettyType(Object any) {
        if (any == null) {
            return "null";
        }

        var type = any.getClass();

        return type.getSimpleName();
    }

    public static String prettyValue(Object instance) {
        if (instance instanceof String) {
            // TODO escape, trim, etc...
            return "\"" + instance + "\"";
        }

        return instance.toString();
    }

    public static String prettyString(Object instance, Object... parameters) {
        var params = new ArrayList<String>();

        if (parameters != null) {
            for (var parameter : parameters) {
                params.add(prettyValue(parameter));
            }
        }

        return prettyType(instance) + "<" + String.join(", ", params) + ">";
    }
}
