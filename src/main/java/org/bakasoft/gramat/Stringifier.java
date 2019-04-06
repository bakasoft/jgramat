package org.bakasoft.gramat;

public class Stringifier {

    public static String literal(char c) {
        return literal(String.valueOf(c));
    }

    public static String literal(String content) {
        StringBuilder builder = new StringBuilder();
        literal(content, builder);
        return builder.toString();
    }

    public static void literal(String content, StringBuilder output) {
        output.append('"');
        if (content != null) {
            output.append(content
                    .replace("\\", "\\\\")
                    .replace("/", "\\/")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\t", "\\t")
            );

            // TODO replace unicode chars
        }
        output.append('"');
    }

}
