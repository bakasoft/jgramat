import bm.parsing.BmParser;
import bm.parsing.BmTerminal;
import org.junit.Test;

import java.nio.file.Path;

public class BmTest {

    @Test
    public void bmTest() {
        var terminal = new BmTerminal();
        var parser = new BmParser(terminal);
        var suite = parser.parseSuite(Path.of("../../bm/bm-core"));

        System.out.println(suite);
    }

}
