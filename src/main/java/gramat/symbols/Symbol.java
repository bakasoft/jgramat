package gramat.symbols;

import java.io.PrintStream;

public interface Symbol {
    void printAmCode(PrintStream out);

    boolean test(char c);
}
