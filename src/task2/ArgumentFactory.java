package task2;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ArgumentFactory {

    private static final Map<Class<?>, Class<?>> ALIASES = Map.ofEntries(
            Map.entry(Integer.class, int.class),
            Map.entry(Long.class, long.class),
            Map.entry(Double.class, double.class),
            Map.entry(Float.class, float.class),
            Map.entry(Short.class, short.class),
            Map.entry(Byte.class, byte.class),
            Map.entry(Boolean.class, boolean.class),
            Map.entry(Character.class, char.class),
            Map.entry(CharSequence.class, String.class),
            Map.entry(Object.class, String.class));

    private final Map<Class<?>, Object> values = new HashMap<>();

    // типы, которые сейчас создаются - защита от бесконечной рекурсии (A(B b), B(A a))
    private final Set<Class<?>> inProgress = new HashSet<>();

    public ArgumentFactory() {
        values.put(int.class, 2);
        values.put(long.class, 2L);
        values.put(double.class, 1.5);
        values.put(float.class, 1.5f);
        values.put(short.class, (short) 1);
        values.put(byte.class, (byte) 1);
        values.put(boolean.class, true);
        values.put(char.class, 'A');
        values.put(String.class, "abc");
    }

    public void setValue(Class<?> type, Object value) {
        values.put(type, value);
    }

    public Object getValue(Class<?> type) {
        return values.get(type);
    }

    public Object[] buildArguments(Class<?>[] types) {
        Object[] args = new Object[types.length];

        for (int i = 0; i < types.length; i++)
        {
            args[i] = create(types[i]);
        }

        return args;
    }

    public Object create(Class<?> type) {
        Class<?> key = ALIASES.getOrDefault(type, type);
        if (values.containsKey(key))
            return values.get(key);

        if (type.isArray())
        {
            // массив из одного элемента нужного типа
            Object array = Array.newInstance(type.getComponentType(), 1);
            Array.set(array, 0, create(type.getComponentType()));

            return array;
        }

        if (type.isEnum())
        {
            Object[] constants = type.getEnumConstants();

            if (constants.length == 0)
                throw new IllegalArgumentException("Перечисление " + type.getName() + " не имеет констант");

            return constants[0];
        }

        if (type.isInterface())
            return createForInterface(type);

        if (Modifier.isAbstract(type.getModifiers()))
            throw new IllegalArgumentException(
                    "Нельзя создать экземпляр абстрактного класса " + type.getName());

        return createByConstructor(type);
    }

    // пользовательский класс перебором конструкторов
    private Object createByConstructor(Class<?> type) {
        if (!inProgress.add(type))
            throw new IllegalArgumentException("Циклическая зависимость при создании " + type.getName());

        try {
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            //сортировка с меньшим колвом параметров раньше
            Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

            Exception lastError = null;
            for (Constructor<?> constructor : constructors)
            {
                try
                {
                    constructor.setAccessible(true);
                    Object[] args = buildArguments(constructor.getParameterTypes());

                    return constructor.newInstance(args);
                } catch (Exception e) {
                    lastError = e; // пробуем следующий конструктор
                }
            }
            throw new IllegalArgumentException("Не удалось создать значение для типа " + type.getName(), lastError);
        } finally {
            inProgress.remove(type);
        }
    }

    private Object createForInterface(Class<?> type) {
        if (type.isAssignableFrom(ArrayList.class))
            return new ArrayList<>();
        if (type.isAssignableFrom(HashSet.class))
            return new HashSet<>();
        if (type.isAssignableFrom(HashMap.class))
            return new HashMap<>();
        if (type.isAssignableFrom(ArrayDeque.class))
            return new ArrayDeque<>();

        // реализацию произвольного интерфейса по одному его типу не найти
        throw new IllegalArgumentException(
                "Нельзя создать экземпляр интерфейса " + type.getName());
    }
}
