package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.ParsingErrors;
import org.bakasoft.gramat.capturing.ObjectBuilder;

public class Context {
    protected final Tape tape;
    protected final ObjectBuilder builder;
    protected final ParsingErrors errors;

    public Context(Tape tape) {
        this(tape, new ObjectBuilder(), new ParsingErrors(tape));
    }

    public Context(Tape tape, ObjectBuilder builder, ParsingErrors errors) {
        this.tape = tape;
        this.builder = builder;
        this.errors = errors;
    }

    public Object getCapture() {
        return builder.pop();
    }
}
