package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import quicktime.qd.SetGWorld;

public class Law {
	public static final String PLUGIN_NAME = "Law";
	public static final String ON_ENABLE_MESSAGE = PLUGIN_NAME + " enabled.";
	public static final String ON_DISABLE_MESSAGE = PLUGIN_NAME + " disabled.";
	public static final String MESSAGE_PREFIX = "[" + PLUGIN_NAME + "] ";
	
	public static final boolean FORCE_FULL_SAVE = true;
	
	
	private final Logger log = Logger.getLogger("Minecraft");
	private Plugin plugin = null;
	private static Law self = null;
	
	private HashMap<String, LawWorld> worlds;
	
	private Law() {
		worlds = new HashMap<String, LawWorld>();
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
	
	public void readConfig() {
		FileConfiguration config = this.plugin.getConfig();
		
		// Create LawWorld instances
		ConfigurationSection worlds = config.getConfigurationSection("worlds");
		
		if (worlds != null) {
			World world;
			LawWorld lawWorld;
			ConfigurationSection prisons;
			ConfigurationSection activePrisons;
			String prefix = null;
			
			for (String worldName : worlds.getKeys(false)) {
				world = plugin.getServer().getWorld(worldName);
				lawWorld = new LawWorld(world, false);
				this.putLawWorld(lawWorld);
				
				prefix = "worlds." + lawWorld.getName() + ".prisons";
				
				// Create Prison instances
				prisons = config.getConfigurationSection(prefix);
				
				Prison prison;
				ConfigurationSection cells;
				double x, y, z, pitch, yaw;
				Location cellLocation;
				
				Double defaultCellX, defaultCellY, defaultCellZ, defaultCellPitch, defaultCellYaw;
				Double exitPointX, exitPointY, exitPointZ, exitPointPitch, exitPointYaw;
				
				if (prisons != null) {
					for (String prisonName : prisons.getKeys(false)) {
						prison = new Prison(lawWorld, prisonName, false);
						lawWorld.addPrison(prison);
						
						prefix += "." + prison.getName();
						
						// Retrieve the exit point for this prison
						exitPointX = config.getDouble(prefix + ".exit_point.location.x");
						exitPointY = config.getDouble(prefix + ".exit_point.location.y");
						exitPointZ = config.getDouble(prefix + ".exit_point.location.z");
						exitPointPitch = config.getDouble(prefix + ".exit_point.location.pitch");
						exitPointYaw = config.getDouble(prefix + ".exit_point.location.yaw");
						
						if (exitPointX != 0 || exitPointY != 0 || exitPointZ != 0) {
							prison.setExitPoint(new Location(world, exitPointX, exitPointY, exitPointZ, new Float(exitPointYaw), new Float(exitPointPitch)));
						}
						
						// Create the Prison cells
						cells = config.getConfigurationSection(prefix + ".cells");
						if (cells != null) {
							for (String cellName : cells.getKeys(false)) {
								prefix += ".cells." + cellName;
								x = config.getDouble(prefix + ".location.x");
								y = config.getDouble(prefix + ".location.y");
								z = config.getDouble(prefix + ".location.z");
								pitch = config.getDouble(prefix + ".location.pitch");
								yaw = config.getDouble(prefix + ".location.yaw");
								cellLocation = new Location(world, x, y, z, new Float(pitch), new Float(yaw));
								
								if (PrisonCell.DEFAULT_NAME.equals(cellName)) {
									prison.createCellAsDefault(cellLocation);
								} else {
									prison.createCell(cellName, cellLocation);
								}
								
								prefix += ".imprisoned_players";
								
								@SuppressWarnings("unchecked")
								List<String> imprisonedPlayersList = config.getList(prefix);
								
								if (imprisonedPlayersList != null) {
									Set<String> imprisonedPlayersSet = new HashSet<String>(imprisonedPlayersList);
									Iterator<String> it = imprisonedPlayersSet.iterator();
									String playerName;
									
									while (it.hasNext()) {
										playerName = it.next();
										lawWorld.imprisonPlayer(playerName, prison.getName(), cellName);
									}
								}
							}
						}
					}
				}
				
				// Set active prisons
				activePrisons = config.getConfigurationSection("worlds." + lawWorld.getName() + ".active_prisons");
				
				String prisonName;
				if (activePrisons != null) {
					for (String playerDisplayName : activePrisons.getKeys(false)) {
						prisonName = config.getString("worlds." + lawWorld.getName() + ".active_prisons." + playerDisplayName);
						lawWorld.setSelectedPrison(playerDisplayName, prisonName);
					}
				}
				
				// Activates all created models including and contained by the LawWorld
				lawWorld.setActive(true);
			}
		}
	}
	
	public LawWorld getLawWorld(String name) {
		return worlds.get(name);
	}
	
	public LawWorld getOrCreateLawWorld(World world) {
		LawWorld lawWorld = worlds.get(world.getName());
		if (lawWorld == null) {
			lawWorld = new LawWorld(world);
			this.putLawWorld(lawWorld);
		}
		return lawWorld;
	}
	
	public void putLawWorld(LawWorld lawWorld) {
		worlds.put(lawWorld.getName(), lawWorld);
	}
	
	public void putWorld(World world) {
		this.putLawWorld(new LawWorld(world));
	}
	
	public void fullSave() {
		Iterator<LawWorld> it = worlds.values().iterator();
		while (it.hasNext()) {
			it.next().save(Law.FORCE_FULL_SAVE);
		}
	}
}
