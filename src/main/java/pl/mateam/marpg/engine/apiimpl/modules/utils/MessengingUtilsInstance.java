package pl.mateam.marpg.engine.apiimpl.modules.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils;

public class MessengingUtilsInstance implements CommodoreMessengingUtils {
	private static final String MaRPG = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MaRPG] ";
	
	@Override
	public MessageBuilder craftBuilder(Player messageReceiver) {
		if(messageReceiver == null)
			return new MessageBuilderImplementation(ChatColor.GOLD, ChatColor.YELLOW, ChatColor.DARK_RED, ChatColor.RED, ChatColor.DARK_GREEN, ChatColor.GREEN) {
				@Override
				public void send() {
					Bukkit.getServer().getConsoleSender().sendMessage(buffer);
				}
			};
		else
			return new MessageBuilderImplementation(ChatColor.GRAY, ChatColor.WHITE, ChatColor.DARK_RED, ChatColor.RED, ChatColor.DARK_GREEN, ChatColor.GREEN) {
				@Override
				public void send() {
					messageReceiver.sendMessage(MaRPG + buffer);
				}
			};
	}

	private abstract class MessageBuilderImplementation implements MessageBuilder {
		public String buffer = "";
		private ChatColor casual, casualHighlighted;
		private ChatColor error, errorHighlighted;
		private ChatColor success, successHighlighted;
		private ChatColor lastColor;
		
		public MessageBuilderImplementation(ChatColor casual, ChatColor casualHighlighted,
				ChatColor error, ChatColor errorHighlighted, ChatColor success, ChatColor successHighlighted) {
			this.casual = casual;
			this.casualHighlighted = casualHighlighted;
			this.error = error;
			this.errorHighlighted = errorHighlighted;
			this.success = success;
			this.successHighlighted = successHighlighted;
			colorCasual();
		}
		
		@Override
		public MessageBuilder append(String text) {
			buffer += text;
			return this;
		}

		@Override
		public MessageBuilder colorCasual() {
			append(casual.toString());
			lastColor = casual;
			return this;
		}

		@Override
		public MessageBuilder colorCasualHighlighted() {
			append(casualHighlighted.toString());
			lastColor = casualHighlighted;
			return this;
		}

		@Override
		public MessageBuilder colorError() {
			append(error.toString());
			lastColor = error;
			return this;
		}

		@Override
		public MessageBuilder colorErrorHighlighted() {
			append(errorHighlighted.toString());
			lastColor = errorHighlighted;
			return this;
		}

		@Override
		public MessageBuilder colorSuccess() {
			append(success.toString());
			lastColor = success;
			return this;
		}

		@Override
		public MessageBuilder colorSuccessHighlighted() {
			append(successHighlighted.toString());
			lastColor = successHighlighted;
			return this;
		}

		@Override
		public MessageBuilder obfuscated() {
			append(ChatColor.MAGIC.toString());
			return this;
		}

		@Override
		public MessageBuilder bold() {
			append(ChatColor.RESET.toString());
			return this;
		}

		@Override
		public MessageBuilder strikethrough() {
			append(ChatColor.STRIKETHROUGH.toString());
			return this;
		}

		@Override
		public MessageBuilder underline() {
			append(ChatColor.UNDERLINE.toString());
			return this;
		}

		@Override
		public MessageBuilder italic() {
			append(ChatColor.ITALIC.toString());
			return this;
		}

		@Override
		public MessageBuilder formatReset() {
			append(ChatColor.RESET.toString());
			append(lastColor.toString());
			return this;
		}
	}
}
