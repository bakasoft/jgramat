package gramat.automata.ndfa;

import gramat.eval.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NTransition {

    public final NState source;
    public final Symbol symbol;
    public final NState target;

    public final List<Action> actions;

    public NTransition(NState source, Symbol symbol, NState target) {
        this.source = source;
        this.symbol = symbol;
        this.target = target;
        this.actions = new ArrayList<>();
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
