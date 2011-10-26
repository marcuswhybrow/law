package net.marcuswhybrow.minecraft.law.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LawCommand implements CommandExecutor {
	
	public LawCommand() {
	}

	@Override
	/**
	 * Determines which method to execute based upon the arguments provided.
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		// All commands are accessed via the "/<plugin-name>" command, so here we check
//		// to see if the commands name is correct.
//		if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME)) {
//			if (args.length <= 0) {
//				// If there are no arguments to the command, we know which method to call
//				this.root(sender, args);
//			} else {
//				// Otherwise the first argument is the name of the command (and method)
//				// we actually want to execute.
//				
//				// So we first get this command name in lower case
//				String lawCommandName = args[0].toLowerCase();
//				
//				// Then capitalise it
//				lawCommandName = Character.toUpperCase(lawCommandName.charAt(0)) + lawCommandName.substring(1);
//				
//				// And use that to create the final method name we are looking for
//				String methodName = COMMAND_METHOD_PREFIX + lawCommandName;
//				
//				// Then attempt to find the respective method on this class
//				Method method = null;
//				try {
//					method = this.getClass().getMethod(methodName, CommandSender.class, String[].class);
//				} catch (SecurityException e) {
//					// The command does not exist as a method and therefore cannot be called
//				} catch (NoSuchMethodException e) {
//					// The command does not exist as a method and therefore cannot be called
//				}
//				
//				if (method != null) {
//					// Now that we have the method invoking it should happen without error
//					try {
//						method.invoke(this, sender, args);
//						return true;
//					} catch (IllegalArgumentException e) {
//						// If there was an error we should inform the user
//					} catch (IllegalAccessException e) {
//						// If there was an error we should inform the user
//					} catch (InvocationTargetException e) {
//						// If there was an error we should inform the user
//						
//						StringWriter sw = new StringWriter();
//						PrintWriter pw = new PrintWriter(sw);
//						e.printStackTrace(pw);
//						
//						MessageDispatcher.consoleInfo(sw.toString());
//					}
//				}
//				
//				// If the method resolution and invocation steps did not go to plan,
//				// then this message will be sent.
//				MessageDispatcher.sendMessage(sender, "Unknown command. Type " + MessageDispatcher.command("/law") + " for the list.");
//			}
//			
			return true;
//		}
//		
//		// Unusually we should return true implying that the command was typed
//		// correctly even though it has not. This is so we can get more control
//		// over the output.
//		return true;
	}
//
//	private void root(CommandSender sender, String[] args) {
//		Method[] methods = this.getClass().getDeclaredMethods();
//		String name = null;
//		Boolean commandsExist = false;
//		
//		this.law.sendMessage(sender, "Command list:");
//		
//		for (Method method : methods) {
//			name = method.getName();
//			if (name.startsWith(COMMAND_METHOD_PREFIX))
//			{
//				name = name.substring(COMMAND_METHOD_PREFIX.length()).toLowerCase();
//				sender.sendMessage("/law " + name);
//				commandsExist = true;
//			}
//		}
//		
//		if (!commandsExist) {
//			sender.sendMessage("There are no commands yet.");
//		}
//	}
//	
//	public void commandImprison(CommandSender sender, String[] args) {
//		if (sender instanceof Player == false) {
//			law.sendMessage(sender, "The \"/" + PLUGIN_COMMAND_NAME + " imprison\" command can only be used in game (meaning not from the console.)");
//			return;
//		}
//		
//		if (args.length == 2 || args.length == 3) {
//			String playerName = args[1];
//			
//			Player player = (Player) sender;
//			Player targetPlayer = Bukkit.getPlayerExact(playerName);
//			
//			LawWorld lawWorld = law.get().getOrCreateLawWorld(player.getWorld());
//			
//			if (lawWorld.isPlayerImprisoned(playerName)) {
//				// Player is already imprisoned in this world so cannot imprison them again
//				this.law.sendMessage(sender, ChatColor.RED + "Error:" + ChatColor.WHITE + " This player is already imprisoned in this world.");
//				return;
//			}
//			
//			// Try and get a prison name
//			String prisonName = null;
//			Prison prison = null;
//			if (args.length == 3) {
//				// Use the one specified in the command if provided
//				prisonName = args[2].toLowerCase();
//				
//				// Check a prison with that name exists
//				prison = lawWorld.getPrison(prisonName);
//				if (prison == null) {
//					// This world does not have a prison by the specified name
//					this.law.sendMessage(sender, ChatColor.RED + "Error:" + ChatColor.WHITE + " No prison with that name.");
//					return;
//				}
//				
//				// Updated the name used to be the exact prison name
//				prisonName = prison.getName();
//			} else {
//				// Otherwise look at the currently selected prison for this user
//				prison = lawWorld.getSelectedPrison(player.getDisplayName());
//				if (prison != null) {
//					// Use its name if one is set
//					prisonName = prison.getName();
//				} else {
//					// Otherwise we don't have a prison to use and so we cannot imprison the player
//					this.law.sendMessage(sender, ChatColor.RED + "Error:" + ChatColor.WHITE + " No default prison selected, or specified in command.");
//					return;
//				}
//			}
//			
//			// Ensure the prison is operational
//			if (!prison.isOperational()) {
//				// Deliver appropriate messages to help the user make the prison operational.
//				if (!prison.hasDefaultCell()) {
//					this.law.sendMessage(sender, ChatColor.RED + "Error:" + ChatColor.WHITE + " Prison " + ChatColor.AQUA + prisonName + ChatColor.WHITE + " has no default cell. Use " + ChatColor.YELLOW + "/law prison setdefaultcell " + ChatColor.WHITE + "to rectify this.");
//				}
//				if (!prison.hasExitPoint()) {
//					this.law.sendMessage(sender, ChatColor.RED + "Error:" + ChatColor.WHITE + " Prison " + ChatColor.AQUA + prisonName + ChatColor.WHITE + " has no exit point. Use " + ChatColor.YELLOW + "/law prison setexitpoint " + ChatColor.WHITE + "to rectify this.");
//				}
//				return;
//			}
//			
//			// Since all test have passed, imprison the player
//			lawWorld.imprisonPlayer(playerName, prisonName, PrisonCell.DEFAULT_NAME);
//			lawWorld.save();
//			law.logMessage(player.getDisplayName() + " imprisoned \"" + playerName + "\" in \"" + prisonName + "\" prison");
//			
//			if (targetPlayer != null) {
//				this.law.sendMessage(sender, "Imprisoned " + ChatColor.AQUA + playerName + ChatColor.WHITE + " in " + ChatColor.AQUA + prisonName + ChatColor.WHITE + " prison.");
//			} else {
//				this.law.sendMessage(sender, "Imprisoned " + ChatColor.AQUA + playerName + ChatColor.WHITE + " in " + ChatColor.AQUA + prisonName + ChatColor.WHITE + " prison. This player is offline but will be imprisoned when they return.");
//			}
//		} else {
//			this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " imprison <player-name> [prison-name]");
//		}
//	}
//	
//	public void commandFree(CommandSender sender, String[] args) {
//		if (sender instanceof Player == false) {
//			law.sendMessage(sender, "The \"/" + PLUGIN_COMMAND_NAME + " imprison\" command can only be used in game (meaning not from the console.)");
//			return;
//		}
//		
//		if (args.length == 2) {
//			String playerName = args[1];
//			
//			Player player = (Player) sender;
//			Player targetPlayer = Bukkit.getPlayerExact(playerName);
//			
//			LawWorld lawWorld = law.get().getOrCreateLawWorld(player.getWorld());
//			PrisonCell cell = lawWorld.getPrisonCellForPlayer(playerName);
//			String prisonName = "<unknown>";
//			
//			if (cell != null) {
//				prisonName = cell.getPrison().getName();
//			}
//			
//			law.logMessage("here1");
//			
//			boolean isFreed = lawWorld.freePlayer(playerName);
//			
//			law.logMessage("here2");
//			
//			lawWorld.save();
//			
//			law.logMessage("here3");
//			
//			if (isFreed) {
//				law.logMessage(player.getDisplayName() + " freed \"" + playerName + "\" from \"" + prisonName + "\" prison");
//				
//				if (targetPlayer != null) {
//					this.law.sendMessage(sender, "Freed " + ChatColor.AQUA + playerName + ChatColor.WHITE + " from " + ChatColor.AQUA + prisonName + ChatColor.WHITE + " prison.");
//				} else {
//					this.law.sendMessage(sender, "Freed " + ChatColor.AQUA + playerName + ChatColor.WHITE + " from " + ChatColor.AQUA + prisonName + ChatColor.WHITE + " prison. This player is offline but will be free when they return.");
//				}
//			} else {
//				this.law.sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.WHITE + "A player with that name is not imprisoned.");
//			}
//		} else {
//			this.law.sendMessage(sender, ChatColor.RED + "usage: /" + PLUGIN_COMMAND_NAME + " imprison <player>");
//		}
//	}
//	
}
