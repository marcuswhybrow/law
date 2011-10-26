package net.marcuswhybrow.minecraft.law.utilities;

public abstract class Validate {
	public static boolean name(String text) {
		return text.matches("[a-zA-Z0-9_-]+");
	}
}
