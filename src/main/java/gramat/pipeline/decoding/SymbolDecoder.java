package gramat.pipeline.decoding;

import gramat.models.automata.ModelSymbol;
import gramat.models.automata.ModelSymbolChar;
import gramat.models.automata.ModelSymbolRange;
import gramat.models.automata.ModelSymbolWild;
import gramat.symbols.Alphabet;
import gramat.symbols.Symbol;

public class SymbolDecoder {

    private final Alphabet alphabet;

    public SymbolDecoder() {
        alphabet = new Alphabet();
    }

    public Symbol build(ModelSymbol model) {
        if (model instanceof ModelSymbolWild) {
            return alphabet.wild();
        }
        else if (model instanceof ModelSymbolChar) {
            var s = (ModelSymbolChar)model;
            return alphabet.character(s.value);
        }
        else if (model instanceof ModelSymbolRange) {
            var s = (ModelSymbolRange)model;
            return alphabet.range(s.begin, s.end);
        }
        else {
            throw new RuntimeException();
        }
    }

}
