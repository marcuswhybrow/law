package net.marcuswhybrow.minecraft.law.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class Utils {
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
}
