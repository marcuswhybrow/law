package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

import org.bukkit.entity.Player;

public class LawImprisonEvent extends LawPrisonDetaineeEvent {
	private static final long serialVersionUID = 7841604312008447536L;

	public LawImprisonEvent(final Player sourcePlayer, final PrisonDetainee detainee) {
		super("LawImprisonSecureEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setDetainee(detainee);
	}
	
	public LawImprisonEvent(final PrisonDetainee detainee) {
		this(null, detainee);
	}
}
