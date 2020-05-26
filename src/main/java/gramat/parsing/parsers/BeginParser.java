package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawSourceBegin;
import gramat.automata.raw.units.RawSourceEnd;
import gramat.parsers.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class BeginParser {

    public static RawAutomaton parse(Reader reader) {
        if (!reader.pull(Mark.BEGIN_SOURCE_MARK)) {
            return null;
        }

        return new RawSourceBegin();
    }

}
