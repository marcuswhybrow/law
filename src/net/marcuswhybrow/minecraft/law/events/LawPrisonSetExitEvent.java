package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.Prison;

import org.bukkit.entity.Player;

public class LawPrisonSetExitEvent extends LawPrisonEvent {
	private static final long serialVersionUID = 5169894301808598974L;

	public LawPrisonSetExitEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonSetExitEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}

}
