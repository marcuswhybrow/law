package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawFreeEvent extends LawCommandEvent {
	private static final long serialVersionUID = 2960186025135779032L;
	
	/** The name of the target player which will be freed. */
	private String targetPlayerName;
	/** The prison cell which the target player will be freed from. */
	private PrisonCell prisonCell;

	public LawFreeEvent(final Player sourcePlayer, final String targetPlayerName, final PrisonCell prisonCell) {
		super("LawFreeEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setTargetPlayerName(targetPlayerName);
		this.setPrisonCell(prisonCell);
	}

	/**
	 * @return the targetPlayerName
	 */
	public String getTargetPlayerName() {
		return targetPlayerName;
	}

	/**
	 * @param targetPlayerName the targetPlayerName to set
	 */
	public void setTargetPlayerName(String targetPlayerName) {
		this.targetPlayerName = targetPlayerName;
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
