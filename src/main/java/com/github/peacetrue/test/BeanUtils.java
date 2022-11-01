package com.github.peacetrue.test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Bean 工具类。
 * <p>
 * 在测试时，想要查看一个对象的详细信息，一般可以通过内省反射的方式，获取属性和属性值。
 * 如果这个对象不符合 Bean 的 getter/setter 规范，就无法获取到它的属性值了。
 * 例如：{@link PosixFileAttributes} 中的绝大多数属性都无法查看。
 * 我们可以在查看对象时，调用对象中无参带返回值的方法，获取到方法的返回值。
 *
 * @author peace
 **/
public class BeanUtils {

    private BeanUtils() {
    }

    /**
     * 查看对象信息。
     * 调用对象中所有无参带返回值的方法，以方法名作为 key，方法返回值作为 value，汇总为 Map 信息。
     *
     * @param object 任意对象，除了 {@code new Object()}
     * @return 可视化对象信息，key 为方法名，value 为方法返回值
     * @throws IllegalStateException 如果内省时或者反射调用方法时发生异常，则将其转换为此运行时异常
     */
    public static Map<String, Object> view(Object object) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass(), Object.class);
            MethodDescriptor[] descriptors = beanInfo.getMethodDescriptors();
            return Arrays.stream(descriptors)
                    .map(MethodDescriptor::getMethod)
                    .filter(Objects::nonNull)
                    .filter(method -> method.getParameterCount() == 0 && method.getReturnType() != Void.class)
                    .collect(Collectors.toMap(Method::getName, method -> invoke(object, method)));
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object invoke(Object object, Method method) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
