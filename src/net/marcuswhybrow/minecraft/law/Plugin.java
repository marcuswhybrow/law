package net.marcuswhybrow.minecraft.law;

import java.util.logging.Logger;

import net.marcuswhybrow.minecraft.law.commands.LawCommand;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	private Law law = null;
	
	@Override
	public void onEnable() {
		law = Law.get();
		law.setPlugin(this);
		
		getCommand("law").setExecutor(new LawCommand());
		
		law.readConfig();
		
		log.info(Law.ON_ENABLE_MESSAGE);
	}
	
	@Override
	public void onDisable() {
		law.fullSave();
		log.info(Law.ON_DISABLE_MESSAGE);
	}
}
