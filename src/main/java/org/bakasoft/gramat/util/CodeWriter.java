package org.bakasoft.gramat.util;

import java.io.IOException;
import java.io.Writer;

public class CodeWriter {

    private final StringBuilder output;
    private final String newLine;
    private final String tab;

    private int indentation;

    private boolean breakLine;

    public CodeWriter() {
        output = new StringBuilder();
        newLine = System.lineSeparator();
        tab = "  ";
    }

    public void indent(int delta) {
        indentation += delta;
    }

    public void breakLine() {
        if (breakLine) {
            output.append(newLine);
        }
        else {
            breakLine = true;
        }
    }

    public void write(char c) {
        if (breakLine) {
            breakLine = false;
            output.append(newLine);

            for (int i = 0; i < indentation; i++) {
                output.append(tab);
            }
        }

        output.append(c);
    }

    public void write(CharSequence code) {
        if (code != null) {
            for (int i = 0; i < code.length(); i++) {
                write(code.charAt(i));
            }
        }
    }

    public String getOutput() {
        return output.toString();
    }

    @Override
    public String toString() {
        return output.toString();
    }
}
