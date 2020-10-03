package gramat;

import gramat.compilers.ExpressionCompiler;
import org.junit.Test;

import util.Resources;

public class GramatTest {

    @Test
    public void test_03() {
        var tape = Resources.loadTape("test_03.gm");
        var gramat = new Gramat();
        var compiler = new ExpressionCompiler(gramat);

        compiler.compile(tape);
    }

}
