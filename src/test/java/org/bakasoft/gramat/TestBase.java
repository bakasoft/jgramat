package org.bakasoft.gramat;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.framboyan.Framboyan;

public class TestBase extends Framboyan {

    protected void test(Element element, boolean expectedResult, String input) {
        Tape tape = new Tape("test", input);

        boolean actualResult = element.parse(tape);

        if (expectedResult) {
            expect(tape.alive()).toBeFalse();
            expect(actualResult).toBeTrue();
        }
        else {
            expect(!tape.alive() && actualResult).toBeFalse();
        }
    }

}

