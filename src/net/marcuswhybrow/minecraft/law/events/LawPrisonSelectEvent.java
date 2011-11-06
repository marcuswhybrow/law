package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.Prison;

import org.bukkit.entity.Player;

public class LawPrisonSelectEvent extends LawEvent {
	private static final long serialVersionUID = 5433822550569039543L;
	
	/** The player which is selecting a new prison. */
	private Player sourcePlayer;
	/** The prison which the source player is selecting, or null if deselecting */
	private Prison prison;

	public LawPrisonSelectEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonSelectEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
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
	 * @return the prison
	 */
	public Prison getPrison() {
		return prison;
	}

	/**
	 * @param prison the prison to set
	 */
	public void setPrison(Prison prison) {
		this.prison = prison;
	}

}
