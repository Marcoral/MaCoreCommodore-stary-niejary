package pl.mateam.marpg.engine.core.commands;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBansBuilder;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBansBuilder.BanBlendMode;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBRetrieval;
import pl.mateam.marpg.engine.core.objects.users.children.bans.BanObjectInstance;

public class CommodoreCommandBan implements CommodoreGenericCommand {
	private static final String VALID_USAGE = "{0} <nickname> [-s <scope>] [-r <reason | reasonID>] [-t <duration>] [-b <blendmode>]";
	
	private CommandSender executor;
	private String[] args;
	private BanScope scope;
	private BanBlendMode blendMode;
	private int reasonID = 0;
	private String reason = null;
	private Date expirationDate = null;
	
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		if(args.length == 0)
			Commodore.getUtils().getCommandsUtils().notifyWrongUsage("No nickname specified.",
					MessageFormat.format(VALID_USAGE, usedLabel), executor);
		else {
			this.executor = executor;
			this.args = args;
			
			this.scope = getScope();
			if(scope == null) {
				informScopeInvalid();
				return;
			}
			
			this.blendMode = getBlendMode();
			if(blendMode == null) {
				informBlendInvalid();
				return;
			}
			
			String reasonString = Commodore.getUtils().getCommandsUtils().getParameterValue(args, "-r");
			if(reasonString != null) {
				try {
					this.reasonID = Integer.parseInt(reasonString);
					if(Commodore.getBansManager().getReasonFor(scope, reasonID) == null) {
						informReasonIDInvalid();
						return;
					}
				} catch(NumberFormatException e) {
					reason = reasonString;
				}
			}
						
			this.expirationDate = getExpirationDate();
			
			if(reason == null)
				Commodore.getUsersManager().setBannedAndCheck(args[0], scope, reasonID,
						this::buildBan, this::handleResponse);
			else
				Commodore.getUsersManager().setBannedAndCheck(args[0], scope, reason,
						this::buildBan, this::handleResponse);			
		}
	}
	
	private void buildBan(CommodoreBansBuilder banBuilder) {
		banBuilder.setExpirationDate(expirationDate, blendMode);
		if(executor instanceof Player)
			banBuilder.setWhoBanned(executor.getName());
	}
	
	private void handleResponse(boolean response) {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		if(!response)
			messenger.craftCommandOutputBuilder(executor)
			.colorErrorHighlighted(args[0])
			.colorError(" haven't ever been there!").send();
		else {
			messenger.craftCommandOutputBuilder(executor)
			.colorSuccessHighlighted(args[0])
			.colorSuccess(" have been banned!").send();
			
			messenger.craftCommandOutputBuilder(executor)
			.colorCasualHighlighted("Scope: ").colorCasual(scope.toSQL()).send();
			
			String reason = this.reason != null? this.reason : Commodore.getBansManager().getReasonFor(scope, reasonID);
			messenger.craftCommandOutputBuilder(executor)
			.colorCasualHighlighted("Reason: ").colorCasual(reason).send();
			
			if(expirationDate == null)
				sendExpirationDateMessage();
			else
				Commodore.getDatabase().performActionOnData(resultSet -> {
					try {
						resultSet.next();
						CommodoreBanObject ban = BanObjectInstance.fromDatabase(resultSet);
						Bukkit.getScheduler().runTask(Initializer.getInstance(), () -> {
							expirationDate = ban.getExpirationDate();
							sendExpirationDateMessage();
						});
					} catch (SQLException e) {
						ControlPanel.exceptionThrown(e);
					}
				}, CoreDBRetrieval.PLAYER_BANINFO, args[0], scope.toSQL());
		}
	}
	
	private void sendExpirationDateMessage() {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		String expires = expirationDate != null? expirationDate.toString() : "Never!";
		messenger.craftCommandOutputBuilder(executor)
		.colorCasualHighlighted("Expires: ").colorCasual(expires).send();
	}
	
	private BanScope getScope() {
		BanScope scope = BanScope.GLOBAL;
		String scopeString = Commodore.getUtils().getCommandsUtils().getParameterValue(args, "-s");
		if(scopeString != null)
			scope = BanScope.fromSQL(scopeString);
		return scope;
	}
	
	private BanBlendMode getBlendMode() {
		BanBlendMode blend = BanBlendMode.OVERWRITE_EXISTING;
		String blendString = Commodore.getUtils().getCommandsUtils().getParameterValue(args, "-b");
		if(blendString != null)
			blend = BanBlendMode.fromString(blendString);
		return blend;
	}
	
	private Date getExpirationDate() {
		Date expirationDate = null;
		Duration duration = Commodore.getUtils().getCommandsUtils().getDuration(args, "-t");
		if(duration != null)
			expirationDate = Date.from(Instant.now().plus(duration));
		return expirationDate;
	}
	
	private void informScopeInvalid() {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		messenger.craftCommandOutputBuilder(executor).colorError("Error: Given invalid ban scope.").send();
		messenger.craftCommandOutputBuilder(executor).colorError("Valid ones:").send();
		StringBuilder builder = new StringBuilder();
		for(BanScope banScope : BanScope.values()) {
			builder.append(banScope.toSQL());
			builder.append(", ");
		}
		builder.setLength(builder.length() - 2);
		messenger.craftCommandOutputBuilder(executor).colorErrorHighlighted(builder.toString()).send();
	}
	
	private void informBlendInvalid() {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		messenger.craftCommandOutputBuilder(executor).colorError("Error: Given invalid blend mode.").send();
		messenger.craftCommandOutputBuilder(executor).colorError("Valid ones:").send();
		StringBuilder builder = new StringBuilder();
		for(BanBlendMode blendMode : BanBlendMode.values()) {
			builder.append(blendMode.getDisplayedName());
			builder.append(", ");
		}
		builder.setLength(builder.length() - 2);
		messenger.craftCommandOutputBuilder(executor).colorErrorHighlighted(builder.toString()).send();
	}
	
	private void informReasonIDInvalid() {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		messenger.craftCommandOutputBuilder(executor).colorError("Error: Passed reason ID is invalid for given scope.").send();
	}
}
