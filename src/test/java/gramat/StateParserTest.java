package gramat;

import org.junit.Test;

import util.Resources;

import gramat.parsing.StateParser;

public class StateParserTest {

    @Test
    public void test0() {
        var tape = Resources.loadTape("test.am");
        var gramat = new Gramat();
        var parser = new StateParser(gramat);
        var node = parser.parse(tape, true);

        assert node != null;
    }

    @Test
    public void test1() {
        var tape = Resources.loadTape("test-02.am");
        var gramat = new Gramat();
        var parser = new StateParser(gramat);
        var node = parser.parse(tape, true);

        assert node != null;
    }

}
