package gramat;

import gramat.badges.BadgeSource;
import gramat.framework.Configuration;
import gramat.framework.Context;
import gramat.framework.StandardContext;
import gramat.parsers.ParserSource;
import gramat.symbols.Alphabet;
import gramat.util.Args;
import gramat.util.Resources;

import java.util.List;
import java.util.Objects;

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
