package gramat;

import gramat.compilers.ExpressionCompiler;
import org.junit.Test;

import util.Resources;

public class GramatTest {

    @Test
    public void test03() {
        var tape = Resources.loadTape("test_03.gm");
        var gramat = new Gramat();
        var compiler = new ExpressionCompiler(gramat);

        compiler.compile(tape);
    }

    @Test
    public void test04() {
        var tape = Resources.loadTape("test_04.gm");
        var gramat = new Gramat();
        var compiler = new ExpressionCompiler(gramat);

        compiler.compile(tape);
    }

}
