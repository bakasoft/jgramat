import gramat.compiling.Compiler;
import org.junit.Test;

import java.nio.file.Paths;

public class MathTest {

    @Test
    public void parseGrammarTest() throws Exception {
        var parser = new Compiler();

        parser.parseFile(Paths.get(MathTest.class.getResource("/math.gm").toURI()));

        parser.compile();
    }

}
