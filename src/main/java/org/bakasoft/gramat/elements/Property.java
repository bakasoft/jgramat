package org.bakasoft.gramat.elements;

abstract public class Property extends Element {

    public static String parseText(Element element, Context ctx) {
        ctx.capture.beginTransaction();

        if (element.parse(ctx)) {
            return ctx.capture.commitTransaction();
        }

        ctx.capture.rollbackTransaction();
        return null;
    }


}
