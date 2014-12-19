package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.marcuswhybrow.minecraft.law.prison.Prison;

public class LawPrisonCreateEvent extends LawPrisonEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public LawPrisonCreateEvent(Player sourcePlayer, Prison prison) {
		super("LawPrisonCreateEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}
}
