package gramat.engine.nodet;

import gramat.engine.Symbol;
import gramat.engine.stack.ControlCheck;

public class NTransition {

    public NState source;
    public NState target;
    public Symbol symbol;
    public ControlCheck check;

    public NTransition(NState source, NState target, Symbol symbol, ControlCheck check) {
        this.source = source;
        this.target = target;
        this.symbol = symbol;
        this.check = check;
    }
}
