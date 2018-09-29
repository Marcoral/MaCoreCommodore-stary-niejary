package pl.mateam.marpg.api.submodules.utils;

public interface CommodoreUtilsReflection {
	Object getFieldValue(Object object, String fieldName);	//Use it when you are sure that field exists.
}
