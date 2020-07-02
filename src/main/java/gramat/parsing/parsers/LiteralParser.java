package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.units.RawLiteralAutomaton;
import gramat.expressions.flat.CharAutomaton;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

public class LiteralParser {

    public static RawAutomaton parse(Reader reader) {
        var value = reader.readString(Mark.LITERAL_DELIMITER);

        if (value == null) {
            return null;
        }

        return new RawLiteralAutomaton(value);
    }

}
