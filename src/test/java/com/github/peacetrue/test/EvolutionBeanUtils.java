package com.github.peacetrue.test;

import com.github.peacetrue.util.stream.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.sql.Time;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 记录 {@link  BeanUtils} 的演化过程。
 *
 * @author peace
 **/
@Slf4j
public class EvolutionBeanUtils {

    private EvolutionBeanUtils() {
    }

    //tag::view[]

    /**
     * 查看对象信息。
     * <p>
     * 调用对象中所有无参带返回值的方法，以方法名作为 key，方法返回值作为 value，汇总为 Map 信息返回。
     *
     * @param object 任意对象
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
                    .collect(LinkedHashMap::new, (map, item) -> map.put(handleKey(object, item), handleValue(object, item)), HashMap::putAll);
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String handleKey(Object object, Method method) {
        return method.getName() + "(" + method.getReturnType().getSimpleName() + ")";
    }

    private static Object handleValue(Object object, Method method) {
        return invoke(object, method);
    }

    private static Object invoke(Object object, Method method) {
        try {
            method.setAccessible(true);
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
    //end::view[]

    //tag::viewNest[]

    /** 查看嵌套对象 */
    public static Object viewNest(Object object) {
        log.info("view: {}", object);
        if (object == null) return null;

        Class<?> type = object.getClass();
        if (isWrapperType(type) || isSimpleType(type)) return object;

        Stream<Object> stream = StreamUtils.toStream(object, temp -> null);
        if (stream != null) {
            return stream.map(EvolutionBeanUtils::viewNest).collect(Collectors.toList());
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass(), Object.class);
            MethodDescriptor[] descriptors = beanInfo.getMethodDescriptors();
            return Arrays.stream(descriptors)
                    .map(MethodDescriptor::getMethod)
                    .filter(Objects::nonNull)
                    .filter(method -> method.getParameterCount() == 0 && method.getReturnType() != Void.class)
                    .collect(LinkedHashMap::new, (map, item) -> map.put(handleKey(object, item), viewNest(invoke(object, item))), HashMap::putAll);
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class
    ));

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>(Arrays.asList(
            String.class, Date.class, Time.class, Enum.class, FileTime.class, UserPrincipal.class, GroupPrincipal.class,
            TemporalAccessor.class // Java 8 时间
    ));

    public static boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPES.stream().anyMatch(item -> item.isAssignableFrom(clazz));
    }

    //end::viewNest[]

}
