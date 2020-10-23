package gramat;

import gramat.pipeline.Pipeline;
import org.junit.Assert;
import org.junit.Test;

import util.Resources;

public class GramatTest {

    private int runTests(String resource) {
        var tape = Resources.loadTape(resource);
        var gramat = new Gramat();
        var source = Pipeline.toSource(gramat, tape);

        return source.runTests(gramat);
    }

    @Test
    public void test03() {
        Assert.assertTrue(runTests("test03.gm") > 0);
    }

    @Test
    public void test04() {
        Assert.assertTrue(runTests("test04.gm") > 0);
    }

    @Test
    public void test06() {
        Assert.assertTrue(runTests("test06.gm") > 0);
    }

    @Test
    public void testJson() {
        Assert.assertTrue(runTests("testJson.gm") > 0);
    }

    @Test
    public void testWild() {
        Assert.assertTrue(runTests("testWild.gm") > 0);
    }

}
