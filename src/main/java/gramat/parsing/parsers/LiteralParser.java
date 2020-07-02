package gramat.parsing.parsers;

import gramat.GramatException;
import gramat.expressions.Expression;
import gramat.expressions.LiteralChar;
import gramat.expressions.LiteralString;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

public class LiteralParser {

    public static Expression parse(Reader reader) {
        var value = reader.readString(Mark.LITERAL_DELIMITER);

        if (value == null) {
            return null;
        }

        if (value.isEmpty()) {
            throw new GramatException("cannot be empty");
        }
        else if (value.length() == 1) {
            return new LiteralChar(value.charAt(0));
        }

        return new LiteralString(value);
    }

}
