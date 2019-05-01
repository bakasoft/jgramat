package org.gramat.plugins;

import java.util.function.Function;

public class TransformationPlugin extends Plugin {

    public final Function<String,String> transformation;

    public TransformationPlugin(Function<String,String> transformation) {
        this.transformation = transformation;
    }

}
