package gramat.input;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Tape {

    public static Tape of(Path file) throws IOException {
        var data = Files.readAllBytes(file);

        return new Tape(new String(data, StandardCharsets.UTF_8), file.toString());
    }

    // TODO Add support to load more tapes from a string path
    private final String source;

    private final String content;

    private int position;

    public Tape(String content) {
        this(content, null);
    }

    public Tape(String content, String source) {
        this.content = content;
        this.source = source;
        this.position = 0;
    }

    public char peek() {
        if (position < 0 || position >= content.length()) {
            return '\0';
        }
        return content.charAt(position);
    }

    public void move() {
        position++;
    }

    public char read() {
        if (position < 0) {
            throw new RuntimeException();
        }
        else if (position >= content.length()) {
            throw new RuntimeException();
        }
        var chr = content.charAt(position);
        position++;
        return chr;
    }

    public boolean alive() {
        return position < content.length();
    }

    public boolean completed() {
        return position >= content.length();
    }

    public Location getLocation() {
        return locationOf(position);
    }

    public Location locationOf(int index) {
        int line = 0;
        int column = 0;

        for (int i = 0; i < content.length(); i++) {
            int c = content.charAt(i);

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
        return new Location(source, index, line + 1, column);
    }

    public String extract(int begin, int end) {
        return content.substring(begin, end);
    }

    public int getPosition() {
        return position;
    }
}