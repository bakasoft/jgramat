package stone.io;

import java.io.IOException;

public interface StoneCharOutput {
    void write(char value) throws IOException;
    void write(CharSequence value) throws IOException;
    void space() throws IOException;
    void line() throws IOException;
    void indent(int delta) throws IOException;
}
