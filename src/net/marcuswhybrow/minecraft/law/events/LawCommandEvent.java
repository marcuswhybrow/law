package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.entity.Player;

public abstract class LawCommandEvent extends LawEvent {
	private static final long serialVersionUID = -6492491601711901151L;
	
	/** The player which initiated the command. */
	private Player sourcePlayer;

	public LawCommandEvent(String event) {
		super(event);
	}

	/**
	 * @return the sourcePlayer or null if event was not directly instigated by a player
	 */
	public Player getSourcePlayer() {
		return sourcePlayer;
	}

	/**
	 * @param sourcePlayer the sourcePlayer to set or null if not instigated by a player
	 */
	public void setSourcePlayer(Player sourcePlayer) {
		this.sourcePlayer = sourcePlayer;
	}

}
