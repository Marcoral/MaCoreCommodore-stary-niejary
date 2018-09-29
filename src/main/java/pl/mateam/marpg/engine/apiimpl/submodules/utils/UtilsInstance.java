package pl.mateam.marpg.engine.apiimpl.submodules.utils;

import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsDevelopment;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsParsing;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsCommands;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsReflection;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtils;

public class UtilsInstance implements CommodoreUtils {
	private CommodoreUtilsCommands commandsUtils = new UtilsCommandsInstance();
	private CommodoreUtilsDevelopment developmentUtils = new UtilsDevelopmentInstance();
	private CommodoreUtilsMessenging messengingUtils = new UtilsMessengingInstance();
	private CommodoreUtilsParsing parsingUtils = new UtilsParsingInstance();
	private CommodoreUtilsReflection reflectionUtils = new UtilsReflectionInstance();
	
	@Override
	public CommodoreUtilsCommands getCommandsUtils() {
		return commandsUtils;
	}
	
	@Override
	public CommodoreUtilsDevelopment getDevelopmentUtils() {
		return developmentUtils;
	}
	
	@Override
	public CommodoreUtilsMessenging getMessengingUtils() {
		return messengingUtils;
	}
	
	@Override
	public CommodoreUtilsParsing getParsingUtils() {
		return parsingUtils;
	}
	
	@Override
	public CommodoreUtilsReflection getReflectionUtils() {
		return reflectionUtils;
	}
}
