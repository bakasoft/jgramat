package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.units.RawSourceEnd;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

public class EndParser {

    public static RawAutomaton parse(Reader reader) {
        if (!reader.pull(Mark.END_SOURCE_MARK)) {
            return null;
        }

        return new RawSourceEnd();
    }

}
