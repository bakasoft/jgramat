package gramat.engine;

import gramat.common.TextSource;

public class Input extends TextSource {

    public static final char STX = '\u0002'; // Start of Text
    public static final char ETX = '\u0003'; // End of Text

    public Input(String content) {
        super(content);
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

    public boolean consumed() {
        return position >= length;
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

}
