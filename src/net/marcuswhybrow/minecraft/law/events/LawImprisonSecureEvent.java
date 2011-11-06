package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawImprisonSecureEvent extends LawEvent {
	private static final long serialVersionUID = 7841604312008447536L;
	
	/** The player which is imprisoning the target player. */
	private Player sourcePlayer;
	/** The player which is being imprisoned. */
	private Player targetPlayer;
	/** The prison cell the target player is being imprisoned within. */
	private PrisonCell prisonCell;

	public LawImprisonSecureEvent(final Player sourcePlayer, final Player targetPlayer, final PrisonCell prisonCell) {
		super("LawImprisonSecureEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setTargetPlayer(targetPlayer);
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
	 * @return the targetPlayer
	 */
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	/**
	 * @param targetPlayer the targetPlayer to set
	 */
	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
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
