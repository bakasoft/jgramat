package gramat.util;

public class GramatWriter {

    public static String toDelimitedString(String value, char delimiter) {
        var output = new StringBuilder();

        output.append(delimiter);

        for (var c : value.toCharArray()) {
            if (c == '\n') {
                output.append("\\n");
            }
            else if (c == '\t') {
                output.append("\\t");
            }
            else if (c == '\f') {
                output.append("\\f");
            }
            else if (c == '\b') {
                output.append("\\b");
            }
            else if (c == delimiter) {
                output.append('\\');
                output.append(delimiter);
            }
            else if (Character.isISOControl(c)) {
                var hex = Integer.toHexString(c);
                output.append("\\u");
                if (hex.length() < 4) {
                    output.append("0".repeat(4 - hex.length()));
                }
                output.append(hex);
            }
            else {
                output.append(c);
            }
        }

        output.append(delimiter);

        return output.toString();
    }

}
