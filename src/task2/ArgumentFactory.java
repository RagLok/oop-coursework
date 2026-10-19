package task2;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

// Создаёт значения аргументов по типу параметра. Значения можно задать из GUI
public class ArgumentFactory {

    private static final int ARRAY_LENGTH = 2;

    private final Map<Class<?>, Object> values = new HashMap<>();

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
        for (int i = 0; i < types.length; i++) {
            args[i] = create(types[i]);
        }
        return args;
    }

    public Object create(Class<?> type) {
        if (values.containsKey(type))
            return values.get(type);

        // Массив заполняется значениями типа своих элементов
        if (type.isArray()) {
            Object array = Array.newInstance(type.getComponentType(), ARRAY_LENGTH);
            for (int i = 0; i < ARRAY_LENGTH; i++)
                Array.set(array, i, create(type.getComponentType()));
            return array;
        }

        throw new IllegalArgumentException(
                "Не удалось создать значение для типа " + type.getName());
    }
}
