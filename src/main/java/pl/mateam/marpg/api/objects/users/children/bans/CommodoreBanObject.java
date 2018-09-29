package pl.mateam.marpg.api.objects.users.children.bans;

import java.util.Date;

public interface CommodoreBanObject {
	public static enum BanScope {
		GLOBAL("Global"),
		CHAT_MAIN("ChatMain"),
		CHAT_TRADE("ChatTrade"),
		CHAT_PRIVATE("ChatPrivate");
		
		//It's just value of toString() at the moment
		public static BanScope fromSQL(String fieldValue) {
			try {
				return BanScope.valueOf(fieldValue);
			} catch(Exception e) {
				return null;
			}
		}
		
		public String toSQL() {
			return toString();
		}
		
		private String configurationPath;
		private BanScope(String configurationPath) {
			this.configurationPath = configurationPath;
		}
		
		public String getConfigurationPath() {
			return configurationPath;
		}
	}
	
	String getWhoBanned();
	Date getDate();
	Date getExpirationDate();
	String getReason();
	String getFormattedReason();
}