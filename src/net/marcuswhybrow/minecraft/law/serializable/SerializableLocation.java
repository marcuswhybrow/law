package net.marcuswhybrow.minecraft.law.serializable;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerializableLocation implements Serializable {
	private static final long serialVersionUID = 2507476208929009419L;
	
	private double locationX;
	private double locationY;
	private double locationZ;
	private float locationPitch;
	private float locationYaw;
	private String locationWorldName;
	private transient Location locationCache = null;
	
	public SerializableLocation(Location location) {
		this.setLocation(location);
	}
	
	public Location getLocation() {
		if (this.locationCache == null) {
			this.locationCache = new Location(Bukkit.getWorld(locationWorldName), locationX, locationY, locationZ, locationYaw, locationPitch);
		}
		return this.locationCache;
	}
	
	public void setLocation(Location location) {
		this.locationCache = location;
		this.locationX = location.getX();
		this.locationY = location.getY();
		this.locationZ = location.getZ();
		this.locationPitch = location.getPitch();
		this.locationYaw = location.getYaw();
		this.locationWorldName = location.getWorld().getName();
	}
}
