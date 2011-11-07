package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawFreeReleaseEvent extends LawCommandEvent {
	private static final long serialVersionUID = -1726278830957239690L;
	
	/** The player which is being freed from prison. */
	private Player targetPlayer;
	/** The prison cell which the target player is being freed from. */
	private PrisonCell prisonCell;

	public LawFreeReleaseEvent(final Player sourcePlayer, final Player targetPlayer, final PrisonCell prisonCell) {
		super("LawFreeReleaseEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setTargetPlayer(targetPlayer);
		this.setPrisonCell(prisonCell);
	}
	
	public LawFreeReleaseEvent(final Player targetPlayer, final PrisonCell prisonCell) {
		this(null, targetPlayer, prisonCell);
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
