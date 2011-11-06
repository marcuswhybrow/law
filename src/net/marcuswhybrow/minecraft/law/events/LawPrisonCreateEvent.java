package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.entity.Player;

import net.marcuswhybrow.minecraft.law.prison.Prison;

public class LawPrisonCreateEvent extends LawEvent {
	private static final long serialVersionUID = -8892599862223101579L;
	
	/** The player creating the new prison. */
	private Player sourcePlayer;
	/** The prison created by the source player. */
	private Prison createdPrison;

	public LawPrisonCreateEvent(Player sourcePlayer, Prison createdPrison) {
		super("LawPrisonCreateEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setCreatedPrison(createdPrison);
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
	 * @return the createdPrison
	 */
	public Prison getCreatedPrison() {
		return createdPrison;
	}

	/**
	 * @param createdPrison the createdPrison to set
	 */
	public void setCreatedPrison(Prison createdPrison) {
		this.createdPrison = createdPrison;
	}
}
