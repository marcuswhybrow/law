package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EntityListener extends  org.bukkit.event.entity.EntityListener {
	
	@Override
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		
		// If the human is a prisoner, do not let their hunger decrease
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player) entity;
			
			int newLevel = event.getFoodLevel();
			int oldLevel = player.getFoodLevel();
			
			// Prevent the event only if the bar is decreasing, allowing
			// players to eat.
			if (newLevel < oldLevel) {
				LawWorld lawWorld = Law.get().getLawWorldForPlayer(player);
				if (lawWorld != null) {
					if (lawWorld.hasPrisoner(player.getName())) {
						// If the player is a prisoner, then cancel the drop in the food bar
						event.setCancelled(true);
					}
				}
			}
		}
		
		super.onFoodLevelChange(event);
	}
}
