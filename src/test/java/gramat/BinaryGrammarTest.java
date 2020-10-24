package gramat;

import gramat.input.Tape;
import gramat.pipeline.Pipeline;
import gramat.machine.binary.StateSerializer;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BinaryGrammarTest {

    @Test
    public void test() throws IOException {
        var gramat = new Gramat();
        var state = Pipeline.toState(gramat, new Tape("\"[\" { {+'0-9'} / \",\" } \"]\""));
        var output = new ByteArrayOutputStream();

        StateSerializer.write(state, output);

        System.out.println(output);
    }

}
