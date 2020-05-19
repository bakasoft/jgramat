package gramat.automaton;

import gramat.automata.ndfa.NLanguage;
import gramat.automata.dfa.DMaker;
import org.junit.Test;

import java.io.IOException;

public class TestNDFA2DFA {

    @Test
    public void test1() throws IOException {
        var lang = new NLanguage();
        var machine = lang.machine((context) -> {
            var s1 = context.initial();
            var s2 = context.state();
            var s3 = context.state();
            var s4 = context.accepted();

            lang.transitionChar(s1, s1, 'a');
            lang.transitionChar(s1, s1, 'b');
            lang.transitionChar(s1, s2, 'b');
            lang.transitionChar(s2, s3, 'a');
            lang.transitionChar(s3, s4, 'a');
            lang.transitionChar(s3, s4, 'b');
        });

        machine.write(System.out);

        var dAm = DMaker.transform(machine);

        System.out.println(dAm.getAmCode());
    }

    @Test
    public void test2() throws IOException {
        var eLang = new NLanguage();
        var eAm = eLang.machine((context) -> {
            var s1 = context.initial();
            var s2 = context.state();
            var s3 = context.accepted();

            eLang.transitionChar(s1, s1, '1');
            eLang.transitionChar(s1, s2, '0');
            eLang.transitionChar(s1, s3, '0');
            eLang.transitionNull(s1, s2);
            eLang.transitionChar(s2, s2, '1');
            eLang.transitionNull(s2, s3);
            eLang.transitionChar(s3, s3, '0');
            eLang.transitionChar(s3, s3, '1');
        });

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        System.out.println(dAm.getAmCode());
    }

    @Test
    public void test3() throws IOException {
        var eLang = new NLanguage();
        var eAm = eLang.machine((context) -> {
            var s1 = context.initial();
            var s2 = context.state();
            var s3 = context.accepted();

            eLang.transitionChar(s1, s1, '0');
            eLang.transitionNull(s1, s2);
            eLang.transitionChar(s2, s2, '1');
            eLang.transitionNull(s2, s3);
            eLang.transitionChar(s3, s3, '2');
        });

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        System.out.println(dAm.getAmCode());
    }

    @Test
    public void test4() throws IOException {
        var eLang = new NLanguage();
        var eAm = eLang.machine((context) -> {
            var s0 = context.initial();
            var s1 = context.state();
            var s2 = context.state();
            var s3 = context.state();
            var s4 = context.state();
            var s5 = context.state();
            var s6 = context.accepted();

            eLang.transitionChar(s0, s1, '1');
            eLang.transitionNull(s0, s1);
            eLang.transitionNull(s1, s0);
            eLang.transitionNull(s1, s2);
            eLang.transitionChar(s2, s3, '0');
            eLang.transitionNull(s2, s4);
            eLang.transitionChar(s3, s4, '1');
            eLang.transitionNull(s4, s2);
            eLang.transitionNull(s4, s5);
            eLang.transitionChar(s5, s6, '1');
            eLang.transitionNull(s5, s6);
            eLang.transitionNull(s6, s5);
        });

        eAm.write(System.out);

        var dAm = DMaker.transform(eAm);

        System.out.println(dAm.getAmCode());
    }

}
