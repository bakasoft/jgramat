package org.bakasoft.gramat.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {

    public static <T> Constructor<T> getDefaultConstructor(Class<T> type) {
        try {
            return type.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("the " + type + " must have an empty constructor", e);
        }
    }

    public static <T> T newInstance(Class<T> type) {
        Constructor<T> ctr = getDefaultConstructor(type);

        try {
            return ctr.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // TODO: improve error handling
            throw new RuntimeException("couldn't create an item of " + type, e);
        }
    }

    public static Map<String, PropertyDescriptor> loadProperties(Class<?> type, boolean canRead, boolean canWrite) {
        try {
            Map<String, PropertyDescriptor> properties = new HashMap<>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

            if (descriptors == null) {
                throw new RuntimeException();
            }

            for (PropertyDescriptor descriptor : descriptors) {
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();

                boolean getCheck = canRead && getter != null && getter.getParameterCount() == 0;
                boolean setCheck = canWrite && setter != null && setter.getParameterCount() == 1;

                if (getCheck || setCheck) {
                    properties.put(descriptor.getName(), descriptor);
                }
            }

            return properties;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setValue(PropertyDescriptor property, Object instance, Object value) {
        Method setter = property.getWriteMethod();

        if (instance == null) {
            throw new RuntimeException("instance cannot be null");
        }
        else if (setter == null) {
            throw new RuntimeException("Cannot set property " + instance.getClass().getSimpleName() + "." + property.getName() + " because is readonly.");
        }
        else if (setter.getParameterCount() != 1) {
            throw new RuntimeException("Expected a setter with one parameter");
        }

        Class<?> parent = setter.getDeclaringClass();
        Class<?> type = setter.getParameters()[0].getType();

        if (!type.isInstance(value)) {
            throw new RuntimeException((value != null ? value.getClass() : "null") + " is not compatible with " + type);
        }
        else if (!parent.isInstance(instance)) {
            throw new RuntimeException(instance.getClass() + " is not compatible with " + parent);
        }

        try {
            setter.invoke(instance, value);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addElement(PropertyDescriptor property, Object instance, Object element) {
        Collection<Object> collection = null;

        Method getter = property.getReadMethod();

        if (getter != null && getter.getParameterCount() == 0) {
            try {
                Object raw = getter.invoke(instance);

                if (raw != null) {
                    if (raw instanceof Collection) {
                        collection = (Collection<Object>)raw;
                    }
                    else {
                        throw new RuntimeException();
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        if (collection == null) {
            Method setter = property.getWriteMethod();

            if (setter != null && setter.getParameterCount() == 1) {
                Class<?> collectionType = property.getPropertyType();

                if (Collection.class.isAssignableFrom(collectionType)
                        && !collectionType.isInterface()
                        && !Modifier.isAbstract(collectionType.getModifiers())) {
                    try {
                        Constructor<?> ctr = collectionType.getConstructor();

                        collection = (Collection<Object>)ctr.newInstance();
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException();
                    }
                }
                else if (collectionType.isAssignableFrom(ArrayList.class)) {
                    collection = new ArrayList<Object>();
                }
                else {
                    throw new RuntimeException();
                }

                try {
                    setter.invoke(instance, collection);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (collection == null) {
            throw new RuntimeException();
        }

        collection.add(element);
    }
}
