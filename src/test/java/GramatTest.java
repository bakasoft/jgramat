import gramat.compiling.Compiler;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class GramatTest {

    @Test
    public void parseGrammarTest() throws Exception {
        var parser = new Compiler();

        parser.parseFile(Paths.get(GramatTest.class.getResource("/json/test.gmt").toURI()));

        parser.compile(false);
    }

}
