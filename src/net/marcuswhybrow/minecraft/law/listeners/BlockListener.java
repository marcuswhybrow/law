package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends org.bukkit.event.block.BlockListener {
	private Law law = Law.get();
	
	@Override
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
		super.onBlockBreak(event);
	}
	
	@Override
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
		super.onBlockPlace(event);
	}
}
