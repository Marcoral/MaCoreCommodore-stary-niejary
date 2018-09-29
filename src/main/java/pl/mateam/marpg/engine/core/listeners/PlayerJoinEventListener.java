package pl.mateam.marpg.engine.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(PlayerJoinEvent e) {
		e.setJoinMessage(null);
	}
}
