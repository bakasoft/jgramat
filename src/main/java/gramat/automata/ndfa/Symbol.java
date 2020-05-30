package gramat.automata.ndfa;

public interface Symbol {

    boolean isWild();

    boolean isChar();

    boolean isRange();

    int getChar();

    int getBegin();

    int getEnd();
}
