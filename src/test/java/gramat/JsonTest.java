package gramat;

import org.junit.Test;

import java.nio.file.Paths;

public class JsonTest {

    @Test
    public void parse_test() throws Exception {
        var grammar = new Grammar();

        grammar.parse(Paths.get(JsonTest.class.getResource("/json.gm").toURI()));
    }

}
