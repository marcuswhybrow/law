package net.marcuswhybrow.minecraft.law.commands.elements;

import java.util.regex.Pattern;

public class ArgumentRequired extends Argument {
	public static final String OPENING_CHARACTER = "<";
	public static final String CLOSING_CHARACTER = ">";

	public ArgumentRequired(String usageText) {
		super(usageText);
	}
	
	public static boolean doesTextMatch(String text) {
		if (text == null) {
			return false;
		}
		return text.matches(Pattern.quote(OPENING_CHARACTER) + ".*" + Pattern.quote(CLOSING_CHARACTER));
	}

	@Override
	public boolean isOptional() {
		return false;
	}
	
	@Override
	public String getOpeningCharacter() {
		return OPENING_CHARACTER;
	}
	
	@Override
	public String getClosingCharacter() {
		return CLOSING_CHARACTER;
	}
}
