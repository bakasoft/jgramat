package gramat.util.parsing;

import gramat.GramatException;
import gramat.util.FileTool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Source {

    public static final int EOF = -1;

    private final int[] content;
    private final Path file;
    private final int length;

    private int position;

    public static Source of(String input) {
        return new Source(input, null);
    }

    public static Source fromFile(Path file) {
        var content = FileTool.loadString(file);

        return new Source(content, file);
    }

    public Source(String content, Path file) {
        var chs = Objects.requireNonNull(content).toCharArray();

        this.content = new int[chs.length];
        for (int i = 0; i < chs.length; i++) {
            this.content[i] = chs[i];
        }
        this.length = content.length();
        this.file = file;
        this.position = 0;
    }

    public boolean alive() {
        return position < length;
    }

    public int peek() {
        if (position >= length) {
            return EOF;
        }

        return content[position];
    }

    public boolean moveNext() {
        if (position < length) {
            position++;
            return true;
        }
        return false;
    }

    public boolean testAny(Iterable<String> tokens) {
        for (String token : tokens) {
            if (test(token)) {
                return true;
            }
        }

        return false;
    }

    public boolean test(String token) {
        if (token == null) {
            return false;
        }

        int pos0 = position;

        for (var expectedChar : token.toCharArray()) {
            var actualChar = peek();

            if (actualChar != expectedChar) {
                position = pos0;
                return false;
            }

            moveNext();
        }

        position = pos0;
        return true;
    }

    public boolean pullAny(Iterable<String> tokens) {
        for (String token : tokens) {
            if (pull(token)) {
                return true;
            }
        }

        return false;
    }

    public boolean pull(int expectedChar) {
        var actualChar = peek();

        if (actualChar == expectedChar) {
            moveNext();
            return true;
        }

        return false;
    }

    public boolean pull(String token) {
        if (token == null) {
            return false;
        }

        int pos0 = position;

        for (var expectedChar : token.toCharArray()) {
            var actualChar = peek();

            if (actualChar != expectedChar) {
                position = pos0;
                return false;
            }

            moveNext();
        }

        return true;
    }

    public String readText(int length) throws ParseException {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < length; i++) {
            var c = peek();

            if (c == EOF) {
                throw error("Unexpected EOF");
            }

            moveNext();

            output.append(c);
        }

        return output.toString();
    }

    public void expect(int token) throws ParseException {
        if (!pull(token)) {
            throw error("Expected token: " + token);
        }
    }

    public void expect(String token) throws ParseException {
        if (!pull(token)) {
            throw error("Expected token: " + token);
        }
    }

    public void expectAny(Iterable<String> tokens) throws ParseException {
        if (!pullAny(tokens)) {
            throw error("Expected any of: " + tokens);
        }
    }

    public Location locationOf(int position) {
        return new Location(this, position);
    }

    public Coordinates coordinatesOf(int position) {
        int line = 0;
        int column = 0;

        for(int i = 0 ; i < length; i++){
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

        return new Coordinates(line + 1, column);
    }

    public ParseException error(String message) {
        return new ParseException(message, this, position);
    }

    public Path getFile() {
        return file;
    }

    public int getPosition() { return position; }

    public int getLength() { return length; }

    public int getChar(int index) { return content[index]; }

    public void setPosition(int position) { this.position = position; }

    @Override
    public String toString() {
        return file != null ? file.toString() : content.length + " char(s)";
    }

    public Location getLocation() {
        return locationOf(position);
    }

    public String extract(int begin, int end) {
        return new String(content, begin, end - begin);
    }
}
