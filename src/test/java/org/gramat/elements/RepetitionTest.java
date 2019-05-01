package org.gramat.elements;

import org.gramat.TestBase;

public class RepetitionTest extends TestBase {{

    pass ("case1", () -> {
        Element data = Element.eval("{2~4 \"0\" / \"-\"}");

        test(data, false, "");
        test(data, false, "0");
        test(data, true, "0-0");
        test(data, true, "0-0-0-0");
        test(data, false, "0-0-0-0-0");
    });

}}
