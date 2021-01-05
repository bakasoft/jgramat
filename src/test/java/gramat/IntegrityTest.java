package gramat;

import gramat.formatting.StateFormatter;
import gramat.pipeline.common.Format;
import org.junit.Assert;
import org.junit.Test;
import util.TestUtils;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;

public class IntegrityTest {

    @Test
    public void testIntegrity() throws IOException {
        for (var compiledFile : TestUtils.getResourcesFolder().searchFiles(file -> file.hasExtension("gmc"))) {
            var state0 = Format.read(compiledFile.getAbsolutePath());
            var data0 = new String(Files.readAllBytes(compiledFile.getAbsolutePath()));

            var data1 = new StringBuilder();
            Format.write(state0, data1);

            var state1 = Format.read(new StringReader(data1.toString()));

            System.out.println("------");
            new StateFormatter(System.out).write(state0);
            System.out.println("------");
            new StateFormatter(System.out).write(state1);
            System.out.println("------");
            System.out.println(data0);
            System.out.println("------");
            System.out.println(data1);
            System.out.println("------");

            Assert.assertEquals(data0, data1.toString());
        }
    }

}
