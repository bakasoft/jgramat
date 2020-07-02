package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;
import gramat.parsing.test.TestValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestParser {

    private static String parseInput(Grammar grammar, Reader reader) {
        return reader.transaction(() -> {
            if (reader.pull('@')) {
                var keyword = reader.readKeyword();

                if (!"load".equals(keyword)) {
                    throw new TextException("not supported keyword: " + keyword, reader.getLocation());
                }

                reader.skipBlanks();

                if (!reader.pull(Mark.GROUP_BEGIN)) {
                    throw new TextException("expected group begin", reader.getLocation());
                }

                reader.skipBlanks();;

                var path = reader.readString(Mark.TOKEN_DELIMITER);

                if (path == null) {
                    throw new TextException("Expected file path", reader.getLocation());
                }

                var file = grammar.resolveFile(Path.of(path));
                String input;

                try {
                    input = Files.readString(file);
                } catch (IOException e) {
                    throw new TextException(e.toString(), reader.getLocation());
                }

                reader.skipBlanks();

                if (!reader.pull(Mark.GROUP_END)) {
                    throw new TextException("expected group end", reader.getLocation());
                }

                return input;
            }

            return reader.readString(Mark.TOKEN_DELIMITER);
        });
    }

    public static boolean parse(Grammar grammar, Reader reader) {
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

            var input = parseInput(grammar, reader);

            if ("match".equals(keyword)) {
                reader.skipBlanks();

                expectedValue = TestValueParser.parse(reader);
            }

            grammar.runTest(exprName, input, expectedMatch, expectedValue);

            return true;
        });
    }

}
