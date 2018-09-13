package pl.mateam.marpg.engine.apiimpl.submodules.utils;

import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils;
import pl.mateam.marpg.api.modules.utils.CommodoreReflectionUtils;
import pl.mateam.marpg.api.modules.utils.CommodoreUtils;

public class UtilsInstance implements CommodoreUtils {
	private CommodoreDevelopmentUtils developmentUtils = new DevelopmentUtilsInstance();
	private CommodoreMessengingUtils messengingUtils = new MessengingUtilsInstance();
	private CommodoreReflectionUtils reflectionUtils = new ReflectionUtilsInstance();
	
	@Override
	public CommodoreDevelopmentUtils getDevelopmentUtils() {
		return developmentUtils;
	}
	
	@Override
	public CommodoreMessengingUtils getMessengingUtils() {
		return messengingUtils;
	}

	@Override
	public CommodoreReflectionUtils getReflectionUtils() {
		return reflectionUtils;
	}
}
