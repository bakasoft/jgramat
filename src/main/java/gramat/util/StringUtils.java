package gramat.util;

import java.util.List;

public class StringUtils {

    public static <T> String join(String separator, T[] items) {
        if (items == null) {
            return "";
        }
        return join(separator, List.of(items));
    }

    public static String join(String separator, Iterable<?> items) {
        var output = new StringBuilder();

        if (items != null) {
            var i = 0;

            for (var item : items) {
                if (i > 0) {
                    output.append(separator);
                }
                output.append(item);
                i++;
            }
        }

        return output.toString();
    }

}
