package pl.mateam.marpg.engine.apiimpl.submodules.utils;

import java.lang.reflect.Field;

import pl.mateam.marpg.api.modules.utils.CommodoreReflectionUtils;
import pl.mateam.marpg.engine.ControlPanel;

public class ReflectionUtilsInstance implements CommodoreReflectionUtils {
	@Override
	public Object getFieldValue(Object object, String fieldName) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
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
