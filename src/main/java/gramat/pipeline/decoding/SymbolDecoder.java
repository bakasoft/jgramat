package gramat.pipeline.decoding;

import gramat.scheme.data.automata.SymbolData;
import gramat.scheme.data.automata.SymbolCharData;
import gramat.scheme.data.automata.SymbolRangeData;
import gramat.scheme.data.automata.SymbolWildData;
import gramat.scheme.common.symbols.Alphabet;
import gramat.scheme.common.symbols.Symbol;

public class SymbolDecoder {

    private final Alphabet alphabet;

    public SymbolDecoder() {
        alphabet = new Alphabet();
    }

    public Symbol build(SymbolData data) {
        if (data instanceof SymbolWildData) {
            return alphabet.wild();
        }
        else if (data instanceof SymbolCharData) {
            var s = (SymbolCharData)data;
            return alphabet.character(s.value);
        }
        else if (data instanceof SymbolRangeData) {
            var s = (SymbolRangeData)data;
            return alphabet.range(s.begin, s.end);
        }
        else {
            throw new RuntimeException();
        }
    }

}
