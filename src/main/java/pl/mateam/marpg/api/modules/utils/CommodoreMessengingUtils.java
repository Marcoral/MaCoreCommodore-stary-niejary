package pl.mateam.marpg.api.modules.utils;

import org.bukkit.entity.Player;

public interface CommodoreMessengingUtils {
	public interface MessageBuilder {
		void send();
		MessageBuilder append(String text);
		MessageBuilder colorCasual();
		MessageBuilder colorCasualHighlighted();
		MessageBuilder colorError();
		MessageBuilder colorErrorHighlighted();
		MessageBuilder colorSuccess();
		MessageBuilder colorSuccessHighlighted();
		MessageBuilder obfuscated();
		MessageBuilder bold();
		MessageBuilder strikethrough();
		MessageBuilder underline();
		MessageBuilder italic();
		MessageBuilder formatReset();
	}
	
	MessageBuilder craftBuilder(Player messageReceiver);
	default void sendCasual(String message, Player messageReceiver) {
		craftBuilder(messageReceiver).append(message).send();
	}
	default void sendCasualWithHighlight(String part1, String highlightedPart, String part2, Player messageReceiver) {
		craftBuilder(messageReceiver).append(part1).append(highlightedPart).append(part2).send();
	}
	default void sendError(String message, Player messageReceiver) {
		craftBuilder(messageReceiver).colorError().append(message).send();
	}
	default void sendErrorWithHighlight(String part1, String highlightedPart, String part2, Player messageReceiver) {
		craftBuilder(messageReceiver).colorError().append(part1).colorErrorHighlighted()
		.append(highlightedPart).colorError().append(part2).send();
	}
	default void sendSuccess(String message, Player messageReceiver) {
		craftBuilder(messageReceiver).colorSuccess().append(message).send();
	}
	default void sendSuccessWithHighlight(String part1, String highlightedPart, String part2, Player messageReceiver) {
		craftBuilder(messageReceiver).colorSuccess().append(part1).colorSuccessHighlighted()
		.append(highlightedPart).colorSuccess().append(part2).send();
	}
}
