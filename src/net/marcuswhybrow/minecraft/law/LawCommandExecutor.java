package net.marcuswhybrow.minecraft.law;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LawCommandExecutor implements CommandExecutor {
	private static final String PLUGIN_COMMAND_NAME = "law";
	private static final String COMMAND_METHOD_PREFIX = "command";
	
	private final Law law;
	private final Plugin plugin;
	private FileConfiguration config;
	
	public LawCommandExecutor() {
		this.law = Law.get();
		this.plugin = law.getPlugin();
		this.config = this.plugin.getConfig();
	}

	@Override
	/**
	 * Determines which method to execute based upon the arguments provided.
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// All commands are accessed via the "/<plugin-name>" command, so here we check
		// to see if the commands name is correct.
		if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME)) {
			if (args.length <= 0) {
				// If there are no arguments to the command, we know which method to call
				this.root(sender, args);
			} else {
				// Otherwise the first argument is the name of the command (and method)
				// we actually want to execute.
				
				// So we first get this command name in lower case
				String lawCommandName = args[0].toLowerCase();
				
				// Then capitalise it
				lawCommandName = Character.toUpperCase(lawCommandName.charAt(0)) + lawCommandName.substring(1);
				
				// And use that to create the final method name we are looking for
				String methodName = COMMAND_METHOD_PREFIX + lawCommandName;
				
				// Then attempt to find the respective method on this class
				Method method = null;
				try {
					method = this.getClass().getMethod(methodName, CommandSender.class, String[].class);
				} catch (SecurityException e) {
					// The command does not exist as a method and therefore cannot be called
				} catch (NoSuchMethodException e) {
					// The command does not exist as a method and therefore cannot be called
				}
				
				if (method != null) {
					// Now that we have the method invoking it should happen without error
					try {
						method.invoke(this, sender, args);
						return true;
					} catch (IllegalArgumentException e) {
						// If there was an error we should inform the user
					} catch (IllegalAccessException e) {
						// If there was an error we should inform the user
					} catch (InvocationTargetException e) {
						// If there was an error we should inform the user
						
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						
						this.law.logMessage(sw.toString());
					}
				}
				
				// If the method resolution and invocation steps did not go to plan,
				// then this message will be sent.
				this.law.sendMessage(sender, "Unknown command. Type \"/law\" for the list.");
			}
			
			return true;
		}
		
		// Unusually we should return true implying that the command was typed
		// correctly even though it has not. This is so we can get more control
		// over the output.
		return true;
	}

	private void root(CommandSender sender, String[] args) {
		Method[] methods = this.getClass().getDeclaredMethods();
		String name = null;
		Boolean commandsExist = false;
		
		this.law.sendMessage(sender, "Command list:");
		
		for (Method method : methods) {
			name = method.getName();
			if (name.startsWith(COMMAND_METHOD_PREFIX))
			{
				name = name.substring(COMMAND_METHOD_PREFIX.length()).toLowerCase();
				sender.sendMessage("/law " + name);
				commandsExist = true;
			}
		}
		
		if (!commandsExist) {
			sender.sendMessage("There are no commands yet.");
		}
	}
	
	public void commandImprison(CommandSender sender, String[] args) {
		if (args.length == 2) {
			String playerName = args[1];
			
			Player player = Bukkit.getPlayer(playerName);
			Location location = player.getLocation();
			
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			
			this.law.logMessage(Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z));
			
			playerName = player.getDisplayName(); 
			
			this.config.set("players." + playerName + ".x", x);
			this.config.set("players." + playerName + ".y", y);
			this.config.set("players." + playerName + ".z", z);
			this.plugin.saveConfig();
			
			this.law.sendMessage(sender, "imprisoned " + ChatColor.AQUA + playerName);
		} else {
			this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " imprison <player>");
		}
	}
	
	public void commandFree(CommandSender sender, String[] args) {
		if (args.length == 2) {
			String playerName = args[1];
			
			Player player = Bukkit.getPlayerExact(playerName);
			playerName = player.getDisplayName();
			
			Double x = this.plugin.getConfig().getDouble("players." + playerName + ".x");
			Double y = this.plugin.getConfig().getDouble("players." + playerName + ".y");
			Double z = this.plugin.getConfig().getDouble("players." + playerName + ".z");
			
			if (x != null && y != null && z != null) {
				Location location = new Location(player.getWorld(), x, y, z);
				player.teleport(location);
			}
			
			
			this.law.sendMessage(sender, "freed " + ChatColor.AQUA + playerName);
		} else {
			this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " free <player>");
		}
	}
	
	public void commandPrison(CommandSender sender, String[] args) {
		if (args.length >= 2) {
			String action = args[1].toLowerCase();
			
			if ("create".equals(action)) {
				if (args.length == 3) {
					String prisonName = args[2].toLowerCase();
					
					try {
						Prison prison = PrisonManager.get().createPrison(prisonName);
						PrisonManager.get().setSelectedPrison(sender, prison.getName());
						this.law.sendMessage(sender, "Prison " + ChatColor.AQUA + prison.getName() + ChatColor.WHITE + " has been created.");
					} catch (PrisonAlreadyExistsException e) {
						this.law.sendMessage(sender, ChatColor.RED + "A prison with that name already exists.");
					} catch (IllegalNameException e) {
						this.law.sendMessage(sender, ChatColor.RED + "A prison name must contain only letters, numbers, dashes or under scores.");
					}
				} else {
					this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " prison create <prison-name>");
				}
			} else if ("remove".equals(action)) {
				if (args.length == 3) {
					String prisonName = args[2].toLowerCase();
					Prison prison = PrisonManager.get().getPrison(prisonName);
					
					if (prison == null) {
						this.law.sendMessage(sender, ChatColor.RED + "A prison with that name does not exist.");
					} else {
						prison.delete();
					}
				} else {
					this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " prison remove <prison-name>");
				}
			} else if ("list".equals(action)) {
				if (args.length == 2) {
					PrisonManager pm = PrisonManager.get();
					Prison[] prisons = pm.getPrisons();
					Prison selectedPrison = pm.getSelectedPrison(sender);
					
					this.law.sendMessage(sender, "Prison list:");
					for (Prison prison : prisons) {
						sender.sendMessage("    " + (prison == selectedPrison ? ChatColor.AQUA : ChatColor.WHITE) + prison.getName());
					}
				} else {
					this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " prison list");
				}
			}
		} else {
			// List the "prison" commands
			Prison prison = PrisonManager.get().getSelectedPrison(sender);
			String prisonName = prison != null ? prison.getName() : "NONE";
			
			this.law.sendMessage(sender, "Current prison: " + prisonName);
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison create <prison-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison remove <prison-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison list");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison switchto <prison-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison addcell <cell-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison removecell <cell-name>");
		}
	}
}
