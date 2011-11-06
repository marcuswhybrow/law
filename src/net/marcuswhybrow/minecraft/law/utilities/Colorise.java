package net.marcuswhybrow.minecraft.law.utilities;

import org.bukkit.ChatColor;

public class Colorise {
	public static final ChatColor DEFAULT = ChatColor.WHITE;
	public static final ChatColor SUCCESS = ChatColor.GREEN;
	public static final ChatColor ACTION = ChatColor.GREEN;
	public static final ChatColor ERROR = ChatColor.RED;
	public static final ChatColor COMMAND = ChatColor.YELLOW;
	public static final ChatColor ENTITY = ChatColor.AQUA;
	public static final ChatColor PREFIX = ChatColor.GRAY;
	public static final ChatColor INFO = ChatColor.DARK_GRAY;
	public static final ChatColor HIGHLIGHT = ChatColor.GOLD;
	
	public static String color(ChatColor color, String text) {
		return color + text + DEFAULT;
	}
	
	public static String error(String text) {
		return color(ERROR, text);
	}
	
	public static String command(String text) {
		return color(COMMAND, text);
	}
	
	public static String entity(String text) {
		return color(ENTITY, text);
	}
	
	public static String info(String text) {
		return color(INFO, text);
	}
	
	public static String success(String text) {
		return color(SUCCESS, text);
	}
	
	public static String highlight(String text) {
		return color(HIGHLIGHT, text);
	}
	
	public static String action(String text) {
		return color(ACTION, text);
	}
}
