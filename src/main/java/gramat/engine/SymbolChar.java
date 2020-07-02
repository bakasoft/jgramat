package gramat.engine;

public class SymbolChar extends Symbol {

    public final char value;

    public SymbolChar(char value) {
        this.value = value;
    }

    @Override
    public boolean matches(char chr) {
        return value == chr;
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
