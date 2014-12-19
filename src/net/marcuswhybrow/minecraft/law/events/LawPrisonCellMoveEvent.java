package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LawPrisonCellMoveEvent extends LawPrisonCellEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public LawPrisonCellMoveEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCellMoveEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrisonCell(prisonCell);
	}
}
