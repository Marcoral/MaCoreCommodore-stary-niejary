package pl.mateam.marpg.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CommodoreInformativeAnnotations {
	@Target({ ElementType.METHOD })
	@Retention(value = RetentionPolicy.SOURCE)
	public @interface UseAsyncOnly {}

	@Target({ ElementType.METHOD })
	@Retention(value = RetentionPolicy.SOURCE)
	public @interface MayRunAsync {}
	
	@Target({ ElementType.METHOD })
	@Retention(value = RetentionPolicy.SOURCE)
	public @interface RunsAsync {}
}
