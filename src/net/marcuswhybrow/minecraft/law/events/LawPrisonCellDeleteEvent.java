package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawPrisonCellDeleteEvent extends LawPrisonCellEvent {
	private static final long serialVersionUID = -3495134639702762260L;

	public LawPrisonCellDeleteEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCellDeleteEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrisonCell(prisonCell);
	}

}
