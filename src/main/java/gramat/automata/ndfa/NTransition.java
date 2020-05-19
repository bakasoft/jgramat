package gramat.automata.ndfa;

public class NTransition {

    public final NState source;
    public final NState target;
    public final Symbol symbol;

    NTransition(NState source, NState target, Symbol symbol) {
        this.source = source;
        this.target = target;
        this.symbol = symbol;
    }

}
