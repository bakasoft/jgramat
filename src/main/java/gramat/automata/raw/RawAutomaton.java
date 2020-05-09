package gramat.automata.raw;

import gramat.automata.nondet.NAutomaton;
import gramat.automata.nondet.NLanguage;

abstract public class RawAutomaton {

    abstract public RawAutomaton collapse();

    abstract public NAutomaton build(NLanguage lang);

}