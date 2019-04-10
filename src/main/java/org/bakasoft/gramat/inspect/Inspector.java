package org.bakasoft.gramat.inspect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class Inspector {

    public static final String RECURSION_MARK = "...";
    public static final String NULL_MARK = "null";
    public static final char STRING_DELIMITER = '"';

    public static String inspect(Object value) {
        Inspector output = new Inspector();
        output.append(value);
        return output.getOutput();
    }

    @Override
    public String toString() {
        return output.toString();
    }

    public String getOutput() {
        return output.toString();
    }

    private final StringBuilder output;
    private final HashSet<Object> inspected;

    public Inspector() {
        output = new StringBuilder();
        inspected = new HashSet<>();
    }

    public void append(Object value) {
        if (value == null) {
            output.append(NULL_MARK);
        }
        else if (value instanceof Inspectable) {
            if (inspected.add(value)) {
                ((Inspectable) value).inspectWith(this);
            }
            else {
                output.append(RECURSION_MARK);
            }
        }
        else if (value instanceof Number || value instanceof Boolean) {
            output.append(value.toString());
        }
        else if (value instanceof Character) {
            appendString(value.toString(), STRING_DELIMITER);
        }
        else if (value instanceof CharSequence) {
            appendString((CharSequence)value, STRING_DELIMITER);
        }
        else if (value instanceof Collection) {
            appendCollection((Collection<?>)value);
        }
        else if (value instanceof Map) {
            appendMap((Map<?, ?>)value);
        }
        else if (value.getClass().isArray()) {
            appendArray(value);
        }
        else {
            appendObject(value);
        }
    }

    public void appendArray(Object array) {
        if (array == null) {
            output.append(NULL_MARK);
        }
        else if (inspected.add(array)) {
            int length = Array.getLength(array);

            output.append('[');

            for (int i = 0; i < length; i++) {
                Object item = Array.get(array, i);

                if (i > 0) {
                    output.append(',');
                }

                append(item);
            }

            output.append(']');
        }
        else {
            output.append(RECURSION_MARK);
        }
    }

    public void appendCollection(Collection<?> collection) {
        if (collection == null) {
            output.append(NULL_MARK);
        }
        else if (inspected.add(collection)) {
            output.append('[');

            boolean addComma = false;
            for (Object item : collection) {
                if (addComma) {
                    output.append(',');
                }

                append(item);

                addComma = true;
            }

            output.append(']');
        }
        else {
            output.append(RECURSION_MARK);
        }
    }

    public void appendMap(Map<?,?> map) {
        if (map == null) {
            output.append(NULL_MARK);
        }
        else if (inspected.add(map)) {
            output.append('{');
            boolean addComma = false;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (addComma) {
                    output.append(',');
                }

                append(entry.getKey());
                output.append(':');
                append(entry.getValue());

                addComma = true;
            }
            output.append('}');
        }
        else {
            output.append(RECURSION_MARK);
        }
    }

    public void appendChar(char c) {
        output.append(c);
    }

    public void appendChars(CharSequence text) {
        if (text != null) {
            output.append(text);
        }
    }

    public void appendString(CharSequence content, char delimiter) {
        appendString(content, delimiter, delimiter);
    }

    public void appendString(CharSequence content, char beginDelimiter, char endDelimiter) {
        if (content == null) {
            output.append(NULL_MARK);
        }
        else {
            output.append(beginDelimiter);
            for (int i = 0; i < content.length(); i++) {
                appendStringChar(content.charAt(i), beginDelimiter, endDelimiter);
            }
            output.append(endDelimiter);
        }
    }

    public void appendStringChar(char c, char delimiter) {
        appendStringChar(c, delimiter, delimiter);
    }

    public void appendStringChar(char c, char beginDelimiter, char endDelimiter) {
        switch(c) {
            // special escapes
            case '\\':
                output.append("\\\\");
                break;
            case '\n':
                output.append("\\n");
                break;
            case '\r':
                output.append("\\r");
                break;
            case '\t':
                output.append("\\t");
                break;

            default:
                // always escape delimiter
                if (c == beginDelimiter || c == endDelimiter) {
                    output.append('\\');
                    output.append(c);
                }
                // basic characters
                else if (c >= 0x20 && c <= 0x7E) {
                    output.append(c);
                }
                // advanced characters
                else {
                    output.append("\\u");
                    String hex = Integer.toHexString(c);
                    for (int i = hex.length(); i < 4; i++) {
                        output.append('0');
                    }
                    output.append(hex);
                }
        }
    }

    public void appendObject(Object instance) {
        if (instance == null) {
            output.append(NULL_MARK);
        }
        else if (inspected.add(instance)) {
            Class<?> type = instance.getClass();

            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
                PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

                if (properties != null && properties.length > 0) {
                    output.append('{');
                    output.append("@id:");
                    output.append(Integer.toHexString(instance.hashCode()));
                    output.append(',');

                    output.append("@type:");
                    output.append(type.getSimpleName());

                    for (PropertyDescriptor property : properties) {
                        Method getter = property.getReadMethod();
                        if (getter != null && getter.getParameterCount() == 0) {
                            String key = property.getName();
                            output.append(',');
                            appendString(key, STRING_DELIMITER);
                            output.append(':');
                            try {
                                Object value = getter.invoke(instance);

                                append(value);
                            }
                            catch (Exception e) {
                                output.append('!');
                                appendString(e.getMessage() != null
                                    ? e.getMessage() : e.getClass().getSimpleName(), '<', '>');
                            }
                        }
                    }
                    output.append('}');
                }
                else {
                    output.append(instance.toString());
                }
            } catch (IntrospectionException e) {
                output.append(instance.toString());
            }
        }
        else {
            output.append(RECURSION_MARK);
        }
    }

}
