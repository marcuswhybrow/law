package net.marcuswhybrow.minecraft.law.utilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Contains methods for converting a players full inventory (that is
 * the items and armor) into a flat array, and back again. And also
 * methods for saving to and reading from file a players inventory.
 * 
 * THANKS: The code for these functions was mostly taken from the
 * MobArena plugin which I used to figure out how to accomplish the
 * tasks involved.
 * 
 * @author Marcus Whybrow
 *
 */
public final class Inventory {
	private static char separator = File.separatorChar;
	private static String inventoriesPath = "plugins" + separator + "Law" + separator + "inventories";
	
	private Inventory() {
		
	}
	
	/**
	 * Sets a players inventory (items and armor) from an array of
	 * ItemStack objects with the items first and armor at the end of
	 * the array.
	 * 
	 * @param player The player whose inventory should be set
	 * @param stacks The items to put in the inventory
	 */
	public static void setFromArray(Player player, ItemStack[] stacks) {
		ItemStack[] armor = new ItemStack[4];
		ItemStack[] items = new ItemStack[stacks.length - armor.length];
		
		for (int i = 0; i < items.length; i++) {
			items[i] = stacks[i];
		}
		for (int i = 0; i < armor.length; i++) {
			armor[i] = stacks[items.length + i];
		}
	
		PlayerInventory inventory = player.getInventory(); 
		inventory.setContents(items);
		inventory.setArmorContents(armor);
	}
	
	/**
	 * Gets the players inventory (items and armor) as an array of
	 * ItemStack objects with the items first and the armor at the
	 * end of the array.
	 * 
	 * @param player The player whose inventory to get
	 * @return The inventory as an array of ItemStack objects (armor at the end).
	 */
	public static ItemStack[] getAsArray(Player player) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] items = inventory.getContents();
		ItemStack[] armor = inventory.getArmorContents();
		
		ItemStack[] all = new ItemStack[items.length + armor.length];
		for (int i = 0; i < items.length; i++) {
			all[i] = items[i];
		}
		for (int i = 0; i < armor.length; i++) {
			all[items.length + i] = armor[i];
		}
		return all;
	}
	
	/**
	 * Clears a player's inventory, saving it to file.
	 * 
	 * @param player The player whose inventory should be stored
	 * @return True if the inventory could be stored
	 */
	public static boolean saveToFile(Player player) {
		
		new File(inventoriesPath + separator + player.getWorld().getName()).mkdir();
		File file = new File(Inventory.getFileName(player));
		
		if (file.exists()) {
			return false;
		}
		
		try {
			file.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(SavableStack.fromItemStacks(getAsArray(player)));
			oos.close();
			
		} catch (Exception e) {
			MessageDispatcher.consoleInfo("Could not create inventory file " + file.getName());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Restores a player's inventory from file and removes the saved file
	 * 
	 * @param player
	 * @return
	 */
	public static boolean restoreFromFile(Player player) {
		File file = new File(Inventory.getFileName(player));
		
		if (file.exists() == false) {
			return false;
		}
		
		SavableStack[] stacks = null;
		
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			stacks = (SavableStack[]) ois.readObject();
			ois.close();
		} catch (Exception e) {
			MessageDispatcher.consoleInfo("Could not read inventory file " + file.getName());
		}
		
		if (stacks == null) {
			MessageDispatcher.consoleWarning("Could not restore inventory for " + player.getName());
		}
		
		Inventory.setFromArray(player, SavableStack.toItemStacks(stacks));
		
		return true;
	}
	
	/**
	 * Removes all items and armor from a players inventory.
	 * 
	 * @param player The player whose inventory should be cleared
	 */
	public static void clear(Player player) {
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setHelmet(null);
		inventory.setChestplate(null);
		inventory.setLeggings(null);
		inventory.setBoots(null);
	}
	
	/**
	 * Deletes the inventory file for the specified player
	 * 
	 * @param player The player whose inventory file should be deleted
	 */
	public static boolean deleteFile(Player player) {
		File file = new File(Inventory.getFileName(player));
		return file.delete();
	}
	
	/**
	 * Gets the file name for the specified players inventory file,
	 * relative to the server root (ex: "plugins/Law/inventories/world/craftysaurus.inv")
	 * 
	 * @param player The player to get the inventory file name for
	 * @return The file name
	 */
	private static String getFileName(Player player) {
		return inventoriesPath + separator + player.getWorld().getName() + separator + player.getName() + ".inv";
	}
	
	/**
	 * A serializable representation of an ItemStack which can be written to file
	 * 
	 * @author Marcus Whybrow
	 */
	private static class SavableStack implements Serializable {
		private static final long serialVersionUID = 1588359775952545712L;
		private int id;
		private int amount;
		private Byte data;
		private short durability;
		
		public SavableStack(ItemStack stack) {
			if (stack == null) {
				id = -1;
				amount = -1;
				data = (byte) 0;
				durability = (short) 0;
			} else {
				id = stack.getTypeId();
				amount = stack.getAmount();
				MaterialData materialData = stack.getData();
				data = materialData == null ? (byte) 0 : materialData.getData();
			}
		}
		
		public static SavableStack[] fromItemStacks(ItemStack[] stacks) {
			SavableStack[] savableStacks = new SavableStack[stacks.length];
			for (int i = 0; i < stacks.length; i++) {
				savableStacks[i] = fromItemStack(stacks[i]);
			}
			return savableStacks;
		}
		
		public static SavableStack fromItemStack(ItemStack stack) {
			return new SavableStack(stack);
		}
		
		public static ItemStack[] toItemStacks(SavableStack[] savableStacks) {
			ItemStack[] stacks = new ItemStack[savableStacks.length];
			for (int i = 0; i < savableStacks.length; i++) {
				stacks[i] = savableStacks[i].toItemStack();
			}
			return stacks;
		}
		
		/**
		 * Get the ItemStack which this SavableStack represents
		 * 
		 * @return The ItemStack representation of this SavableStack
		 */
		public ItemStack toItemStack() {
			if (id == -1) {
				return null;
			}
			
			ItemStack stack = new ItemStack(id, amount, durability);
			if (data != null) {
				stack.setData(new MaterialData(id, data));
			}
			return stack;
		}
	}
}
