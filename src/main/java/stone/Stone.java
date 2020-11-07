package stone;

import stone.examiners.ExaminerRepository;
import stone.io.StoneCharInput;
import stone.io.StoneCharOutput;
import stone.io.impl.PrettyPrintOutput;
import stone.io.impl.ReadableInput;
import stone.io.impl.SequenceInput;
import stone.io.impl.StoneAppendableOutput;
import stone.text.StoneTextDecoder;
import stone.text.StoneTextEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.regex.Pattern;

public interface Stone {

    Pattern TOKEN_PATTERN = Pattern.compile("[a-zA-Z0-9_./+-]+");

    static StoneCharOutput standardOutput(Appendable output) {
        return new StoneAppendableOutput(output);
    }

    static StoneCharOutput prettyOutput(Appendable output) {
        return new PrettyPrintOutput(output);
    }

    static StoneTextEncoder standardEncoder(StoneSchema schema) {
        return standardEncoder(schema.createExaminerRepository());
    }

    static StoneTextEncoder standardEncoder(ExaminerRepository repository) {
        return new StoneTextEncoder(repository);
    }

    static StoneCharInput charInput(CharSequence sequence) {
        return new SequenceInput(sequence);
    }

    static StoneCharInput charInput(Readable readable) {
        return new ReadableInput(readable);
    }
}
