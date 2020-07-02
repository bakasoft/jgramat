package gramat;

import org.junit.Test;

import java.nio.file.Paths;

public class GramatTest {

    @Test
    public void parse_test() throws Exception {
        var grammar = new Grammar();

        grammar.parse(Paths.get(GramatTest.class.getResource("/gramat.gm").toURI()));
    }

}
