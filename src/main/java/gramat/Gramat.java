package gramat;

import gramat.framework.Component;
import gramat.framework.Configuration;
import gramat.framework.Logger;
import gramat.framework.SystemLogger;
import gramat.parsers.ParserSource;
import gramat.symbols.Alphabet;

public class Gramat implements Component {

    public final Configuration config;

    public final ParserSource parsers;
    public final Alphabet symbols;

    private final Logger logger;

    public Gramat() {
        this(null);
    }

    public Gramat(ParserSource parsers) {
        this(parsers, null, null);
    }

    public Gramat(ParserSource parsers, Alphabet symbols) {
        this(parsers, symbols, null);
    }

    public Gramat(ParserSource parsers, Alphabet symbols, Logger logger) {
        this.config = new Configuration();

        config.registerAll(GramatConfig.values());

        this.parsers = parsers != null ? parsers : new ParserSource();
        this.symbols = symbols != null ? symbols : new Alphabet();
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

}
