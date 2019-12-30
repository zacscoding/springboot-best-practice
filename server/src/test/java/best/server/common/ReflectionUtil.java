package best.server.common;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static String[] getFieldNames(Class<?> clazz) {
        try {
            String[] fields = new String[clazz.getDeclaredFields().length];
            Field[] declaredFields = clazz.getDeclaredFields();

            for (int i = 0; i < declaredFields.length; i++) {
                fields[i] = declaredFields[i].getName();
            }

            return fields;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReflectionUtil() {}
}
