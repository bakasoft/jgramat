package gramat.symbols;

import java.util.Objects;

public class SymbolRange implements Symbol {
    public final char start;
    public final char end;

    public SymbolRange(char start, char end) {
        this.start = start;
        this.end = end;
    }

    public char getStart() {
        return start;
    }

    public char getEnd() {
        return end;
    }

    @Override
    public boolean matches(char value) {
        return value >= start && value <= end;
    }

    @Override
    public boolean stacks(Symbol symbol) {
        if (symbol instanceof SymbolRange) {
            var range = (SymbolRange)symbol;

            return this.start == range.start && this.end == range.end;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "SymbolRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
