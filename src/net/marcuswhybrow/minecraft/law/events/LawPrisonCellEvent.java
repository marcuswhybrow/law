package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.event.HandlerList;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

public abstract class LawPrisonCellEvent extends LawCommandEvent {
	private static final HandlerList handlers = new HandlerList();
	
	private PrisonCell prisonCell;

	public LawPrisonCellEvent(String event) {
		super(event);
	}

	/**
	 * @return the prisonCell
	 */
	public PrisonCell getPrisonCell() {
		return prisonCell;
	}

	/**
	 * @param prisonCell the prisonCell to set
	 */
	public void setPrisonCell(PrisonCell prisonCell) {
		this.prisonCell = prisonCell;
	}
}
