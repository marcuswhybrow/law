package net.marcuswhybrow.minecraft.law.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class Utils {
	public static final long TICKS_PER_SECOND = 20L;
	/**
	 * Gets a string version of an exceptions stack trace
	 * 
	 * @param e The exception to get the stack trace from
	 * @return THe string version of the stack trace
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		return sw.toString();
	}
	
	/**
	 * Converts seconds to server ticks.
	 * 
	 * @param seconds The number of seconds
	 * @return The number seconds multiplied by the number of ticks per second
	 */
	public static long toTicks(long seconds) {
		return seconds * TICKS_PER_SECOND;
	}
	
	/**
	 * Converts server ticks to seconds.
	 * 
	 * @param ticks The number of server ticks
	 * @return The number of server ticks divided by the number of ticks per second
	 */
	public static long toSeconds(long ticks) {
		return ticks / TICKS_PER_SECOND;
	}
}
