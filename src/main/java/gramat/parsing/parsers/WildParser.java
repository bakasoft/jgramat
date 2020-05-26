package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawWildAutomaton;
import gramat.automata.raw.units.RawSourceEnd;
import gramat.parsers.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class WildParser {

    public static RawAutomaton parse(Reader reader) {
        if (!reader.pull(Mark.WILD_MARK)) {
            return null;
        }

        return new RawWildAutomaton();
    }

}
