package org.bakasoft.gramat.elements;

abstract public class Property extends Element {

    public static boolean parsePushValue(Element element, Context ctx) {
        if (element instanceof ObjectElement || element instanceof ValueElement || element instanceof ListElement) {
            return element.parse(ctx);
        }

        String value = parseText(element, ctx);

        if (value == null) {
            return false;
        }

        ctx.builder.pushValue(value);
        return true;
    }

    public static String parseText(Element element, Context ctx) {
        ctx.capture.beginTransaction();

        if (element.parse(ctx)) {
            return ctx.capture.commitTransaction();
        }

        ctx.capture.rollbackTransaction();
        return null;
    }


}
