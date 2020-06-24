package gramat.epsilon;

import gramat.util.GramatWriter;
import gramat.util.parsing.Source;

import java.util.HashSet;
import java.util.Iterator;

public class SymbolSet implements Iterable<Symbol>{

    private final HashSet<Symbol> symbols;

    private SymbolWild wild;
    private SymbolEmpty empty;

    public SymbolSet() {
        symbols = new HashSet<>();
    }

    public Symbol getChar(char value) {
        var symbol = search_symbol(value);

        if (symbol != null) {
            return symbol;
        }

        symbol = new SymbolChar(value);
        symbols.add(symbol);
        return symbol;
    }

    public Symbol getRange(char begin, char end) {
        var symbol = search_symbol(begin, end);

        if (symbol != null) {
            return symbol;
        }

        symbol = new SymbolRange(begin, end);
        symbols.add(symbol);
        return symbol;
    }

    public Symbol getWild() {
        if (wild == null) {
            wild = new SymbolWild();
            symbols.add(wild);
        }
        return wild;
    }

    public Symbol getEmpty() {
        if (empty == null) {
            empty = new SymbolEmpty();
            symbols.add(empty);
        }
        return wild;
    }

    private Symbol search_symbol(char c) {
        for (var symbol : symbols) {
            if (symbol instanceof SymbolChar) {
                var sc = (SymbolChar)symbol;

                if (sc.value == c) {
                    return sc;
                }
            }
        }

        return null;
    }

    private Symbol search_symbol(char begin, char end) {
        if (begin == end) {
            return search_symbol(begin);
        }

        for (var symbol : symbols) {
            if (symbol instanceof SymbolRange) {
                var sr = (SymbolRange)symbol;

                if (sr.begin == begin && sr.end == end) {
                    return sr;
                }
            }
        }

        return null;
    }

    @Override
    public Iterator<Symbol> iterator() {
        return symbols.iterator();
    }

    public static class SymbolChar implements Symbol {

        public final char value;

        public SymbolChar(char value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (value == Input.STX) {
                return "^";
            }
            else if (value == Input.ETX) {
                return "$";
            }
            return GramatWriter.toDelimitedString(String.valueOf((char)value), '\"');
        }

        @Override
        public boolean isWild() {
            return false;
        }

        @Override
        public boolean isChar() {
            return true;
        }

        @Override
        public boolean isRange() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public char getChar() {
            return value;
        }

        @Override
        public char getBegin() {
            return 0;
        }

        @Override
        public char getEnd() {
            return 0;
        }
    }

    public static class SymbolRange implements Symbol {

        public final char begin;
        public final char end;

        public SymbolRange(char begin, char end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public String toString() {
            return
                    "[" + GramatWriter.toDelimitedString(String.valueOf(begin), '\0') +
                            "-" + GramatWriter.toDelimitedString(String.valueOf(end), '\0') +
                            "]";
        }

        @Override
        public boolean isWild() {
            return false;
        }

        @Override
        public boolean isChar() {
            return false;
        }

        @Override
        public boolean isRange() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public char getChar() {
            return 0;
        }

        @Override
        public char getBegin() {
            return begin;
        }

        @Override
        public char getEnd() {
            return end;
        }
    }

    private static class SymbolWild implements Symbol {
        @Override
        public String toString() {
            return "*";
        }

        @Override
        public boolean isWild() {
            return true;
        }

        @Override
        public boolean isChar() {
            return false;
        }

        @Override
        public boolean isRange() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public char getChar() {
            return 0;
        }

        @Override
        public char getBegin() {
            return 0;
        }

        @Override
        public char getEnd() {
            return 0;
        }
    }

    private static class SymbolEmpty implements Symbol {
        @Override
        public String toString() {
            return "ε";
        }

        @Override
        public boolean isWild() {
            return false;
        }

        @Override
        public boolean isChar() {
            return false;
        }

        @Override
        public boolean isRange() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public char getChar() {
            return 0;
        }

        @Override
        public char getBegin() {
            return 0;
        }

        @Override
        public char getEnd() {
            return 0;
        }
    }

}
