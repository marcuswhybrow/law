package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

/**
 * Represents the event triggered when a player is imprisoned.
 * The plugin has yet to check whether the player is actually
 * online and thus has not effect the actual player in anyway.
 * 
 * The {@link LawImprisonSecureEvent} class represents when an in-game
 * player is actually imprisoned. 
 * 
 * @author Marcus Whybrow
 *
 */
public class LawImprisonEvent extends LawCommandEvent {
	private static final long serialVersionUID = -6159307465018927999L;
	
	/** The name of the player that is being imprisoned. */
	private String targetPlayerName;
	/** The prison cell that the target player is imprisoned within. */
	private PrisonCell prisonCell;

	public LawImprisonEvent(final Player sourcePlayer, final String targetPlayerName, final PrisonCell prisonCell) {
		super("LawImprisonEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setTargetPlayerName(targetPlayerName);
		this.setPrisonCell(prisonCell);
	}

	public String getTargetPlayerName() {
		return targetPlayerName;
	}

	public void setTargetPlayerName(String targetPlayerName) {
		this.targetPlayerName = targetPlayerName;
	}

	public PrisonCell getPrisonCell() {
		return prisonCell;
	}

	public void setPrisonCell(PrisonCell prisonCell) {
		this.prisonCell = prisonCell;
	}
}
