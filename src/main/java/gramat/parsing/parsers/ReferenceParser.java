package gramat.parsing.parsers;

import gramat.expressions.Expression;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.expressions.Reference;
import gramat.parsing.Reader;

public class ReferenceParser {

    public static Expression parse(Grammar grammar, Reader reader) {
        return reader.transaction(() -> {
            var name = reader.readKeyword();

            if (name == null) {
                return null;
            }

            reader.skipBlanks();

            if (reader.pull(Mark.HARD_ASSIGNMENT_MARK)) {
                return null;
            }

            return new Reference(grammar, name);
        });
    }

}
