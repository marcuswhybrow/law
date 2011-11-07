package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawPrisonCellCreateEvent extends LawEvent {
	private static final long serialVersionUID = 2946894571419504837L;
	
	/** The player which created the prison cell. */
	private Player sourcePlayer;
	/** The prison cell which has been created. */
	private PrisonCell prisonCell;

	public LawPrisonCellCreateEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCelLCreateEvent");
		
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
