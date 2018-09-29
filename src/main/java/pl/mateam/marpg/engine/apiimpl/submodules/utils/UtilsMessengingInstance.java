package pl.mateam.marpg.engine.apiimpl.submodules.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.superclasses.LogLevel;

public class UtilsMessengingInstance implements CommodoreUtilsMessenging {
	@Override
	public MessageBuilder craftMessageBuilder(Player receiver) {
		return new AbstractMessageBuilder(ChatColor.GRAY, ChatColor.WHITE, ChatColor.DARK_RED, ChatColor.RED, ChatColor.DARK_GREEN, ChatColor.GREEN) {
			private final String MaRPG = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MaRPG] ";
			
			@Override
			public void send() {
				receiver.sendMessage(MaRPG + toString());
			}
		};
	}

	@Override
	public MessageBuilder craftLogger(Logger logger, LogLevel level) {
		return new AbstractMessageBuilder(ChatColor.GOLD, ChatColor.YELLOW, ChatColor.DARK_RED, ChatColor.RED, ChatColor.DARK_GREEN, ChatColor.GREEN) {			
			//Carefully! This method should not be invoked before LoggerManager is set up!
			@Override
			public void send() {
				Commodore.getLoggersManager().log(toString(), logger, level);
			}
		};
	}
	
	@Override
	public MessageBuilder craftCommandOutputBuilder(CommandSender commandSender) {
		if(commandSender instanceof Player) {
			return new AbstractMessageBuilder(ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.AQUA) {
				@Override
				public void send() {
					commandSender.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "[GM helper] " + toString());
				}
			};
		} else {
			return new AbstractMessageBuilder(ChatColor.GOLD, ChatColor.YELLOW, ChatColor.DARK_RED, ChatColor.RED, ChatColor.DARK_GREEN, ChatColor.GREEN) {
				@Override
				public void send() {
					commandSender.sendMessage(toString());
				}
			};
		}
	}
	
	private static abstract class AbstractMessageBuilder implements MessageBuilder {
		private final ChatColor COLOR_CASUAL;
		private final ChatColor COLOR_CASUAL_HIGHLIGHTED;
		private final ChatColor COLOR_ERROR;
		private final ChatColor COLOR_ERROR_HIGHLIGHTED;
		private final ChatColor COLOR_SUCCESS;
		private final ChatColor COLOR_SUCCESS_HIGHLIGHTED;
		
		private AbstractMessageBuilder(ChatColor colorCasual, ChatColor colorCasualHighlighted, ChatColor colorError,
				ChatColor colorErrorHighlighted, ChatColor colorSuccess, ChatColor colorSuccessHighlighted) {
			this.COLOR_CASUAL = colorCasual;
			this.COLOR_CASUAL_HIGHLIGHTED = colorCasualHighlighted;
			this.COLOR_ERROR = colorError;
			this.COLOR_ERROR_HIGHLIGHTED = colorErrorHighlighted;
			this.COLOR_SUCCESS = colorSuccess;
			this.COLOR_SUCCESS_HIGHLIGHTED = colorSuccessHighlighted;
		}
				
		public StringBuilder buffer = new StringBuilder();
		private ChatColor lastColor;
		private ChatColor lastFormat;
		
		@Override
		public String toString() {
			return buffer.toString();
		}
		
		@Override
		public MessageBuilder append(String text) {
			buffer.append(text);
			return this;
		}
		
		private MessageBuilder changeColor(ChatColor newColor) {
			if(lastColor == newColor)
				return this;
			lastColor = newColor;
			buffer.append(lastColor.toString());
			if(lastFormat != null)
				buffer.append(lastFormat.toString());
			return this;
		}
		
		private MessageBuilder changeFormat(ChatColor newFormat) {
			if(lastFormat == newFormat)
				return this;
			lastFormat = newFormat;
			buffer.append(lastColor.toString());
			buffer.append(lastFormat.toString());
			return this;
		}

		@Override
		public MessageBuilder colorCasual() {
			return changeColor(COLOR_CASUAL);
		}

		@Override
		public MessageBuilder colorCasualHighlighted() {
			return changeColor(COLOR_CASUAL_HIGHLIGHTED);
		}

		@Override
		public MessageBuilder colorError() {
			return changeColor(COLOR_ERROR);
		}

		@Override
		public MessageBuilder colorErrorHighlighted() {
			return changeColor(COLOR_ERROR_HIGHLIGHTED);
		}

		@Override
		public MessageBuilder colorSuccess() {
			return changeColor(COLOR_SUCCESS);
		}

		@Override
		public MessageBuilder colorSuccessHighlighted() {
			return changeColor(COLOR_SUCCESS_HIGHLIGHTED);
		}

		@Override
		public MessageBuilder obfuscated() {
			return changeFormat(ChatColor.MAGIC);
		}

		@Override
		public MessageBuilder bold() {
			return changeFormat(ChatColor.BOLD);
		}

		@Override
		public MessageBuilder strikethrough() {
			return changeFormat(ChatColor.STRIKETHROUGH);
		}

		@Override
		public MessageBuilder underline() {
			return changeFormat(ChatColor.UNDERLINE);
		}

		@Override
		public MessageBuilder italic() {
			return changeFormat(ChatColor.ITALIC);
		}

		@Override
		public MessageBuilder formatReset() {
			if(lastFormat == null)
				return this;
			lastFormat = null;
			buffer.append(ChatColor.RESET.toString());
			if(lastColor != null)
				buffer.append(lastColor);
			return this;
		}

		@Override
		public MessageBuilder colorCasual(String text) {
			colorCasual();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder colorCasualHighlighted(String text) {
			colorCasualHighlighted();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder colorError(String text) {
			colorError();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder colorErrorHighlighted(String text) {
			colorErrorHighlighted();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder colorSuccess(String text) {
			colorSuccess();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder colorSuccessHighlighted(String text) {
			colorSuccessHighlighted();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder obfuscated(String text) {
			obfuscated();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder bold(String text) {
			bold();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder strikethrough(String text) {
			strikethrough();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder underline(String text) {
			underline();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder italic(String text) {
			italic();
			append(text);
			return this;
		}

		@Override
		public MessageBuilder formatReset(String text) {
			formatReset();
			append(text);
			return this;
		}
	}
}
