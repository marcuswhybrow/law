package net.marcuswhybrow.minecraft.law.utilities;

import java.util.logging.Logger;

import net.marcuswhybrow.minecraft.law.Law;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public abstract class MessageDispatcher {
	public static final String MESSAGE_PREFIX = "[" + Law.PLUGIN_NAME + "] ";
	public static final String CONSOLE_PREFIX = "[" + Law.PLUGIN_NAME + "] ";
	
	public static final String LOGGER_NAME = "Minecraft";
	
	private static final String PREFIX = Colorise.color(Colorise.PREFIX, MESSAGE_PREFIX);
	
	public static void consoleInfo(String message) {
		Logger.getLogger(LOGGER_NAME).info(CONSOLE_PREFIX + message);
	}
	
	public static void consoleWarning(String message) {
		Logger.getLogger(LOGGER_NAME).warning(CONSOLE_PREFIX + message);
	}
	
	public static void consoleSevere(String message) {
		Logger.getLogger(LOGGER_NAME).severe(CONSOLE_PREFIX + message);
	}
	
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(PREFIX + message);
	}
	
	public static void sendMessageWithoutPrefix(CommandSender sender, String message) {
		sender.sendMessage(message);
	}
	
	public static void broadcast(String message, String permission) {
		Bukkit.broadcast(PREFIX + message, permission);
	}
	
	public static void broadcastMessage(String message) {
		Bukkit.broadcastMessage(PREFIX + message);
	}
}
