package net.marcuswhybrow.minecraft.law;

import java.util.Set;

import com.google.common.collect.Sets;

public class Settings {
	private static Settings self = null;
	private Set<Integer> switchableIdentifiers;
	private Set<Integer> usableIdentifiers;
	
	private Settings() {
		// These identifiers are listed on the Minecraft wiki and are for items such as doors and levers.
		switchableIdentifiers = Sets.newHashSet(25, 54, 61, 62, 64, 69, 70, 71, 72, 77, 84, 93, 64);
		usableIdentifiers = Sets.newHashSet(259, 325, 326, 327, 351);
	}
	
	public static Settings get() {
		if (self == null) {
			self = new Settings();
		}
		return self;
	}
	
	public boolean isSwitchableId(int identifier) {
		return switchableIdentifiers.contains(identifier);
	}
	
	public boolean isUsableId(int identifier) {
		return usableIdentifiers.contains(identifier);
	}
}
