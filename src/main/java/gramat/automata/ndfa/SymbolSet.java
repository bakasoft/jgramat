package gramat.automata.ndfa;

import gramat.util.GramatWriter;
import gramat.util.parsing.Source;

import java.util.HashSet;
import java.util.Iterator;

public class SymbolSet implements Iterable<Symbol>{

    private final HashSet<Symbol> symbols;

    private SymbolWild wild;

    public SymbolSet() {
        symbols = new HashSet<>();
    }

    public Symbol getChar(int value) {
        var symbol = search_symbol(value);

        if (symbol != null) {
            return symbol;
        }

        symbol = new SymbolChar(value);
        symbols.add(symbol);
        return symbol;
    }

    public Symbol getRange(int begin, int end) {
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

    private Symbol search_symbol(int c) {
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

    private Symbol search_symbol(int begin, int end) {
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

        public final int value;

        public SymbolChar(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (value == Source.EOF) {
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
        public int getChar() {
            return value;
        }

        @Override
        public int getBegin() {
            return 0;
        }

        @Override
        public int getEnd() {
            return 0;
        }
    }

    public static class SymbolRange implements Symbol {

        public final int begin;
        public final int end;

        public SymbolRange(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public String toString() {
            return
                    "[" + GramatWriter.toDelimitedString(String.valueOf((char)begin), '\0') +
                            "-" + GramatWriter.toDelimitedString(String.valueOf((char)end), '\0') +
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
        public int getChar() {
            return 0;
        }

        @Override
        public int getBegin() {
            return begin;
        }

        @Override
        public int getEnd() {
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
        public int getChar() {
            return 0;
        }

        @Override
        public int getBegin() {
            return 0;
        }

        @Override
        public int getEnd() {
            return 0;
        }
    }

}
