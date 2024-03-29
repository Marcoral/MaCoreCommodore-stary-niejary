package pl.mateam.marpg.api.submodules.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;

public interface CommodoreUtilsDevelopment {
	@Target({ ElementType.FIELD })
	@Retention(value = RetentionPolicy.RUNTIME)
	public @interface Extern {
		String key();
	}

	<T> void injectExternalField(Class<? super T> clazz, T instance, String keyOfExternal, Object value, boolean fieldSurelyExists) throws InjectExternalFieldException;
	public class InjectExternalFieldException extends CommodoreRuntimeException {
		private static final long serialVersionUID = -3825475363175213808L;
		
		private Class<?> clazz;
		private String keyOfExternal;
		private Object value;
		
		public InjectExternalFieldException(String message, Class<?> clazz, String keyOfExternal, Object value) {
			super(message);
			this.clazz = clazz;
			this.keyOfExternal = keyOfExternal;
			this.value = value;
		}
		
		public final Class<?> getPassedClass() {
			return clazz;
		}
		
		public final String getPassedKey() {
			return keyOfExternal;
		}
		
		public final Object getPassedValue() {
			return value;
		}
	}
}
