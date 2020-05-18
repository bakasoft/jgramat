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
        var eAm = eLang.automaton((initialSet, acceptedSet) -> {
            var s1 = initialSet.create();
            var s2 = eLang.state();
            var s3 = eLang.state();
            var s4 = acceptedSet.create();

            eLang.transition(s1, s1, 'a');
            eLang.transition(s1, s1, 'b');
            eLang.transition(s1, s2, 'b');
            eLang.transition(s2, s3, 'a');
            eLang.transition(s3, s4, 'a');
            eLang.transition(s3, s4, 'b');
        });

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        System.out.println(dAm.getAmCode());
    }

    @Test
    public void test2() throws IOException {
        var eLang = new Language();
        var eAm = eLang.automaton((initialSet, acceptedSet) -> {
            var s1 = initialSet.create();
            var s2 = eLang.state();
            var s3 = acceptedSet.create();

            eLang.transition(s1, s1, '1');
            eLang.transition(s1, s2, '0');
            eLang.transition(s1, s3, '0');
            eLang.transition(s1, s2, null);
            eLang.transition(s2, s2, '1');
            eLang.transition(s2, s3, null);
            eLang.transition(s3, s3, '0');
            eLang.transition(s3, s3, '1');
        });

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        System.out.println(dAm.getAmCode());
    }

    @Test
    public void test3() throws IOException {
        var eLang = new Language();
        var eAm = eLang.automaton((initialSet, acceptedSet) -> {
            var s1 = initialSet.create();
            var s2 = eLang.state();
            var s3 = acceptedSet.create();

            eLang.transition(s1, s1, '0');
            eLang.transition(s1, s2, null);
            eLang.transition(s2, s2, '1');
            eLang.transition(s2, s3, null);
            eLang.transition(s3, s3, '2');
        });

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        System.out.println(dAm.getAmCode());
    }

    @Test
    public void test4() throws IOException {
        var eLang = new Language();
        var eAm = eLang.automaton((initialSet, acceptedSet) -> {
            var s0 = initialSet.create();
            var s1 = eLang.state();
            var s2 = eLang.state();
            var s3 = eLang.state();
            var s4 = eLang.state();
            var s5 = eLang.state();
            var s6 = acceptedSet.create();

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
        });

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        System.out.println(dAm.getAmCode());
    }

}
