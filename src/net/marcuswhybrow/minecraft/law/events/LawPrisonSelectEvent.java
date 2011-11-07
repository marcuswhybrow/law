package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.Prison;

import org.bukkit.entity.Player;

public class LawPrisonSelectEvent extends LawPrisonEvent {
	private static final long serialVersionUID = 5433822550569039543L;

	public LawPrisonSelectEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonSelectEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}

}
