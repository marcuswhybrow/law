package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

import org.bukkit.entity.Player;

public class LawDetainEndEvent extends LawPrisonDetaineeEvent {
	private static final long serialVersionUID = 2960186025135779032L;

	public LawDetainEndEvent(final Player sourcePlayer, final PrisonDetainee detainee) {
		super("LawFreeEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setDetainee(detainee);
	}
	
	public LawDetainEndEvent(final PrisonDetainee detainee) {
		this(null, detainee);
	}
}
