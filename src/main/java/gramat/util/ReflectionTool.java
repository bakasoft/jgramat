package gramat.util;

import gramat.Gramat;
import gramat.GramatException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTool {

    private static Method find_setter_method(Method[] methods, String name) {
        for (var method : methods) {
            if (method.getName().toLowerCase().equals("set" + name) && method.getParameterCount() == 1) {
                return method;
            }
        }

        return null;
    }

    private static Field find_setter_field(Field[] fields, String name) {
        for (var field : fields) {
            if (field.getName().toLowerCase().equals(name)) {
                return field;
            }
        }

        return null;
    }

    public static void set(Class<?> type, Object instance, String name, Object value) {
        var methods = type.getMethods();

        var setterMethod = find_setter_method(methods, name);

        if (setterMethod != null) {
            try {
                setterMethod.invoke(instance, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new GramatException(e, "Cannot assign  " + name + " for " + type);
            }
        }
        else {
            var setterField = find_setter_field(type.getFields(), name);

            if (setterField == null) {
                throw new GramatException("Field not assignable " + name + " for " + type);
            }

            try {
                setterField.set(instance, value);
            } catch (IllegalAccessException e) {
                throw new GramatException(e, "Cannot assign  " + name + " for " + type);
            }
        }
    }

    public static Object newInstance(Class<?> type) {
        Constructor<?> ctr;
        try {
            ctr = type.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new GramatException(e, "Missing empty constructor: " + type);
        }

        try {
            return ctr.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new GramatException(e, "Cannot create instance of: " + type);
        }
    }
}
