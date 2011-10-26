package net.marcuswhybrow.minecraft.law.commands.elements;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Element {
	/** The text displayed for this element of the command when showing its usage pattern */
	protected String usageText;  
			
	public Element(String usageText) {
		this.usageText = usageText;
	}
	
	public String getUsageText() {
		return usageText;
	}
	
	public static boolean doesTextMatch(String arg) {
		throw new NotImplementedException();
	}
	
	public static Element createElementFromDefinition(String text) {
		if (ArgumentRequired.doesTextMatch(text)) {
			return new ArgumentRequired(text.substring(1, text.length() - 1));
		} else if (ArgumentOptional.doesTextMatch(text)) {
			return new ArgumentOptional(text.substring(1, text.length() - 1));
		} else if (Text.doesTextMatch(text)) {
			return new Text(text);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return getUsageText();
	}
	
	public abstract boolean isOptional();
}
