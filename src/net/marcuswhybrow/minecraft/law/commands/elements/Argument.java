package net.marcuswhybrow.minecraft.law.commands.elements;

public abstract class Argument extends Element {
	public static final String OPENING_CHARACTER = "{";
	public static final String CLOSING_CHARACTER = "}";

	public Argument(String usageText) {
		super(usageText);
	}
	
	@Override
	public String getUsageText() {
		return this.getOpeningCharacter() + usageText + this.getClosingCharacter();
	}
	
	public static boolean doesTextMatch(String text) {
		if (text == null) {
			return false;
		}
		return ArgumentOptional.doesTextMatch(text) || ArgumentRequired.doesTextMatch(text);
	}
	
	public String getOpeningCharacter() {
		return OPENING_CHARACTER;
	}
	
	public String getClosingCharacter() {
		return CLOSING_CHARACTER;
	}
}
