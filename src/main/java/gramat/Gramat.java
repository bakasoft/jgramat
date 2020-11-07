package gramat;

import gramat.badges.BadgeSource;
import gramat.framework.Component;
import gramat.framework.Configuration;
import gramat.framework.Logger;
import gramat.framework.SystemLogger;
import gramat.parsers.ParserSource;
import gramat.symbols.Alphabet;
import gramat.util.Args;
import gramat.util.Resources;

import java.util.List;
import java.util.Objects;

public class Gramat implements Component {

    public final Configuration config;

    public final ParserSource parsers;
    public final Alphabet symbols;
    public final BadgeSource badges;

    private final Logger logger;

    public Gramat() {
        this(null);
    }

    public Gramat(ParserSource parsers) {
        this(parsers, null, null, null);
    }

    public Gramat(ParserSource parsers, Alphabet symbols) {
        this(parsers, symbols, null, null);
    }

    public Gramat(ParserSource parsers, Alphabet symbols, BadgeSource badges) {
        this(parsers, symbols, badges, null);
    }

    public Gramat(ParserSource parsers, Alphabet symbols, BadgeSource badges, Logger logger) {
        this.config = new Configuration();

        config.registerAll(GramatConfig.values());

        this.parsers = parsers != null ? parsers : new ParserSource();
        this.symbols = symbols != null ? symbols : new Alphabet();
        this.badges = badges != null ? badges : new BadgeSource();
        this.logger = logger != null ? logger : new SystemLogger(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Gramat getGramat() {
        return this;
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
