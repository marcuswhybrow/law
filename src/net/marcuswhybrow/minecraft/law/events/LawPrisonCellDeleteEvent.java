package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LawPrisonCellDeleteEvent extends LawPrisonCellEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public LawPrisonCellDeleteEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCellDeleteEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrisonCell(prisonCell);
	}
}
