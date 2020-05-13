package gramat.output;

import java.io.IOException;

public interface Writable {

    void write(Appendable output) throws IOException;

    default String captureOutput() {
        var output = new StringBuilder();

        try {
            write(output);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }

        return output.toString();
    }

}
