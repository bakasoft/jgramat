package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.TestBase;

public class NegationTest extends TestBase {{

    it("Negation", ()-> {
        Element element = Element.eval("\"(\" {<\")\">} \")\"");

        test(element, false, "");
        test(element, true, "()");
        test(element, true, "(asdf)");
        test(element, true, "(1 2 3)");
        test(element, false, "(1 ) 3)");
        test(element, false, "(x)(y)");
    });

}}
