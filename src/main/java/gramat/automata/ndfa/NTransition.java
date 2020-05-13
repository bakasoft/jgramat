package gramat.automata.ndfa;

import java.io.IOException;

public class NTransition {

    public final NState source;
    public final Symbol symbol;
    public final NState target;

    public NTransition(NState source, Symbol symbol, NState target) {
        this.source = source;
        this.symbol = symbol;
        this.target = target;
    }

    public void write(Appendable output) throws IOException {
        source.write(output);
        output.append(" -> ");
        target.write(output);
        output.append(": ");
        if (symbol == null) {
            output.append('\u03B5');
        }
        else {
            symbol.write(output);
        }

    }
}
