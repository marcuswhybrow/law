package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.events.LawFreeEvent;
import net.marcuswhybrow.minecraft.law.events.LawImprisonEvent;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.Settings;
import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {
	private Law law = Law.get();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		LawWorld lawWorld = law.getLawWorld(player.getWorld());
		
		if (lawWorld == null) {
			// This could only happen if a World was added at runtime
			return;
		}
		
		PrisonDetainee detainee = lawWorld.getDetainee(player.getName());
		if (detainee != null) {
			switch (detainee.getState()) {
			case TO_BE_FREED:
				Law.fireEvent(new LawFreeEvent(detainee));
				break;
			case TO_BE_IMPRISONED:
				Law.fireEvent(new LawImprisonEvent(detainee));
				break;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		LawWorld lawWorld;
		
		if (player != null) {
			lawWorld = law.getLawWorldForPlayer(player);
			if (lawWorld != null) {
				if (lawWorld.hasPrisoner(player.getName())) {
					
					if (event.hasItem()) {
						int itemInHandTypeId = event.getItem().getTypeId();
						if (Settings.get().isUsableId(itemInHandTypeId)) {
							event.setCancelled(true);
							MessageDispatcher.sendMessage(player, Colorise.error("You cannot use items as a prisoner"));
							return;
						}
					}
					
					Block clickedBlock = event.getClickedBlock();
					if (clickedBlock != null) {
						if (Settings.get().isSwitchableId(clickedBlock.getTypeId())) {
							event.setCancelled(true);
							MessageDispatcher.sendMessage(player, Colorise.error("You cannot interact as a prisoner"));
							return;
						}
					}
				}
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		LawWorld lawWorld = Law.get().getLawWorldForPlayer(player);
		
		PrisonDetainee detainee = lawWorld.getDetainee(player.getName());
		
		if (detainee != null) {
			event.setRespawnLocation(detainee.getPrisonCell().getLocation());
		}
		
	}
}
