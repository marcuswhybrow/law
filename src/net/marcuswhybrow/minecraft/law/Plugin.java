package net.marcuswhybrow.minecraft.law;

import net.marcuswhybrow.minecraft.law.listeners.BlockListener;
import net.marcuswhybrow.minecraft.law.listeners.PlayerListener;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private Law law = null;
	
	@Override
	public void onEnable() {
		law = Law.get();
		law.setPlugin(this);
		
		getCommand("law").setExecutor(new LawCommandExecutor());
		
		PluginManager pluginManager = getServer().getPluginManager();
		PlayerListener playerListener = new PlayerListener();
		BlockListener blockListener = new BlockListener();
		
		pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pluginManager.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Highest, this);
		pluginManager.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Highest, this);
		pluginManager.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Highest, this);
		
		law.setup();
		MessageDispatcher.consoleInfo(Law.ON_ENABLE_MESSAGE);
	}
	
	@Override
	public void onDisable() {
		law.fullSave();
		MessageDispatcher.consoleInfo(Law.ON_DISABLE_MESSAGE);
	}
}
