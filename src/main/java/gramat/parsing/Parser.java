package gramat.parsing;

import gramat.input.Tape;
import gramat.parsers.ParserSource;
import gramat.util.Args;
import gramat.util.Resources;

import java.util.List;
import java.util.Objects;

public class Parser {

    public final Tape tape;
    public final ParserSource parsers;

    public Parser(Tape tape, ParserSource parsers) {
        this.tape = tape;
        this.parsers = parsers;
    }

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

}
