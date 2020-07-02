package gramat.common;

import gramat.tools.FileTool;

import java.nio.file.Path;
import java.util.Objects;

public class TextSource {

    protected final String source;
    protected final char[] content;
    protected final int length;

    protected int position;

    public TextSource(Path file) {
        this(FileTool.loadString(file), file.toString());
    }

    public TextSource(String content) {
        this(content, null);
    }

    public TextSource(String content, String source) {
        this(content.toCharArray(), source);
    }

    public TextSource(char[] content, String source) {
        this.content = Objects.requireNonNull(content);
        this.length = content.length;
        this.source = source;
    }

    public String extract(int begin, int end) {
        return new String(content, begin, end - begin);
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    public TextLocation getLocation() {
        return locationOf(position);
    }

    public TextLocation locationOf(int index) {
        int line = 0;
        int column = 0;

        for (int i = 0; i < length; i++) {
            int c = content[i];

            if (c == '\n') {
                line += 1;
                column = 0;
            } else {
                column += 1;
            }

            if (i >= position) {
                break;
            }
        }
        return new TextLocation(source, index, line + 1, column);
    }
}
