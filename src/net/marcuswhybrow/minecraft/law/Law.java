package net.marcuswhybrow.minecraft.law;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.marcuswhybrow.minecraft.law.interfaces.PrisonerContainer;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Law {
	public static final String PLUGIN_NAME = "Law";
	public static final String PLUGIN_VERSION = "0.1";
	public static final String ON_ENABLE_MESSAGE = PLUGIN_NAME + " enabled.";
	public static final String ON_DISABLE_MESSAGE = PLUGIN_NAME + " disabled.";
	public static final boolean FORCE_FULL_SAVE = true;
	
	private Plugin plugin = null;
	private static Law self = null;
	private HashMap<String, LawWorld> worlds;
	private enum State {SETUP, ACTIVE};
	private State state;
	
	private Law() {
		worlds = new HashMap<String, LawWorld>();
		state = State.SETUP;
	}
	
	public static Law get() {
		if (self == null) {
			self = new Law();
		}
		return self;
	}
	
	public boolean isActive() {
		return state == State.ACTIVE;
	}
	
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public Plugin getPlugin() {
		return this.plugin;
	}
	
	public void setup() {
		
		for (World world : Bukkit.getWorlds()) {
			LawWorld lawWorld = new LawWorld(world);
			this.worlds.put(lawWorld.getName(), lawWorld);
			lawWorld.setup();
		}
		
		this.setActive();
	}
	
	public void setActive() {
		this.state = State.ACTIVE;
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
	
	public void fullSave() {
		Iterator<LawWorld> it = worlds.values().iterator();
		while (it.hasNext()) {
			it.next().save(Law.FORCE_FULL_SAVE);
		}
	}
	
	public static String getVersion() {
		return PLUGIN_VERSION;
	}
	
	public Collection<LawWorld> getWorlds() {
		return this.worlds.values();
	}
	
	public boolean imprisonPlayer(String playerName, PrisonerContainer prisonerContainer) {
		if (prisonerContainer == null) {
			throw new IllegalArgumentException("Prisoner container cannot be null");
		}
		
		if (prisonerContainer.imprisonPlayer(playerName) == false) {
			return false;
		}
		
		// Give the player the imprisoned permission
		
		PrisonCell cell = prisonerContainer.getPrisonerCell(playerName);
		Player player = Bukkit.getPlayerExact(playerName);
		if (player != null) {
			// Teleport the player right now to the location
			player.teleport(cell.getLocation());
		} else {
			LawWorld lawWorld = cell.getPrison().getLawWorld();
			lawWorld.addLatentTeleport(playerName, cell.getLocation());
			lawWorld.save();
		}
		
		return true;
	}
	
	public void freePlayer(String playerName, PrisonerContainer prisonerContainer) {
		PrisonCell cell = prisonerContainer.getPrisonerCell(playerName);
		if (cell == null) {
			return;
		}
		
		prisonerContainer.freePlayer(playerName);
		
		Player player = Bukkit.getPlayerExact(playerName);
		Location location = cell.getPrison().getExitPoint();
		
		if (location == null) {
			location = cell.getPrison().getLawWorld().getBukkitWorld().getSpawnLocation();
		}
		
		if (player != null) {
			player.teleport(location);
		} else {
			LawWorld lawWorld = cell.getPrison().getLawWorld();
			
			// Since there are only two states (free and imprisoned) and previous tests
			// returned if the player is already free, if a location exists it is
			// because the player was to be imprisoned.
			// If this is the case we can just remove the teleport directive so that
			// the player in question is none the wiser when they next login.
			Location storedLocation = lawWorld.removeLatentTeleport(playerName);
			if (storedLocation == null) {
				// Otherwise write the teleport instruction as the player is imprisoned
				lawWorld.addLatentTeleport(playerName, location);
			}
			lawWorld.save();
		}
	}
	
	public void save() {
		save(false);
	}
	
	public void save(boolean forceFullSave) {
		for (LawWorld lawWorld : worlds.values()) {
			lawWorld.save(forceFullSave);
		}
	}
}
