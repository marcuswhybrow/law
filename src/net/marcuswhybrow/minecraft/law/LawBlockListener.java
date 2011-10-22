package net.marcuswhybrow.minecraft.law;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class LawBlockListener extends BlockListener {
	private Law plugin;
	
	public LawBlockListener(Law plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		this.plugin.logMessage("Block prevented from being broken!");
		event.setCancelled(true);
		super.onBlockBreak(event);
	}
}
