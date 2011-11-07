package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawPrisonCellMoveEvent extends LawPrisonCellEvent {
	private static final long serialVersionUID = -5207298816866713213L;

	public LawPrisonCellMoveEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCellMoveEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrisonCell(prisonCell);
	}

}
