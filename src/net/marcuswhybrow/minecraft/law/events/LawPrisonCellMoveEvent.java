package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawPrisonCellMoveEvent extends LawEvent {
	private static final long serialVersionUID = -5207298816866713213L;
	
	/** The player which moved the prison cell to a new location. */
	private Player sourcePlayer;
	/** The prison cell which has moved location. */
	private PrisonCell prisonCell;

	public LawPrisonCellMoveEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCellMoveEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrisonCell(prisonCell);
	}

	/**
	 * @return the sourcePLayer
	 */
	public Player getSourcePlayer() {
		return sourcePlayer;
	}

	/**
	 * @param sourcePLayer the sourcePLayer to set
	 */
	public void setSourcePlayer(Player sourcePlayer) {
		this.sourcePlayer = sourcePlayer;
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
