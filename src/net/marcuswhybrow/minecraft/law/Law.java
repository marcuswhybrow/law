package net.marcuswhybrow.minecraft.law;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Law extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	private static final String PLUGIN_NAME = "Law";
	private static final String ON_ENABLE_MESSAGE = PLUGIN_NAME + " enabled.";
	private static final String ON_DISABLE_MESSAGE = PLUGIN_NAME + " disabled.";
	private final LawBlockListener lawBlockListener = new LawBlockListener(this);
	
	
	@Override
	public void onEnable() {
		log.info(ON_ENABLE_MESSAGE);
		
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvent(Event.Type.BLOCK_BREAK, lawBlockListener, Event.Priority.Highest, this);
	}
	
	@Override
	public void onDisable() {
		log.info(ON_DISABLE_MESSAGE);
	}
	
	public void logMessage(String message) {
		log.info(PLUGIN_NAME + ": " + message);
	}
}
