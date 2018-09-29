package pl.mateam.marpg.engine.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.objects.users.CommodoreUser;

public class PlayerQuitEventListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		CommodoreUser user = Commodore.getUsersManager().getOnlineUser(event.getPlayer().getName());
		user.save();
	}
}
