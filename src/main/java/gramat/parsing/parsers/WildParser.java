package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.units.RawWildAutomaton;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

public class WildParser {

    public static RawAutomaton parse(Reader reader) {
        if (!reader.pull(Mark.WILD_MARK)) {
            return null;
        }

        return new RawWildAutomaton();
    }

}
