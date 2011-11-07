package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.entity.Player;

import net.marcuswhybrow.minecraft.law.prison.Prison;

public class LawPrisonDeleteEvent extends LawPrisonEvent {
	private static final long serialVersionUID = -8999110569484208471L;

	public LawPrisonDeleteEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonDeleteEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}
	
}
