import gramat.compiling.Compiler;
import gramat.parsing.Options;
import gramat.parsing.Parser;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class GramatTest {

    @Test
    public void parseGrammarTest() throws Exception {
        var parser = new Parser();

        parser.parse(Paths.get(MathTest.class.getResource("/gramat.gm").toURI()));

        parser.runTests();
    }

}
