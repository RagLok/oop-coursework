package task2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// Вызывает аннотированные @Repeat защищённые и приватные методы объекта
public class MethodInvoker {

    private final ArgumentFactory argumentFactory;
    private final Consumer<String> out;

    public MethodInvoker(ArgumentFactory argumentFactory, Consumer<String> out) {
        this.argumentFactory = argumentFactory;
        this.out = out;
    }

    public static List<Method> sortedMethods(Class<?> type) {
        Method[] methods = type.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));
        return List.of(methods);
    }

    public static boolean isTarget(Method method) {
        int mod = method.getModifiers();
        return method.isAnnotationPresent(Repeat.class)
                && (Modifier.isProtected(mod) || Modifier.isPrivate(mod));
    }

    public static String signature(Method method) {
        String params = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
        return method.getName() + "(" + params + ")";
    }

    public void invokeAnnotated(Object target) {
        for (Method method : sortedMethods(target.getClass())) {
            if (!isTarget(method)) {
                continue; // не аннотирован или публичный - пропускаем
            }

            int times = method.getAnnotation(Repeat.class).value();
            out.accept("Метод " + signature(method)
                    + " (" + Modifier.toString(method.getModifiers()) + "), вызовов: " + times);

            // доступ к private/protected
            method.setAccessible(true);

            Object[] arguments;
            try {
                arguments = argumentFactory.buildArguments(method.getParameterTypes());
            } catch (IllegalArgumentException e) {
                out.accept("    не удалось подготовить аргументы: " + e.getMessage());
                out.accept("");
                continue;
            }

            for (int i = 0; i < times; i++) {
                try {
                    Object result = method.invoke(target, arguments);
                    if (method.getReturnType() != void.class) {
                        out.accept("    результат: " + result);
                    }
                } catch (IllegalAccessException e) {
                    out.accept("    нет доступа: " + e.getMessage());
                } catch (InvocationTargetException e) {
                    out.accept("    метод выбросил исключение: " + e.getCause());
                }
            }
            out.accept("");
        }
    }
}
