package pl.mateam.marpg.engine.core.objects.users.children.bans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager;
import pl.mateam.marpg.api.submodules.text.TextNode;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBField;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBInsertion;

public class BanObjectInstance implements CommodoreBanObject {
	private BanScope scope;

	private String whoBanned;
	private Date date = new Date();
	private Date expires;
	private Integer reasonID;
	private String customReason;

	public static BanObjectInstance fromDatabase(ResultSet data) {
		try {
			BanScope scope = BanScope.fromSQL(data.getString(CoreDBField.BANS_SCOPE));
			Date expires = data.getTimestamp(CoreDBField.BANS_EXPIRES);
			if (expires != null && expires.before(new Date()))
				return null;
			String whoBanned = data.getString(CoreDBField.BANS_WHOBANNED);
			Date date = data.getTimestamp(CoreDBField.BANS_DATE);
			int reasonID = data.getInt(CoreDBField.BANS_REASON);
			String customReason = data.getString(CoreDBField.BANS_CUSTOMREASON);
			return new BanObjectInstance(scope, whoBanned, date, expires, reasonID, customReason);
		} catch (Exception e) {
			ControlPanel.exceptionThrown(e);
			return null;
		}
	}
	
	public void save(Connection connection, String nickname) {
		CoreUtils.executeStatementInline(connection, CoreDBInsertion.PLAYERS_BAN,
			/* nickname = */		nickname,
			/* scope = */			scope.toSQL(),
			/* who_banned = */		whoBanned,
			/* reason = */			reasonID,
			/* custom_reason = */	customReason,
			/* date = */			date,
			/* expires = */			expires,
			/* users table FK */	nickname);
	}
	
	public BanObjectInstance(String customReason) {
		this.customReason = customReason;
	}
	
	public BanObjectInstance(BanScope scope, int reasonID) {
		this.scope = scope;
		this.reasonID = reasonID;
	}

	public BanObjectInstance(BanScope scope, String whoBanned, Date expires, int reasonID) {
		this.scope = scope;
		this.whoBanned = whoBanned;
		this.expires = expires;
		this.reasonID = reasonID;
	}
	
	public BanObjectInstance(String customReason, String whoBanned, Date expires) {
		this.whoBanned = whoBanned;
		this.expires = expires;
		this.customReason = customReason;
	}
	
	private BanObjectInstance(BanScope scope, String whoBanned, Date date, Date expires, Integer reasonID,
			String customReason) {
		this.scope = scope;
		this.whoBanned = whoBanned;
		this.date = date;
		this.expires = expires;
		this.reasonID = reasonID;
		this.customReason = customReason;
	}

	@Override
	public String getWhoBanned() {
		return whoBanned;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public Date getExpirationDate() {
		return expires;
	}

	@Override
	public String getReason() {
		if (customReason != null)
			return customReason;
		return Commodore.getBansManager().getReasonFor(scope, reasonID);
	}

	@Override
	public String getFormattedReason() {
		CommodoreTextManager texts = Commodore.getTextManager();
		List<Object> arguments = new ArrayList<>();
		arguments.add(getReason());
		CoreUtils.addFormatArguments(arguments, TextNode.FORMAT_BANNED_MESSAGE_WHOBANNED, whoBanned);
		arguments.add(date);
		CoreUtils.addFormatArguments(arguments, TextNode.FORMAT_BANNED_MESSAGE_EXPIRES, expires,
				generateBanProgressBar());
		String pattern = texts.getNode(TextNode.FORMAT_BANNED_MESSAGE_TEMPLATE);
		return MessageFormat.format(pattern, arguments.toArray());
	}

	private static final int BAR_LENGTH = 25;
	private static final char BAR_CHAR = '\u258c';
	private static final ChatColor BAR_COLOR_PASSED = ChatColor.GREEN;
	private static final ChatColor BAR_COLOR_REMAINING = ChatColor.GRAY;

	private String generateBanProgressBar() {
		if (expires == null)
			return null;
		Date currentDate = new Date();
		long length = expires.getTime() - date.getTime();
		long passed = currentDate.getTime() - date.getTime();
		float percent = (float) (1D * passed / length * 100);
		StringBuilder builder = new StringBuilder(BAR_COLOR_PASSED.toString());
		int remaining = BAR_LENGTH;
		for (int i = 0; i < Math.floor(percent / 100 * BAR_LENGTH); i++, remaining--)
			builder.append(BAR_CHAR);
		builder.append(BAR_COLOR_REMAINING.toString());
		for (int i = 0; i < remaining; i++)
			builder.append(BAR_CHAR);
		builder.append(" ");
		String pattern = Commodore.getTextManager().getNode(TextNode.FORMAT_BANNED_MESSAGE_BAR_PERCENTS);
		builder.append(MessageFormat.format(pattern, new DecimalFormat("#.##").format(percent)));
		return builder.toString();
	}

	public void setWhoBanned(String nickname) {
		this.whoBanned = nickname;
	}
	
	public void setExpirationDate(Date date) {
		this.expires = date;
	}
}
