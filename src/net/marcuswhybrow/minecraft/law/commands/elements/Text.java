package net.marcuswhybrow.minecraft.law.commands.elements;

public class Text extends Element {

	public Text(String usageText) {
		super(usageText);
	}
	
	public static boolean doesTextMatch(String text) {
		if (text == null) {
			return false;
		}
		return text.matches("[a-zA-Z0-9_-]+");
	}

	@Override
	public boolean isOptional() {
		return false;
	}
}
