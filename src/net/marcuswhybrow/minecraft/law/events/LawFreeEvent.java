package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LawFreeEvent extends LawPrisonDetaineeEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public LawFreeEvent(final Player sourcePlayer, final PrisonDetainee detainee) {
		super("LawFreeReleaseEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setDetainee(detainee);
	}
	
	public LawFreeEvent(final PrisonDetainee detainee) {
		this(null, detainee);
	}
}
