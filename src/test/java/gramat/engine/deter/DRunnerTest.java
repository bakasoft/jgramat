package gramat.engine.deter;

import gramat.Rejection;
import gramat.Parser;
import gramat.engine.Input;
import gramat.engine.checks.CheckSource;
import gramat.engine.indet.ILanguage;
import gramat.engine.symbols.SymbolSource;
import org.junit.Test;

public class DRunnerTest {

    @Test
    public void runner_test() throws Rejection {
        var lang = new ILanguage();
        var symbols = new SymbolSource();
        var checks = new CheckSource();

        var s0 = lang.createState("0", false);
        var s1 = lang.createState("1", false);
        var s2 = lang.createState("2", false);
        var s3 = lang.createState("3", false);
        var s4 = lang.createState("4", false);
        var s5 = lang.createState("5", false);
        var s6 = lang.createState("6", false);
        var s7 = lang.createState("7", true);

        lang.createTransition(s0, s1, symbols.getChar(Input.STX));

        lang.createTransition(s1, s2, symbols.getCheck(symbols.getChar('{'), checks.push("obj1")));
        lang.createTransition(s2, s3, symbols.getChar('a'));
        lang.createTransition(s3, s3, symbols.getChar('a'));
        lang.createTransition(s3, s4, symbols.getChar(':'));

        lang.createTransition(s4, s2, symbols.getCheck(symbols.getChar('{'), checks.push("obj2")));

        lang.createTransition(s4, s5, symbols.getChar('a'));
        lang.createTransition(s5, s5, symbols.getChar('a'));
        lang.createTransition(s5, s2, symbols.getChar(','));

        lang.createTransition(s2, s6, symbols.getCheck(symbols.getChar('}'), checks.pop("obj1")));
        lang.createTransition(s2, s5, symbols.getCheck(symbols.getChar('}'), checks.pop("obj2")));

        lang.createTransition(s5, s6, symbols.getCheck(symbols.getChar('}'), checks.pop("obj1")));
        lang.createTransition(s5, s5, symbols.getCheck(symbols.getChar('}'), checks.pop("obj2")));

        lang.createTransition(s6, s7, symbols.getChar(Input.ETX));

        var initial = lang.compile(s0);
        var parser = new Parser(initial);

        assert parser.eval("{}") == null;
        assert parser.eval("{aa:a}") == null;
        assert parser.eval("{a:a,aa:a}") == null;
        assert parser.eval("{a:{}}") == null;
        assert parser.eval("{a:{},aa:{}}") == null;
        assert parser.eval("{a:{a:a},a:{}}") == null;
        assert parser.eval("{a:{a:aa},a:{a:a}}") == null;
        assert parser.eval("{a:{aa:{}},a:{a:{}}}") == null;
    }

}

/*
-> 0
0 -> 1 : ^
1 -> 2 : "{"

2 -> 3 : [a-z]
3 -> 3 : [a-z]


3 -> 4 : "\:"

4 -> 2 : "{" / PUSH[1]

4 -> 5 : [a-z]
5 -> 5 : [a-z]


5 -> 2 : "\,"

2 -> 6 : "}" / EMPTY
2 -> 5 : "}" / POP[1]

5 -> 6 : "}" / EMPTY
5 -> 5 : "}" / POP[1]

6 => 7 : $
*/