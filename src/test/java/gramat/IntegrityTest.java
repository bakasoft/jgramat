package gramat;

import stone.binary.formatter.JsonFormatter;
import gramat.formatting.StateFormatter;
import gramat.machine.binary.Format;
import org.junit.Assert;
import org.junit.Test;
import util.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;

public class IntegrityTest {

    @Test
    public void testIntegrity() throws IOException {
        for (var compiledFile : TestUtils.getResourceFiles(path -> path.toString().endsWith(".gmc"))) {
            var state0 = Format.read(compiledFile);
            var data0 = new String(Files.readAllBytes(compiledFile));

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
