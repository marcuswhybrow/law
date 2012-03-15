package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.Prison;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LawPrisonSelectEvent extends LawPrisonEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public LawPrisonSelectEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonSelectEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}
}
