package pl.mateam.marpg.api.modules.utils;

public interface CommodoreReflectionUtils {
	Object getFieldValue(Object object, String fieldName);	//Use it when you are sure that field exists.
}
