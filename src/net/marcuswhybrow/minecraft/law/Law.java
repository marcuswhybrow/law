package net.marcuswhybrow.minecraft.law;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Law {
	public static final String PLUGIN_NAME = "Law";
	public static final String ON_ENABLE_MESSAGE = PLUGIN_NAME + " enabled.";
	public static final String ON_DISABLE_MESSAGE = PLUGIN_NAME + " disabled.";
	public static final String MESSAGE_PREFIX = "[" + PLUGIN_NAME + "] ";
	
	private final Logger log = Logger.getLogger("Minecraft");
	private Plugin plugin = null;
	private static Law self = null;
	
	private Law() {
	}
	
	public static Law get() {
		if (self == null) {
			self = new Law();
		}
		return self;
	}
	
	public void logMessage(String message) {
		log.info(PLUGIN_NAME + ": " + message);
	}
	
	public void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GRAY + MESSAGE_PREFIX + ChatColor.WHITE + message);
	}
	
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public Plugin getPlugin() {
		return this.plugin;
	}
}
