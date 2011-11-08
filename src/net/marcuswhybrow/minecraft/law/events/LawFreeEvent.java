package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

import org.bukkit.entity.Player;

public class LawFreeEvent extends LawPrisonDetaineeEvent {
	private static final long serialVersionUID = -1726278830957239690L;
	
	public LawFreeEvent(final Player sourcePlayer, final PrisonDetainee detainee) {
		super("LawFreeReleaseEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setDetainee(detainee);
	}
	
	public LawFreeEvent(final PrisonDetainee detainee) {
		this(null, detainee);
	}

}
