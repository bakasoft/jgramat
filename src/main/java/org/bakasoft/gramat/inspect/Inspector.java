package org.bakasoft.gramat.inspect;

import org.bakasoft.gramat.util.CodeWriter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class Inspector extends CodeWriter {

    public static final String RECURSION_MARK = "...";
    public static final String NULL_MARK = "null";
    public static final char STRING_DELIMITER = '"';

    public static String inspect(Object value) {
        Inspector output = new Inspector();
        output.writeAny(value);
        return output.getOutput();
    }

    private final StringBuilder output;
    private final HashSet<Object> inspected;

    public Inspector() {
        output = new StringBuilder();
        inspected = new HashSet<>();
    }

    public void writeAny(Object value) {
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
            writeString(value.toString(), STRING_DELIMITER);
        }
        else if (value instanceof CharSequence) {
            writeString((CharSequence)value, STRING_DELIMITER);
        }
        else if (value instanceof Collection) {
            writeCollection((Collection<?>)value);
        }
        else if (value instanceof Map) {
            writeMap((Map<?, ?>)value);
        }
        else if (value.getClass().isArray()) {
            writeArray(value);
        }
        else {
            writeObject(value);
        }
    }

    public void writeArray(Object array) {
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

                writeAny(item);
            }

            output.append(']');
        }
        else {
            output.append(RECURSION_MARK);
        }
    }

    public void writeCollection(Collection<?> collection) {
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

                writeAny(item);

                addComma = true;
            }

            output.append(']');
        }
        else {
            output.append(RECURSION_MARK);
        }
    }

    public void writeMap(Map<?,?> map) {
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

                writeAny(entry.getKey());
                output.append(':');
                writeAny(entry.getValue());

                addComma = true;
            }
            output.append('}');
        }
        else {
            output.append(RECURSION_MARK);
        }
    }

    public void writeString(CharSequence content, char delimiter) {
        writeString(content, delimiter, delimiter);
    }

    public void writeString(CharSequence content, char beginDelimiter, char endDelimiter) {
        if (content == null) {
            output.append(NULL_MARK);
        }
        else {
            output.append(beginDelimiter);
            for (int i = 0; i < content.length(); i++) {
                writeStringChar(content.charAt(i), beginDelimiter, endDelimiter);
            }
            output.append(endDelimiter);
        }
    }

    public void writeStringChar(char c, char delimiter) {
        writeStringChar(c, delimiter, delimiter);
    }

    public void writeStringChar(char c, char beginDelimiter, char endDelimiter) {
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

    public void writeObject(Object instance) {
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
                            writeString(key, STRING_DELIMITER);
                            output.append(':');
                            try {
                                Object value = getter.invoke(instance);

                                writeAny(value);
                            }
                            catch (Exception e) {
                                output.append('!');
                                writeString(e.getMessage() != null
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

    @Override
    public String toString() {
        return output.toString();
    }

}
