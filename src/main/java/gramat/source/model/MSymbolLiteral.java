package gramat.source.model;

import gramat.source.formatting.SourceWriter;

public class MSymbolLiteral implements MSymbol {

    public Character value;

    @Override
    public void write(SourceWriter writer) {
        writer.writeStringChar(value);
    }
}
