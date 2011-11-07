package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.entity.Player;

import net.marcuswhybrow.minecraft.law.prison.Prison;

public class LawPrisonCreateEvent extends LawPrisonEvent {
	private static final long serialVersionUID = -8892599862223101579L;

	public LawPrisonCreateEvent(Player sourcePlayer, Prison prison) {
		super("LawPrisonCreateEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}
}
