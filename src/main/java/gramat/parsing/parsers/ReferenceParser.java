package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.parsers.Mark;
import gramat.parsing.Parser;
import gramat.parsing.RawAutomataPromise;
import gramat.parsing.Reader;

public class ReferenceParser {

    public static RawAutomaton parse(Parser parser, Reader reader) {
        return reader.transaction(() -> {
            var name = reader.readKeyword();

            if (name == null) {
                return null;
            }

            reader.skipBlanks();

            if (reader.pull(Mark.HARD_ASSIGNMENT_MARK)) {
                return null;
            }

            return new RawAutomataPromise(parser, name);
        });
    }

}
