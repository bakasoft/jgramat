package stone.io.impl;

import stone.io.StoneCharOutput;

import java.io.IOException;

public class PrettyPrintOutput implements StoneCharOutput {

    private final Appendable output;

    private int tabs;

    public PrettyPrintOutput(Appendable output) {
        this.output = output;
        this.tabs = 0;
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
        output.append(' ');
    }

    @Override
    public void line() throws IOException {
        output.append('\n');

        output.append("  ".repeat(tabs));
    }

    @Override
    public void indent(int delta) throws IOException {
        tabs += delta;
    }
}
