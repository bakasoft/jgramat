package gramat.pipeline.parsing;

import gramat.framework.Context;
import gramat.input.Tape;
import gramat.parsers.ParserSource;
import gramat.parsers.ValueParser;
import gramat.util.Args;
import gramat.util.Resources;

import java.util.List;
import java.util.Objects;

public class GramatParser implements SourceParser {

    private final Context ctx;
    private final Tape tape;
    private final ParserSource parsers;

    public GramatParser(Context ctx, Tape tape, ParserSource parsers) {
        this.ctx = ctx;
        this.tape = tape;
        this.parsers = parsers;
    }

    @Override
    public String loadValue(String valueDirective, List<Object> arguments) {
        if (Objects.equals(valueDirective, "readFile")) {
            var args = Args.of(arguments, List.of("path"));
            var path = args.getString("path");

            return Resources.loadText(path);
        }
        else {
            throw new RuntimeException("unsupported value directive: " + valueDirective);
        }
    }

    @Override
    public ValueParser findParser(String name) {
        return parsers.findParser(name);
    }

    @Override
    public Tape getTape() {
        return tape;
    }
}
