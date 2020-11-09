package gramat;

import gramat.scheme.core.badges.BadgeSource;
import gramat.framework.Configuration;
import gramat.parsers.ParserSource;
import gramat.scheme.core.symbols.Alphabet;

public class Gramat {

    public final Configuration config;
    public final ParserSource parsers;
    public final Alphabet symbols;
    public final BadgeSource badges;

    public Gramat() {
        this(null);
    }

    public Gramat(ParserSource parsers) {
        this(parsers, null, null);
    }

    public Gramat(ParserSource parsers, Alphabet symbols) {
        this(parsers, symbols, null);
    }

    public Gramat(ParserSource parsers, Alphabet symbols, BadgeSource badges) {
        this.config = new Configuration();

        config.registerAll(GramatConfig.values());

        this.parsers = parsers != null ? parsers : new ParserSource();
        this.symbols = symbols != null ? symbols : new Alphabet();
        this.badges = badges != null ? badges : new BadgeSource();
    }

}
