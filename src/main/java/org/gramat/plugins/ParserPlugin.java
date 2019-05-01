package org.gramat.plugins;

import java.util.function.Function;

public class ParserPlugin extends Plugin {

    public final Function<String, ?> parser;

    public ParserPlugin(Function<String, ?> parser) {
        this.parser = parser;
    }

}
