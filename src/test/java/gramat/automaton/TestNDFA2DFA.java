package gramat.automaton;

import gramat.automata.ndfa.DMaker;
import gramat.automata.ndfa.Language;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class TestNDFA2DFA {

    @Test
    public void test1() throws IOException {
        var eLang = new Language();
        var s1 = eLang.state();
        var s2 = eLang.state();
        var s3 = eLang.state();
        var s4 = eLang.state();

        eLang.transition(s1, s1, 'a');
        eLang.transition(s1, s1, 'b');
        eLang.transition(s1, s2, 'b');
        eLang.transition(s2, s3, 'a');
        eLang.transition(s3, s4, 'a');
        eLang.transition(s3, s4, 'b');

        var eAm = eLang.automaton(s1, Set.of(s4));

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        dAm.write(System.out);
    }

    @Test
    public void test2() throws IOException {
        var eLang = new Language();
        var s1 = eLang.state();
        var s2 = eLang.state();
        var s3 = eLang.state();

        eLang.transition(s1, s1, '1');
        eLang.transition(s1, s2, '0');
        eLang.transition(s1, s3, '0');
        eLang.transition(s1, s2, null);
        eLang.transition(s2, s2, '1');
        eLang.transition(s2, s3, null);
        eLang.transition(s3, s3, '0');
        eLang.transition(s3, s3, '1');

        var eAm = eLang.automaton(s1, Set.of(s3));

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        dAm.write(System.out);
    }

    @Test
    public void test3() throws IOException {
        var eLang = new Language();
        var s1 = eLang.state();
        var s2 = eLang.state();
        var s3 = eLang.state();

        eLang.transition(s1, s1, '0');
        eLang.transition(s1, s2, null);
        eLang.transition(s2, s2, '1');
        eLang.transition(s2, s3, null);
        eLang.transition(s3, s3, '2');

        var eAm = eLang.automaton(s1, Set.of(s3));

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        dAm.write(System.out);
    }

    @Test
    public void test4() throws IOException {
        var eLang = new Language();
        var s0 = eLang.state();
        var s1 = eLang.state();
        var s2 = eLang.state();
        var s3 = eLang.state();
        var s4 = eLang.state();
        var s5 = eLang.state();
        var s6 = eLang.state();

        eLang.transition(s0, s1, '1');
        eLang.transition(s0, s1, null);
        eLang.transition(s1, s0, null);
        eLang.transition(s1, s2, null);
        eLang.transition(s2, s3, '0');
        eLang.transition(s2, s4, null);
        eLang.transition(s3, s4, '1');
        eLang.transition(s4, s2, null);
        eLang.transition(s4, s5, null);
        eLang.transition(s5, s6, '1');
        eLang.transition(s5, s6, null);
        eLang.transition(s6, s5, null);

        var eAm = eLang.automaton(s0, Set.of(s6));

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        dAm.write(System.out);
    }

}
