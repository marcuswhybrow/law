package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.entity.Player;

import net.marcuswhybrow.minecraft.law.prison.Prison;

public class LawPrisonDeleteEvent extends LawEvent {
	private static final long serialVersionUID = -8999110569484208471L;
	
	/** The player which has deleted the prison */
	private Player sourcePlayer;
	/** The prison which has been deleted. */
	private Prison prison;

	public LawPrisonDeleteEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonDeleteEvent");
		
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
