package gramat.automata.raw;

import gramat.automata.nondet.NLanguage;
import gramat.automata.nondet.NState;

abstract public class RawAutomaton {

    abstract public RawAutomaton collapse();

    abstract public NState build(NLanguage lang, NState start);

}