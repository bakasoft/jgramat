package gramat.parsing.parsers;

import gramat.compiling.GrammarTest;
import gramat.parsing.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;
import gramat.parsing.test.TestValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestParser {

    private static String parseInput(Parser parser, Reader reader) {
        return reader.transaction(() -> {
            if (reader.pull('@')) {
                var keyword = reader.readKeyword();

                if (!"load".equals(keyword)) {
                    throw reader.error("not supported keyword: " + keyword);
                }

                reader.skipBlanks();

                if (!reader.pull(Mark.GROUP_BEGIN)) {
                    throw reader.error("expected group begin");
                }

                reader.skipBlanks();;

                var path = reader.readString(Mark.TOKEN_DELIMITER);

                if (path == null) {
                    throw reader.error("Expected file path");
                }

                var file = parser.resolveFile(Path.of(path));
                String input;

                try {
                    input = Files.readString(file);
                } catch (IOException e) {
                    throw reader.error(e.toString());
                }

                reader.skipBlanks();

                if (!reader.pull(Mark.GROUP_END)) {
                    throw reader.error("expected group end");
                }

                return input;
            }

            return reader.readString(Mark.TOKEN_DELIMITER);
        });
    }

    public static boolean parse(Parser parser, Reader reader) {
        return reader.transaction(() -> {
            TestValue expectedValue = null;
            boolean expectedMatch;

            if (!reader.pull('@')) {
                return false;
            }

            var keyword = reader.readKeyword();

            if ("pass".equals(keyword)) {
                expectedMatch = true;
            }
            else if ("fail".equals(keyword)) {
                expectedMatch = false;
            }
            else if ("match".equals(keyword)) {
                expectedMatch = true;
            }
            else {
                return false;
            }

            reader.skipBlanks();

            var exprName = reader.readKeyword();

            reader.skipBlanks();

            var input = parseInput(parser, reader);

            if ("match".equals(keyword)) {
                reader.skipBlanks();

                expectedValue = TestValueParser.parse(reader);
            }

            var test = new GrammarTest(exprName, input, expectedMatch, expectedValue);

            parser.addTest(test);

            return true;
        });
    }

}
