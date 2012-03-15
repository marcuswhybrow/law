package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LawDetainEndEvent extends LawPrisonDetaineeEvent {
	private static final HandlerList handlers = new HandlerList();

	public LawDetainEndEvent(final Player sourcePlayer, final PrisonDetainee detainee) {
		super("LawFreeEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setDetainee(detainee);
	}
	
	public LawDetainEndEvent(final PrisonDetainee detainee) {
		this(null, detainee);
	}
}
