package net.marcuswhybrow.minecraft.law;

import net.marcuswhybrow.minecraft.law.listeners.BlockListener;
import net.marcuswhybrow.minecraft.law.listeners.EntityListener;
import net.marcuswhybrow.minecraft.law.listeners.LawListener;
import net.marcuswhybrow.minecraft.law.listeners.PlayerListener;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private Law law = null;
	
	@Override
	public void onEnable() {
		law = Law.get();
		law.setPlugin(this);
		
		// Register in-game command
		getCommand("law").setExecutor(new LawCommandExecutor());
		
		// Setup event registrations
		PluginManager pluginManager = getServer().getPluginManager();
		
		// player events
		pluginManager.registerEvents(new PlayerListener(), this);
		
		// Entity events
		pluginManager.registerEvents(new EntityListener(), this);
		
		// Block events
		pluginManager.registerEvents(new BlockListener(), this);
		
		// Custom events
		pluginManager.registerEvents(new LawListener(), this);
		
		// Setup the Law instance
		law.setup();
		
		// Log that everything is complete
		MessageDispatcher.consoleInfo(Law.ON_ENABLE_MESSAGE);
	}
	
	@Override
	public void onDisable() {
		// Saves all objects with state to file
		Law.save();
		
		// Log that everything has been disabled
		MessageDispatcher.consoleInfo(Law.ON_DISABLE_MESSAGE);
	}
}
