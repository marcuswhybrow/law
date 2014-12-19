package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.Prison;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LawPrisonSetExitEvent extends LawPrisonEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public LawPrisonSetExitEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonSetExitEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}
}
