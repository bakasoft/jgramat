package gramat.source.model;

import gramat.source.formatting.SourceWriter;

public class MSymbolRange implements MSymbol {

    public Character start;
    public Character end;

    @Override
    public void write(SourceWriter writer) {
        writer.writeStringChar(start);
        writer.write('-');
        writer.writeStringChar(end);
    }
}
