package pl.mateam.marpg.api.submodules.utils;

public interface CommodoreUtilsParsing {
	String getTextTime(int timeInSeconds);
	String getTextTime(int timeInSeconds, int displayedNamesCount);
	String getTruncatedTextTime(int timeInSeconds, int grade);
	String getTruncatedTextTime(int timeInSeconds, int grade, int displayedNamesCount);
}
