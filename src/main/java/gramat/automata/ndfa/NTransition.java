package gramat.automata.ndfa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NTransition {

    public final NState source;
    public final Symbol symbol;
    public final NState target;
    public final List<String> actions;

    public NTransition(NState source, Symbol symbol, NState target, List<String> actions) {
        this.source = source;
        this.symbol = symbol;
        this.target = target;
        this.actions = new ArrayList<>(actions);
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

        if (actions.size() > 0) {
            output.append("  //");
            output.append(String.join(" + ", actions));
        }
    }
}
