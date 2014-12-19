package net.marcuswhybrow.minecraft.law;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;

import net.marcuswhybrow.minecraft.law.events.LawEvent;
import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;
import net.marcuswhybrow.minecraft.law.utilities.Utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Law {
	public static final String PLUGIN_NAME = "Law";
	public static final String PLUGIN_VERSION = "0.2";
	public static final String ON_ENABLE_MESSAGE = PLUGIN_NAME + " enabled.";
	public static final String ON_DISABLE_MESSAGE = PLUGIN_NAME + " disabled.";
	public static final boolean FORCE_FULL_SAVE = true;
	
	private static char separator = File.separatorChar;
	private static String stateFilePath = "plugins" + separator + "Law";
	private static String stateFileTempPath = "plugins" + separator + "Law";
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
	
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public Plugin getPlugin() {
		return this.plugin;
	}
	
	public void setup() {
		
		// Load objects from file
		Law.load();
		
		// Create worlds which don't exist yet
		MessageDispatcher.consoleInfo("Loading worlds...");
		for (World world : Bukkit.getWorlds()) {
			if (worlds.containsKey(world.getName()) == false) {
				this.worlds.put(world.getName(), new LawWorld(world));
			}
		}
		
		// Schedule all detainee releases
		MessageDispatcher.consoleInfo("Setting up detainee release times...");
		for (LawWorld lawWorld : this.worlds.values()) {
			for (PrisonDetainee detainee : lawWorld.getDetainees()) {
				detainee.ensureReleaseIsScheduled();
			}
		}
	}
	
	public LawWorld getLawWorld(String name) {
		return worlds.get(name);
	}
	
	public LawWorld getLawWorld(World world) {
		return worlds.get(world.getName());
	}
	
	public LawWorld getLawWorldForPlayer(Player player) {
		return getLawWorld(player.getWorld());
	}
	
	public static String getVersion() {
		return PLUGIN_VERSION;
	}
	
	public Collection<LawWorld> getWorlds() {
		return this.worlds.values();
	}
	
	/**
	 * Save the plugin state to disk
	 */
	public static void save() {
		new File(stateFilePath).mkdirs();
		new File(stateFileTempPath).mkdirs();
		
		File file = new File(stateFilePath + separator + "state.dat");
		File tempFile = new File(stateFileTempPath + separator + "state-temp.dat");
		
		try {
			tempFile.delete();
			tempFile.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(tempFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(get().worlds.values().toArray(new LawWorld[get().worlds.size()]));
			oos.close();
			
			if (tempFile.renameTo(file) == false) {
				file.delete();
				tempFile.renameTo(file);
			}
			
		} catch (Exception e) {
			MessageDispatcher.consoleWarning("Could not save state to disk.");
			MessageDispatcher.consoleWarning(Utils.getStackTraceAsString(e));
		}
	}
	
	/**
	 * Load the plugin state from disk
	 */
	public static void load() {
		File file = new File(stateFilePath + separator + "state.dat");
		
		if (file.exists() == false) {
			// try and look for the temp file instead
			File f1 = new File(stateFileTempPath + separator + "state-temp.dat");
			if (f1.exists() == false) {
				return;
			} else {
				f1.renameTo(new File(stateFilePath + separator + "state.dat"));
			}
		}
		
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			if(ois.available() > 1)
			{
				for (LawWorld lawWorld : (LawWorld[]) ois.readObject()) {
					get().worlds.put(lawWorld.getName(), lawWorld);
				}
			}
			
			ois.close();
		} catch (Exception e) {
			MessageDispatcher.consoleWarning("Could not read state from disk.");
			MessageDispatcher.consoleWarning(Utils.getStackTraceAsString(e));
		}
	}
	
	/**
	 * Fires a {@link LawEvent} event.
	 * 
	 * @param event The {@link LawEvent} subclass to fire
	 */
	public static void fireEvent(LawEvent event) {
		get().getPlugin().getServer().getPluginManager().callEvent(event);
	}
}
