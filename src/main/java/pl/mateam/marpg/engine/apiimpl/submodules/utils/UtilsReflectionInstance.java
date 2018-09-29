package pl.mateam.marpg.engine.apiimpl.submodules.utils;

import java.lang.reflect.Field;

import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsReflection;
import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;
import pl.mateam.marpg.engine.ControlPanel;

public class UtilsReflectionInstance implements CommodoreUtilsReflection {
	@Override
	public Object getFieldValue(Object object, String fieldName) {
		try {
			Class<?> clazz = object.getClass();
			Field field = null;
			do {
				if(clazz == null)
					throw new CommodoreRuntimeException("No field with given name!");
				try {
					field = clazz.getDeclaredField(fieldName);
				} catch(NoSuchFieldException e) {
					field = null;
				}
				clazz = clazz.getSuperclass();
			} while(field == null);
			field.setAccessible(true);
		    Object result = field.get(object);
		    field.setAccessible(false);
		    return result;
		} catch (Exception e) {
			ControlPanel.exceptionThrown(e);
			return null;
		}
	}
}
