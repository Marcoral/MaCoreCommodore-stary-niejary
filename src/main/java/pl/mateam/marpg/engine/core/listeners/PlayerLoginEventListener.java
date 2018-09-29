package pl.mateam.marpg.engine.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.submodules.users.UsersManagerInstance;

public class PlayerLoginEventListener implements Listener {
	/* Raw PlayerInstance should be assembled with Bukkit player fastest as possible
	 * - therefore I used LOWEST */
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerLoginEvent event) {
		UsersManagerInstance manager = ((UsersManagerInstance) Commodore.getUsersManager());
		manager.handle(event);	//Building object
		manager.actionOnAllOnlinePlayers(playerObject -> {
			Player player = playerObject.getBukkitPlayer();
			player.hidePlayer(Initializer.getInstance(), event.getPlayer());
		});
	}
}