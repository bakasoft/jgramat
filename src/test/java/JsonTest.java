import gramat.parsing.Parser;
import org.junit.Test;

import java.nio.file.Paths;

public class JsonTest {

    @Test
    public void parseGrammarTest() throws Exception {
        var parser = new Parser();

        parser.parse(Paths.get(JsonTest.class.getResource("/json/test.gm").toURI()));

        parser.runTests();
    }

}
