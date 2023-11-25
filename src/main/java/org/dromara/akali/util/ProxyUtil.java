package org.dromara.akali.util;

public class ProxyUtil {

    public static Class<?> getUserClass(Class<?> clazz) {
        if (isCglibProxyClass(clazz)) {
            Class<?> superclass = clazz.getSuperclass();
            return getUserClass(superclass);
        }
        return clazz;
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    private static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

}
