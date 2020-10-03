package gramat;

import gramat.pipeline.Step0Compiler;
import org.junit.Test;

import util.Resources;

public class GramatTest {

    @Test
    public void test_03() {
        var tape = Resources.loadTape("test_03.gm");
        var gramat = new Gramat();
        var compiler = new Step0Compiler(gramat);

        compiler.compile(tape);
    }

}
