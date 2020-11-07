package stone.io.impl;

import stone.io.StoneCharInput;

public class SequenceInput implements StoneCharInput {

    private final CharSequence sequence;

    private int position;
    private int line;
    private int column;

    public SequenceInput(CharSequence sequence) {
        this.sequence = sequence;
        this.position = 0;
    }

    @Override
    public boolean isAlive() {
        return position < sequence.length();
    }

    @Override
    public char pull() {
        if (position < sequence.length()) {
            var c = sequence.charAt(position);

            if (c == '\n') {
                line++;
                column = 0;
            }
            else {
                column++;
            }

            position++;

            return c;
        }

        return '\0';
    }

    @Override
    public char peek() {
        if (position < sequence.length()) {
            return sequence.charAt(position);
        }

        return '\0';
    }

    @Override
    public int getLine() {
        return line + 1;
    }

    @Override
    public int getColumn() {
        return column + 1;
    }
}
