package net.marcuswhybrow.minecraft.law;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.java.JavaPlugin;

public class Law extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	private static final String PLUGIN_NAME = "Law";
	private static final String ON_ENABLE_MESSAGE = PLUGIN_NAME + " enabled.";
	private static final String ON_DISABLE_MESSAGE = PLUGIN_NAME + " disabled.";
	private static final String MESSAGE_PREFIX = "[" + PLUGIN_NAME + "] ";
//	private final LawBlockListener lawBlockListener = new LawBlockListener(this);
	
	private FileConfiguration config;
	
	@Override
	public void onEnable() {
		log.info(ON_ENABLE_MESSAGE);
		
//		PluginManager pluginManager = this.getServer().getPluginManager();
//		pluginManager.registerEvent(Event.Type.BLOCK_BREAK, lawBlockListener, Event.Priority.Highest, this);
		
		getCommand("law").setExecutor(new LawCommandExecutor(this));
		
		config = this.getConfig();
		config.set("marcus.whybrow.is", "Great");
		
		this.saveConfig();
	}
	
	@Override
	public void onDisable() {
		log.info(ON_DISABLE_MESSAGE);
	}
	
	public void logMessage(String message) {
		log.info(PLUGIN_NAME + ": " + message);
	}
	
	public void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GRAY + MESSAGE_PREFIX + ChatColor.WHITE + message);
	}
}
