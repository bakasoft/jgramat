package gramat.source.model;

import gramat.source.formatting.SourceWriter;

import java.util.List;

public class MSymbolUnion implements MSymbol {

    public List<MSymbol> symbols;

    @Override
    public void write(SourceWriter writer) {
        for (int i = 0; i < symbols.size(); i++) {
            if (i > 0) {
                writer.write(' ');
            }
            symbols.get(i).write(writer);
        }
    }
}
