package gramat.automata.nondet;

public class NState {

    private final NLanguage language;
    private final int number;

    public NState(NLanguage language, int number) {
        this.language = language;
        this.number = number;
    }

    public NState linkEmpy(char value) {
        var target = language.state();
        linkEmpty(target);
        return target;
    }

    public void linkEmpty(NState target) {
        language._transition(this, new NSymbolEmpty(), target);
    }

    public void linkNot(char value, NState target) {
        language._transition(this, new NSymbolNotChar(value), target);
    }

    public NState linkNot(char value) {
        var target = language.state();
        linkNot(value, target);
        return target;
    }

    public void linkWild(NState target) {
        language._transition(this, new NSymbolWild(), target);
    }

    public NState linkChar(char value) {
        var target = language.state();
        linkChar(value, target);
        return target;
    }

    public void linkChar(char value, NState target) {
        language._transition(this, new NSymbolChar(value), target);
    }

    public NState linkRange(char begin, char end) {
        var target = language.state();
        linkRange(begin, end, target);
        return target;
    }

    public void linkRange(char begin, char end, NState target) {
        language._transition(this, new NSymbolRange(begin, end), target);
    }

    @Override
    public String toString() {
        return "S" + number;
    }
}
