package com.github.peacetrue.test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * Bean 工具类。
 * <p>
 * 在测试时，想要查看一个对象的详细信息，一般可以通过内省反射的方式，获取属性和属性值。
 * 如果这个对象不符合 Bean 的 getter/setter 规范，就无法获取到它的属性值了。
 * 例如：{@link java.nio.file.attribute.PosixFileAttributes} 中的绝大多数属性都无法查看。
 * 我们可以在查看对象时，调用对象中无参带返回值的方法，获取到方法的返回值。
 *
 * @author peace
 **/
public class BeanUtils {

    private BeanUtils() {
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> view(Object object) {
        return (Map<String, Object>) view(object, 1);
    }

    /**
     * 查看对象信息。
     * 调用对象中所有无参带返回值的方法，以方法名作为 key，方法返回值作为 value，汇总为 Map 信息。
     *
     * @param object 任意对象
     * @param depth  深度
     * @return 可视化对象信息，key 为方法名，value 为方法返回值
     * @throws IllegalStateException 如果内省时或者反射调用方法时发生异常，则将其转换为此运行时异常
     */
    public static Object view(Object object, int depth) {
//        if (depth == 0 || object == null || object.getClass().isPrimitive()) return object;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
//            return view(object, beanInfo);
            return null;
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Stream<Method> getMethodStream(BeanInfo beanInfo) {
        return Arrays.stream(beanInfo.getMethodDescriptors())
                .map(MethodDescriptor::getMethod)
                .filter(Objects::nonNull);
    }

    private static Map<String, Object> view(Object object, Stream<Method> methods) {
        return methods
                .filter(method -> method.getParameterCount() == 0 && method.getReturnType() != Void.class)
                .collect(LinkedHashMap::new, (map, item) -> map.put(item.getName(), invoke(object, item)), HashMap::putAll);
    }

    /**
     * 调用无参方法。
     *
     * @param object 对象
     * @param method 无参方法
     * @return 无参方法调用后的返回值，如果发生异常，异常消息作为返回值输出
     */
    public static Object invoke(Object object, Method method) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return e.getClass().getName() + ":" + e.getMessage();
        }
    }
}
