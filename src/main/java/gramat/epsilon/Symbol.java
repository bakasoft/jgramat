package gramat.epsilon;

public interface Symbol {

    boolean isWild();

    boolean isChar();

    boolean isRange();

    boolean isEmpty();

    char getChar();

    char getBegin();

    char getEnd();
}
