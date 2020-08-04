package gramat.engine.symbols;

import gramat.engine.AmCode;
import gramat.engine.Input;
import gramat.engine.Runner;

public class SymbolChar extends Symbol {

    public final char value;

    public SymbolChar(char value) {
        this.value = value;
    }

    @Override
    public boolean matches(Runner runner) {
        return value == runner.getChar();
    }

    @Override
    public String toString() {
        if (value == Input.STX) {
            return "^";
        }
        else if (value == Input.ETX) {
            return "$";
        }

        return "\"" + AmCode.escape(String.valueOf(value)) + "\"";
    }
}
