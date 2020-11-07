package stone.io.impl;

import stone.io.StoneCharInput;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class ReadableInput implements StoneCharInput {

    private Readable source;

    private final Queue<Character> queue;

    private int column;
    private int line;

    public ReadableInput(Readable source) {
        this.source = source;
        this.queue = new LinkedList<>();
    }

    private boolean checkBuffer() throws IOException {
        if (source != null) {
            var buffer = CharBuffer.allocate(10);
            int length = source.read(buffer);

            if (length == 0) {
                source = null;
            }
            else {
                for (int i = 0; i < length; i++) {
                    queue.add(buffer.get(i));
                }
            }
        }

        return !queue.isEmpty();
    }

    @Override
    public boolean isAlive() {
        return !queue.isEmpty() || source != null;
    }

    @Override
    public char pull() throws IOException {
        if (checkBuffer()) {
            var c = queue.remove();

            if (c == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }

            return c;
        }
        return '\0';
    }

    @Override
    public char peek() throws IOException {
        if (checkBuffer()) {
            return queue.element();
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
