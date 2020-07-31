package gramat.engine.deter;

import gramat.Rejection;
import gramat.Parser;
import gramat.engine.Input;
import org.junit.Test;

public class DRunnerTest {

    @Test
    public void runner_test() throws Rejection {
        var builder = new DBuilder();

        builder.accept(7);

        builder.transition(0, 1, Input.STX);

        builder.transition(1, 2, '{');
        builder.transition(2, 3, 'a');
        builder.transition(3, 3, 'a');
        builder.transition(3, 4, ':');

        builder.transition(4, 2, '{', builder.checkSource.push("obj"));

        builder.transition(4, 5, 'a');
        builder.transition(5, 5, 'a');
        builder.transition(5, 2, ',');

        builder.transition(2, 6, '}', builder.checkSource.getClear());
        builder.transition(2, 5, '}', builder.checkSource.pop("obj"));

        builder.transition(5, 6, '}', builder.checkSource.getClear());
        builder.transition(5, 5, '}', builder.checkSource.pop("obj"));

        builder.transition(6, 7, Input.ETX);

        var parser = new Parser(builder.getState(0));

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