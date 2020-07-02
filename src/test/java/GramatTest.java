import gramat.parsing.Parser;
import org.junit.Test;

import java.nio.file.Paths;

public class GramatTest {

    @Test
    public void parseGrammarTest() throws Exception {
        var parser = new Parser();

        parser.parse(Paths.get(GramatTest.class.getResource("/gramat.gm").toURI()));

        parser.runTests();
    }

}
