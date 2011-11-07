package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawPrisonCellDeleteEvent extends LawEvent {
	private static final long serialVersionUID = -3495134639702762260L;
	
	/** The player which has deleted the prison cell. */
	private Player sourcePlayer;
	/** The prison cell which has deleted the prison cell. */
	private PrisonCell prisonCell;

	public LawPrisonCellDeleteEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCellDeleteEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrisonCell(prisonCell);
	}

	/**
	 * @return the sourcePlayer
	 */
	public Player getSourcePlayer() {
		return sourcePlayer;
	}

	/**
	 * @param sourcePlayer the sourcePlayer to set
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
