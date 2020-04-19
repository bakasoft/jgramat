import gramat.compiling.Compiler;
import org.junit.Test;

import java.nio.file.Paths;

public class GramatTest {

    @Test
    public void parseGrammarTest() {
        try {
            var parser = new Compiler();

            parser.parseFile(Paths.get(GramatTest.class.getResource("/url.gmt").toURI()));

            parser.compile(false);
            parser.test();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
