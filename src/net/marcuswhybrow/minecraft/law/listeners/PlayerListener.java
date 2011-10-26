package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		LawWorld lawWorld = Law.get().getLawWorld(player.getWorld());
		
		if (lawWorld == null) {
			// This could only happen if a World was added at runtime
			return;
		}
		
		Location location = lawWorld.removeLatentTeleport(player.getName());
		
		if (location == null) {
			return;
		}
		
		player.teleport(location);
		
		lawWorld.save();
	}
}
