package gramat.epsilon;

import java.util.Objects;

public class Input {

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
        if (position < 0) {
            return STX;
        }
        else if (position < length) {
            return content[position];
        }
        else {
            return ETX;
        }
    }

    public void move() {
        if (position < length) {
            position++;
        }
    }

    public boolean alive() {
        return position <= length;
    }
}
