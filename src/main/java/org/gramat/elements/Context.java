package org.gramat.elements;

import org.gramat.Tape;
import org.gramat.ParsingErrors;
import org.gramat.capturing.TextBuilder;
import org.gramat.capturing.ObjectBuilder;

public class Context {
    protected final Tape tape;
    protected final ObjectBuilder builder;
    protected final TextBuilder capture;
    protected final ParsingErrors errors;

    public Context(Tape tape) {
        this(tape, new ObjectBuilder(), new TextBuilder(), new ParsingErrors(tape));
    }

    public Context(Tape tape, ObjectBuilder builder, TextBuilder capture, ParsingErrors errors) {
        this.tape = tape;
        this.builder = builder;
        this.capture = capture;
        this.errors = errors;
    }

    public Object getCapture() {
        return builder.pop();
    }
}
