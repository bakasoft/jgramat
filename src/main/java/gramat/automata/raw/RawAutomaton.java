package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;

abstract public class RawAutomaton {

    abstract public RawAutomaton collapse();

    abstract public NAutomaton build(Language lang);

}