package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.event.Event;

public abstract class LawEvent extends Event {
	private static final long serialVersionUID = -108929565086972620L;

	protected LawEvent(String event) {
		super(event);
	}
	
}
