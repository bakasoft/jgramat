package gramat.automata.ndfa;

public class NAutomaton {

    public final String name;
    public final NState initial;
    public final NStateSet accepted;

    public NAutomaton(String name, NState initial, NStateSet accepted) {
        this.name = name;
        this.initial = initial;
        this.accepted = accepted;
    }

}
