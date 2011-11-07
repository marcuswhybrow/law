package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.Prison;

import org.bukkit.entity.Player;

public class LawPrisonSetExitEvent extends LawEvent {
	private static final long serialVersionUID = 5169894301808598974L;
	
	private Player sourcePlayer;
	private Prison prison;

	public LawPrisonSetExitEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonSetExitEvent");
		
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
