package pl.mateam.marpg.api.objects.users;

import org.bukkit.entity.Player;

public interface CommodoreUser {
	Player getBukkitPlayer();
	boolean passwordMatches(String password);
	
	void save();
}
