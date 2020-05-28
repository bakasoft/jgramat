package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.units.RawMiniWild;
import gramat.parsing.Reader;

public class MiniWildParser {

    public static RawAutomaton parse(Reader reader) {
        if (!reader.pull('~')) {
            return null;
        }

        return new RawMiniWild();
    }

}
