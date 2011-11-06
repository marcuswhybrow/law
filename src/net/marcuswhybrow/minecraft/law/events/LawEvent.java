package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class LawEvent extends Event implements Cancellable {
	private static final long serialVersionUID = -108929565086972620L;
	/** True if the event has been cancelled. */
	private boolean isCancelled;

	public LawEvent(String event) {
		super(event);
		this.setCancelled(false);
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
}
