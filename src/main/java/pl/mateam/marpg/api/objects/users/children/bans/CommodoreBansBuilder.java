package pl.mateam.marpg.api.objects.users.children.bans;

import java.util.Date;

import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;
import pl.mateam.marpg.engine.core.internal.CoreUtils;

public interface CommodoreBansBuilder {
	CommodoreBansBuilder setWhoBanned(String nickname);
	CommodoreBansBuilder setExpirationDate(Date date, BanBlendMode blendMode);
	
	public static enum BanBlendMode {
		OVERWRITE_EXISTING("Overwrite"),
		CHOOSE_LONGER("Longer"),
		APPEND_DURATION("Append");
		
		private String displayedName;
		private BanBlendMode(String displayedName) {
			this.displayedName = displayedName;
		}
		
		public String getDisplayedName() {
			return displayedName;
		}
		
		public static BanBlendMode fromString(String blend) {
			switch(blend.toLowerCase()) {
				case "overwrite":
					return BanBlendMode.OVERWRITE_EXISTING;
				case "longer":
					return BanBlendMode.CHOOSE_LONGER;
				case "append":
					return BanBlendMode.APPEND_DURATION;
				default:
					return null;
			}
		}
		
		public Date applyTo(Date newDate, Date oldDate) {
			if(newDate == null || oldDate == null)
				return null;
			switch(this) {
				case APPEND_DURATION:
					if(newDate.after(oldDate)) {
						long remaining = Math.max(0, oldDate.getTime() - new Date().getTime());
						return Date.from(newDate.toInstant().plusMillis(remaining));
					} else {
						long remaining = Math.max(0, newDate.getTime() - new Date().getTime());
						return Date.from(oldDate.toInstant().plusMillis(remaining));
					}
				case CHOOSE_LONGER:
					return CoreUtils.maxOrFirst(newDate, oldDate);
				case OVERWRITE_EXISTING:
					return newDate;
				default:
					throw new CommodoreRuntimeException("Unknown BanBlendMode!");
			}
		}
	}
}
