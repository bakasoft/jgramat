package gramat.parsing.parsers;

import gramat.parsers.BaseParsers;
import gramat.parsers.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;
import gramat.util.parsing.Source;

import java.nio.file.Paths;

public class ImportParser {

    public static boolean parse(Parser parser, Reader reader) {
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
                throw reader.error("Expected file path string.");
            }

            var file = Paths.get(path);

            parser.parse(file);

            return true;
        });
    }

}
