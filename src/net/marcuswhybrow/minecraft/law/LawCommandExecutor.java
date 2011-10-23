package net.marcuswhybrow.minecraft.law;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
		if (sender instanceof Player == false) {
			law.sendMessage(sender, "The \"/" + PLUGIN_COMMAND_NAME + " prison\" commands can only be used in game (meaning not from the console.)");
			return;
		}
		
		Player player = (Player) sender;
		LawWorld lawWorld = law.getOrCreateLawWorld(player.getWorld());
		
		if (args.length >= 2) {
			String action = args[1].toLowerCase();
			
			if ("create".equals(action)) {
				if (args.length == 3) {
					String prisonName = args[2].toLowerCase();
					
					try {
						Prison prison = lawWorld.createPrison(prisonName);
						lawWorld.setSelectedPrison(player, prison.getName());
						this.law.sendMessage(sender, "Prison " + ChatColor.AQUA + prison.getName() + ChatColor.WHITE + " has been created.");
					} catch (PrisonAlreadyExistsException e) {
						this.law.sendMessage(sender, ChatColor.RED + "A prison with that name already exists.");
					} catch (IllegalNameException e) {
						this.law.sendMessage(sender, ChatColor.RED + "A prison name must contain only letters, numbers, dashes or under scores.");
					}
				} else {
					this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " prison create <prison-name>");
				}
			} else if ("delete".equals(action)) {
				if (args.length == 3) {
					String prisonName = args[2].toLowerCase();
					Prison prison = lawWorld.getPrison(prisonName);
					
					if (prison == null) {
						this.law.sendMessage(sender, ChatColor.RED + "A prison with that name does not exist.");
					} else {
						prison.delete();
						
						Prison selectedPrison = lawWorld.getSelectedPrison(player);
						if (selectedPrison == prison) {
							lawWorld.setSelectedPrison(player, null);
						}
						
						this.law.sendMessage(sender, "The prison " + ChatColor.AQUA + prison.getName() + ChatColor.WHITE + " has been deleted. Use " + ChatColor.YELLOW + "/" + PLUGIN_COMMAND_NAME + " prison select" + ChatColor.WHITE + " to choose another prison to work on.");
					}
				} else {
					this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " prison remove <prison-name>");
				}
			} else if ("list".equals(action)) {
				if (args.length == 2) {
					Prison[] prisons = lawWorld.getPrisons();
					Prison selectedPrison = lawWorld.getSelectedPrison(player);
					
					this.law.sendMessage(sender, "Prison list:");
					if (prisons.length == 0) {
						sender.sendMessage("    There are no prisons yet. Create one using " + ChatColor.YELLOW + "/" + PLUGIN_COMMAND_NAME + " prison create <prison-name>");
					}
					for (Prison prison : prisons) {
						sender.sendMessage("    " + (prison == selectedPrison ? ChatColor.AQUA : ChatColor.WHITE) + prison.getName());
					}
				} else {
					this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " prison list");
				}
			} else if ("select".equals(action)) {
				if (args.length == 3) {
					String prisonName = args[2].toLowerCase();
					Prison prison = lawWorld.getPrison(prisonName);
					
					if (prison == null) {
						this.law.sendMessage(sender, ChatColor.RED + "A prison with that name does not exist.");
					} else {
						lawWorld.setSelectedPrison(player, prison.getName());
						this.law.sendMessage(sender, "The prison " + ChatColor.AQUA + prison.getName() + ChatColor.WHITE + " has been selected. All prison commands now apply to this prison.");
					}
				} else {
					this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " prison select <prison-name>");
				}
			} else {
				this.law.sendMessage(sender, "Unknown command. Type \"/law prison\" for the list.");
			}
		} else {
			// List the "prison" commands
			Prison prison = lawWorld.getSelectedPrison(player);
			String prisonName = prison != null ? prison.getName() : "NONE";
			
			this.law.sendMessage(sender, "Current prison: " + prisonName);
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison create <prison-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison delete <prison-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison list");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison select <prison-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison addcell <cell-name>");
			sender.sendMessage("/" + PLUGIN_COMMAND_NAME + " prison removecell <cell-name>");
		}
	}
}
