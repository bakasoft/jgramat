package gramat.parsing.parsers;

import gramat.engine.Input;
import gramat.expressions.Expression;

import gramat.expressions.LiteralChar;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

public class BeginParser {

    public static Expression parse(Reader reader) {
        if (!reader.pull(Mark.BEGIN_SOURCE_MARK)) {
            return null;
        }

        return new LiteralChar(Input.STX);
    }

}
