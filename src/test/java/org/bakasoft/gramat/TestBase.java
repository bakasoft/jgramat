package org.bakasoft.gramat;

import org.bakasoft.gramat.elements.Context;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.parsing.elements.GElement;

public class TestBase extends Framboyan {

    protected Tape tape(String content) {
        return new Tape("test", content);
    }

    protected void test(String code, boolean expectedResult, String input) {
        test(Element.eval(code), expectedResult, input);
    }

    protected void test(Element element, boolean expectedResult, String input) {
        Tape tape = new Tape(null, input);
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
        GElement exp = GElement.expectExpression(tape(code));

        expect(exp).not.toBeNull();

        String generatedCode = exp.stringify();

        expect(generatedCode).toBe(code);
    }

}

