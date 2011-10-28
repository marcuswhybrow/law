package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.InventoryManager;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.Settings;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	private Law law = Law.get();
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		LawWorld lawWorld = law.getLawWorld(player.getWorld());
		
		if (lawWorld == null) {
			// This could only happen if a World was added at runtime
			return;
		}
		
		Location location = lawWorld.removeLatentTeleport(player.getName());
		
		if (location != null) {
			player.teleport(location);
			lawWorld.save();
		}
		
		InventoryManager.Action action = lawWorld.removeLatentInventoryChange(player.getName());
		if (action != null) {
			InventoryManager.preformAction(player, action);
			lawWorld.save();
		}
	}
	
	@Override
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
		
		super.onPlayerInteract(event);
	}
}
