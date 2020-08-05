package gramat.engine.deter;

import gramat.Rejection;
import gramat.Parser;
import gramat.engine.Input;
import gramat.engine.checks.CheckSource;
import gramat.engine.indet.ICompiler;
import gramat.engine.indet.ILanguage;
import gramat.engine.symbols.SymbolSource;
import org.junit.Test;

public class DRunnerTest {

    @Test
    public void runner_test() throws Rejection {
        var iLang = new ILanguage();
        var symbols = new SymbolSource();
        var checks = new CheckSource();

        var s0 = iLang.createState("0", false);
        var s1 = iLang.createState("1", false);
        var s2 = iLang.createState("2", false);
        var s3 = iLang.createState("3", false);
        var s4 = iLang.createState("4", false);
        var s5 = iLang.createState("5", false);
        var s6 = iLang.createState("6", false);
        var s7 = iLang.createState("7", true);

        iLang.createTransition(s0, s1, symbols.getChar(Input.STX));

        iLang.createTransition(s1, s2, symbols.getCheck(symbols.getChar('{'), checks.push("obj1")));
        iLang.createTransition(s2, s3, symbols.getChar('a'));
        iLang.createTransition(s3, s3, symbols.getChar('a'));
        iLang.createTransition(s3, s4, symbols.getChar(':'));

        iLang.createTransition(s4, s2, symbols.getCheck(symbols.getChar('{'), checks.push("obj2")));

        iLang.createTransition(s4, s5, symbols.getChar('a'));
        iLang.createTransition(s5, s5, symbols.getChar('a'));
        iLang.createTransition(s5, s2, symbols.getChar(','));

        iLang.createTransition(s2, s6, symbols.getCheck(symbols.getChar('}'), checks.pop("obj1")));
        iLang.createTransition(s2, s5, symbols.getCheck(symbols.getChar('}'), checks.pop("obj2")));

        iLang.createTransition(s5, s6, symbols.getCheck(symbols.getChar('}'), checks.pop("obj1")));
        iLang.createTransition(s5, s5, symbols.getCheck(symbols.getChar('}'), checks.pop("obj2")));

        iLang.createTransition(s6, s7, symbols.getChar(Input.ETX));

        var iCompiler = new ICompiler(iLang);
        var initial = iCompiler.compile(s0);
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