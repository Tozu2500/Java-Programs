package practice.reflection;

import java.lang.reflect.Field;

public class ReflectionCopier {
	
	public static void copyFields(Object source, Object target) {
		if (source == null || target == null) {
			throw new IllegalArgumentException("Source and target must not be null!");
		}
		
		Class<?> clazz = source.getClass();
		if (!clazz.equals(target.getClass())) {
			throw new IllegalArgumentException("Source and target must be from the same class!");
		}
		
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true); // Access to private fields
				try {
					Object value = field.get(source);
					field.set(target, value);
				} catch (IllegalAccessException e) {
					System.err.println("Failed to copy field: " + field.getName());
					e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

}
