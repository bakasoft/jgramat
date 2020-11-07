package stone.io;

import gramat.util.PP;

import java.io.IOException;

public interface StoneCharInput {
    boolean isAlive() throws IOException;
    char pull() throws IOException;
    char peek() throws IOException;

    int getLine();
    int getColumn();

    default boolean peek(char c) throws IOException{
        return peek() == c;
    }

    default boolean tryPull(char c) throws IOException {
        if (peek() == c) {
            pull();
            return true;
        }
        return false;
    }

    default void expect(char expected) throws IOException {
        var actual = peek();
        if (actual != expected) {
            throw new RuntimeException("Expected char " + PP.str(expected) + " instead of " + PP.str(actual) + " at " + getLine() + ":" + getColumn());
        }
        pull();
    }
}
