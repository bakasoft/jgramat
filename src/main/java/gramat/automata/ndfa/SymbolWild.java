package gramat.automata.ndfa;

import java.io.IOException;

public class SymbolWild extends Symbol {
    @Override
    public void write(Appendable output) throws IOException {
        output.append("*");
    }
}
