package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

public class LawPrisonDetaineeEvent extends LawCommandEvent {
	private static final long serialVersionUID = -6190963474099604979L;
	
	/** The prison detainee which is the subject of this event. */
	private PrisonDetainee detainee;

	public LawPrisonDetaineeEvent(String event) {
		super(event);
	}

	/**
	 * @return the detainee
	 */
	public PrisonDetainee getDetainee() {
		return detainee;
	}

	/**
	 * @param detainee the detainee to set
	 */
	public void setDetainee(PrisonDetainee detainee) {
		this.detainee = detainee;
	}

}
