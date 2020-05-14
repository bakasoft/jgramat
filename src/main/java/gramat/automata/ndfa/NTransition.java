package gramat.automata.ndfa;

import java.io.IOException;

public class NTransition {

    public final NState source;
    public final Symbol symbol;
    public final NState target;
    public final String action;

    public NTransition(NState source, Symbol symbol, NState target, String action) {
        this.source = source;
        this.symbol = symbol;
        this.target = target;
        this.action = action;
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

        if (action != null) {
            output.append("  //");
            output.append(action);
        }
    }
}
