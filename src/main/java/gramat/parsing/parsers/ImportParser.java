package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

import java.nio.file.Path;

public class ImportParser {

    public static boolean parse(Grammar grammar, Reader reader) {
        return reader.transaction(() -> {
            if (!reader.pull('@')) {
                return false;
            }

            var keyword = reader.readKeyword();

            if (!"import".equals(keyword)) {
                return false;
            }

            reader.skipBlanks();

            String path = reader.readString(Mark.TOKEN_DELIMITER);

            if (path == null) {
                throw new TextException("Expected file path string.", reader.getLocation());
            }

            var file = grammar.resolveFile(Path.of(path));

            grammar.parse(file);

            return true;
        });
    }

}
