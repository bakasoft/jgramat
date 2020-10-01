package gramat.util;

public class StringUtils {

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
