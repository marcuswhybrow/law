package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockListener implements Listener {
	private Law law = Law.get();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		LawWorld lawWorld;
		
		if (player != null) {
			lawWorld = law.getLawWorldForPlayer(player);
			if (lawWorld != null) {
				if (lawWorld.hasPrisoner(player.getName())) {
					event.setCancelled(true);
					MessageDispatcher.sendMessage(player, Colorise.error("You cannot break blocks as a prisoner"));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		LawWorld lawWorld;
		
		if (player != null) {
			lawWorld = law.getLawWorldForPlayer(player);
			if (lawWorld != null) {
				if (lawWorld.hasPrisoner(player.getName())) {
					event.setCancelled(true);
					MessageDispatcher.sendMessage(player, Colorise.error("You cannot place blocks as a prisoner"));
				}
			}
		}
	}
}
