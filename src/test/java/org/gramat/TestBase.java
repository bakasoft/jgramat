package org.gramat;

import org.bakasoft.framboyan.test.TestCase;
import org.gramat.elements.Context;
import org.gramat.elements.Element;
import org.gramat.parsers.Parser;
import org.gramat.parsing.GExpression;

abstract public class TestBase extends TestCase {

    protected Tape tape(String content) {
        return new Tape(content, "test");
    }

    protected void test(String code, boolean expectedResult, String input) {
        test(Element.eval(code), expectedResult, input);
    }

    protected void test(Element element, boolean expectedResult, String input) {
        Tape tape = new Tape(input);
        boolean actualResult = element.parse(new Context(tape));

        if (expectedResult) {
            expect(tape.alive()).toBeFalse();
            expect(actualResult).toBeTrue();
        }
        else {
            expect(!tape.alive() && actualResult).toBeFalse();
        }
    }

    protected void testExpression(String code) {
        Gramat gramat = new Gramat();
        GExpression exp = Parser.expectExpression(gramat, tape(code));

        expect(exp).not.toBeNull();

        String generatedCode = exp.stringify();

        expect(generatedCode).toEqual(code);
    }

}

