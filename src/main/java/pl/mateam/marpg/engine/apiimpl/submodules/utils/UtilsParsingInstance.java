package pl.mateam.marpg.engine.apiimpl.submodules.utils;

import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsParsing;

public class UtilsParsingInstance implements CommodoreUtilsParsing {
	@Override
	public String getTextTime(int timeInSeconds) {
		return TextToTimeAuxiliary.get(timeInSeconds, 4, 4);
	}
	
	@Override
	public String getTextTime(int timeInSeconds, int displayedNamesCount) {
		return TextToTimeAuxiliary.get(timeInSeconds, displayedNamesCount, 4);
	}
	
	@Override
	public String getTruncatedTextTime(int timeInSeconds, int grade) {
		return TextToTimeAuxiliary.get(timeInSeconds, 4, grade);
	}

	@Override
	public String getTruncatedTextTime(int timeInSeconds, int grade, int displayedNamesCount) {
		return TextToTimeAuxiliary.get(timeInSeconds, displayedNamesCount, grade);
	}

	private static class TextToTimeAuxiliary {
		private int grade;
		private int secondsLeft;
		private int namesLeftToDisplay;
		private StringBuilder result;
		
		public static String get(int timeInSeconds, int displayedNamesCount, int grade) {
			if(timeInSeconds <= 0 || grade <= 0)
				return "";
			TextToTimeAuxiliary instance = new TextToTimeAuxiliary(timeInSeconds, displayedNamesCount, grade);
			instance.appendTextTime("day", 86400);
			instance.appendTextTime("hour", 3600);
			instance.appendTextTime("minute", 60);
			instance.appendTextTime("second", 1);
			if(instance.result.length() >= 2)
				instance.result.setLength(instance.result.length()-2);
			return instance.result.toString();
		}
		
		private TextToTimeAuxiliary(int timeInSeconds, int displayedNamesCount, int grade) {
			this.grade = grade;
			this.secondsLeft = timeInSeconds;
			this.namesLeftToDisplay = displayedNamesCount;
			this.result = new StringBuilder();
		}
		
		private void appendTextTime(String singularForm, int durationInSeconds) {
			if(namesLeftToDisplay <= 0 || grade <= 0)
				return;
			grade--;
			int count = secondsLeft / durationInSeconds;
			this.secondsLeft %= durationInSeconds;
			switch(count) {
				case 0:
					return;
				case 1:
					result.append(count);
					result.append(" ");
					result.append(singularForm);
					result.append(", ");
					namesLeftToDisplay--;
					return;
				default:
					result.append(count);
					result.append(" ");
					result.append(singularForm);
					result.append("s, ");
					namesLeftToDisplay--;
					return;
			}
		}
	}
}
