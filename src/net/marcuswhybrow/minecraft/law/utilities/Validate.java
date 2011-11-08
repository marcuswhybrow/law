package net.marcuswhybrow.minecraft.law.utilities;

public abstract class Validate {
	public static boolean name(String text) {
		return text.matches("[a-zA-Z0-9_-]+");
	}
	
	/**
	 * Converts a formatted duration string into a number of seconds.
	 * Here are some examples of the acceptable formats:
	 * 
	 * 12
	 * 2m
	 * 5h
	 * 1d
	 * 52w
	 * 
	 * In other words a number followed by the letter m,h,d or w.
	 * 
	 * @param formattedDuration The duration formatted as a string
	 * @return The number of seconds represented by the formatted duration
	 * @throws NumberFormatException If the number part of the formatted string could not be converted to a number
	 */
	public static long getTime(String formattedDuration) throws NumberFormatException {
		char lastChar = formattedDuration.charAt(formattedDuration.length() -1);
		
		if (Character.isDigit(lastChar)) {
			return Long.parseLong(formattedDuration);
		}
		
		String timeString = formattedDuration.substring(0, formattedDuration.length() - 1);
		
		long number = Long.parseLong(timeString);
		
		switch (lastChar) {
		case 'w':
			return number * 60 * 60 * 24 * 7;
		case 'd':
			return number * 60 * 60 * 24;
		case 'h':
			return number * 60 * 60;
		case 'm':
			return number * 60;
		default:
			throw new IllegalArgumentException("Time format must consist of a number, optionally followed by one of these letters: m,h,d,w.");
		}
	}
}
