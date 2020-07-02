package gramat.epsilon;

import gramat.util.parsing.Location;

import java.util.Objects;

public class Input {

    public static Input of(String text) {
        return new Input(text.toCharArray());
    }

    public static final char STX = '\u0002'; // Start of Text
    public static final char ETX = '\u0003'; // End of Text

    private final char[] content;
    private final int length;

    private int position;

    public Input(char[] content) {
        this.content = Objects.requireNonNull(content);
        this.length = content.length;
        this.position = -1;
    }

    public char peek() {
        return getChar(position);
    }

    public void move() {
        if (position < length) {
            position++;
        }
    }

    public boolean alive() {
        return position <= length;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    public char getChar(int index) {
        if (index < 0) {
            return STX;
        }
        else if (index < length) {
            return content[index];
        }
        else {
            return ETX;
        }
    }

    public String extract(int begin, int end) {
        return new String(content, begin, end - begin);
    }

    public Location getLocation() {
        return null; // TODO
    }
}
