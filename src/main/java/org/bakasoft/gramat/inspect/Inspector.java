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

    private final HashSet<Object> inspected;

    public Inspector() {
        inspected = new HashSet<>();
    }

    public void writeAny(Object value) {
        if (value == null) {
            write(NULL_MARK);
        }
        else if (value instanceof Inspectable) {
            if (inspected.add(value)) {
                ((Inspectable) value).inspectWith(this);
            }
            else {
                write(RECURSION_MARK);
            }
        }
        else if (value instanceof Number || value instanceof Boolean) {
            write(value.toString());
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
            write(NULL_MARK);
        }
        else if (inspected.add(array)) {
            int length = Array.getLength(array);

            write('[');

            for (int i = 0; i < length; i++) {
                Object item = Array.get(array, i);

                if (i > 0) {
                    write(',');
                }

                writeAny(item);
            }

            write(']');
        }
        else {
            write(RECURSION_MARK);
        }
    }

    public void writeCollection(Collection<?> collection) {
        if (collection == null) {
            write(NULL_MARK);
        }
        else if (inspected.add(collection)) {
            write('[');

            boolean addComma = false;
            for (Object item : collection) {
                if (addComma) {
                    write(',');
                }

                writeAny(item);

                addComma = true;
            }

            write(']');
        }
        else {
            write(RECURSION_MARK);
        }
    }

    public void writeMap(Map<?,?> map) {
        if (map == null) {
            write(NULL_MARK);
        }
        else if (inspected.add(map)) {
            write('{');
            boolean addComma = false;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (addComma) {
                    write(',');
                }

                writeAny(entry.getKey());
                write(':');
                writeAny(entry.getValue());

                addComma = true;
            }
            write('}');
        }
        else {
            write(RECURSION_MARK);
        }
    }

    public void writeString(char content, char delimiter) {
        writeString(String.valueOf(content), delimiter, delimiter);
    }

    public void writeString(CharSequence content, char delimiter) {
        writeString(content, delimiter, delimiter);
    }

    public void writeString(CharSequence content, char beginDelimiter, char endDelimiter) {
        if (content == null) {
            write(NULL_MARK);
        }
        else {
            write(beginDelimiter);
            for (int i = 0; i < content.length(); i++) {
                writeStringChar(content.charAt(i), beginDelimiter, endDelimiter);
            }
            write(endDelimiter);
        }
    }

    public void writeStringChar(char c, char delimiter) {
        writeStringChar(c, delimiter, delimiter);
    }

    public void writeStringChar(char c, char beginDelimiter, char endDelimiter) {
        switch(c) {
            // special escapes
            case '\\':
                write("\\\\");
                break;
            case '\n':
                write("\\n");
                break;
            case '\r':
                write("\\r");
                break;
            case '\t':
                write("\\t");
                break;

            default:
                // always escape delimiter
                if (c == beginDelimiter || c == endDelimiter) {
                    write('\\');
                    write(c);
                }
                // basic characters
                else if (c >= 0x20 && c <= 0x7E) {
                    write(c);
                }
                // advanced characters
                else {
                    write("\\u");
                    String hex = Integer.toHexString(c);
                    for (int i = hex.length(); i < 4; i++) {
                        write('0');
                    }
                    write(hex);
                }
        }
    }

    public void writeObject(Object instance) {
        if (instance == null) {
            write(NULL_MARK);
        }
        else if (inspected.add(instance)) {
            Class<?> type = instance.getClass();

            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
                PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

                if (properties != null && properties.length > 0) {
                    write('{');
                    write("@id:");
                    write(Integer.toHexString(instance.hashCode()));
                    write(',');

                    write("@type:");
                    write(type.getSimpleName());

                    for (PropertyDescriptor property : properties) {
                        Method getter = property.getReadMethod();
                        if (getter != null && getter.getParameterCount() == 0) {
                            String key = property.getName();
                            write(',');
                            writeString(key, STRING_DELIMITER);
                            write(':');
                            try {
                                Object value = getter.invoke(instance);

                                writeAny(value);
                            }
                            catch (Exception e) {
                                write('!');
                                writeString(e.getMessage() != null
                                    ? e.getMessage() : e.getClass().getSimpleName(), '<', '>');
                            }
                        }
                    }
                    write('}');
                }
                else {
                    write(instance.toString());
                }
            } catch (IntrospectionException e) {
                write(instance.toString());
            }
        }
        else {
            write(RECURSION_MARK);
        }
    }

    @Override
    public String toString() {
        return toString();
    }

}
