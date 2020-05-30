package gramat.automata.ndfa;

public class NAutomaton extends NSegment {

    public final String name;

    public NAutomaton(NLanguage language, String name, NState initial, NState accepted) {
        super(language, initial, accepted);
        this.name = name;
    }

}
