package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

public class PrisonManager {
	private static PrisonManager self = null;
	
	private HashMap<String, Prison> prisons;
	private HashMap<CommandSender, Prison> selectedPrisons;
	
	private PrisonManager() {
		prisons = new HashMap<String, Prison>();
		selectedPrisons = new HashMap<CommandSender , Prison>();
	}
	
	public static PrisonManager get() {
		if (self == null) {
			self = new PrisonManager();
		}
		return self;
	}
	
	public Prison createPrison(String name) throws PrisonAlreadyExistsException, IllegalNameException {
		if (prisons.containsKey(name)) {
			throw new PrisonAlreadyExistsException();
		} else {
			if (name.matches("[a-zA-Z0-9_-]+")) {
				Prison prison = new Prison(name);
				prisons.put(prison.getName(), prison);
				return prison;
			} else {
				throw new IllegalNameException();
			}
		}
	}
	
	public Prison getPrison(String name) {
		return prisons.get(name);
	}
	
	public Prison getOrCreatePrison(String name) {
		Prison prison = prisons.get(name);
		if (prison == null) {
			prison = new Prison(name);
		}
		return prison;
	}
	
	public Prison removePrison(String name) {
		return prisons.remove(name);
	}
	
	public Prison setSelectedPrison(CommandSender sender, String name) {
		Prison prison = prisons.get(name);
		if (prison != null) {
			selectedPrisons.put(sender, prison);
		}
		return prison;
	}
	
	public Prison getSelectedPrison(CommandSender sender) {
		return selectedPrisons.get(sender);
	}
	
	public Prison[] getPrisons() {
		return prisons.values().toArray(new Prison[prisons.size()]);
	}
}
