package pl.mateam.marpg.engine.apiimpl.modules.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils;
import pl.mateam.marpg.engine.ControlPanel;

public class DevelopmentUtilsInstance implements CommodoreDevelopmentUtils {
	@Override
	public <T> void injectExternalField(Class<T> clazz, T instance, String keyOfExternal, Object value) throws InjectExternalFieldException {
		Field[] fields = clazz.getDeclaredFields();
		Field destination = null;
		for(Field field : fields) {
			if(field.isAnnotationPresent(Extern.class)
					&& field.getAnnotation(Extern.class).key().equals(keyOfExternal)) {
				if(destination == null)
					destination = field;
				else
					throw new InjectExternalFieldException("At least two fields in the class have same external key!", clazz, keyOfExternal, value);
			}
		}
		if(destination == null)
			throw new InjectExternalFieldException("No external field with given key!", clazz, keyOfExternal, value);
		destination.setAccessible(true);
		
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
			    modifiersField.setAccessible(true);
			    return null;
			});

			modifiersField.setInt(destination, destination.getModifiers() & ~Modifier.FINAL);
			destination.set(instance, value);
			destination.setAccessible(false);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			ControlPanel.exceptionThrown(e);
		} finally {
			destination.setAccessible(false);
		}
	}
}
