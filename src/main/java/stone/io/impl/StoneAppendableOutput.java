package stone.io.impl;

import stone.io.StoneCharOutput;

import java.io.IOException;

public class StoneAppendableOutput implements StoneCharOutput {

    private final Appendable output;

    public StoneAppendableOutput(Appendable output) {
        this.output = output;
    }

    @Override
    public void write(char value) throws IOException {
        output.append(value);
    }

    @Override
    public void write(CharSequence value) throws IOException {
        output.append(value);
    }

    @Override
    public void space() throws IOException {
        // do nothing
    }

    @Override
    public void line() throws IOException {
        // do nothing
    }

    @Override
    public void indent(int delta) throws IOException {
        // do nothing
    }
}
