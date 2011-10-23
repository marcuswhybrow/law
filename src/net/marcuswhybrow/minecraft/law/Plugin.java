package net.marcuswhybrow.minecraft.law;

import java.util.logging.Logger;

import net.marcuswhybrow.minecraft.law.commands.LawCommand;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	
	@Override
	public void onEnable() {
		log.info(Law.ON_ENABLE_MESSAGE);
		
		Law law = Law.get();
		law.setPlugin(this);
		
		getCommand("law").setExecutor(new LawCommand());
		
		law.readConfig();
	}
	
	@Override
	public void onDisable() {
		log.info(Law.ON_DISABLE_MESSAGE);
	}
}
