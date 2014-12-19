package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.Prison;

public abstract class LawPrisonEvent extends LawCommandEvent {
	
	/** The prison effected by this event. */
	private Prison prison;

	public LawPrisonEvent(String event) {
		super(event);
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
