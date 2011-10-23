package net.marcuswhybrow.minecraft.law;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LawCommandExecutor implements CommandExecutor {
	private static final String PLUGIN_COMMAND_NAME = "law";
	
	private final Law plugin;
	
	public LawCommandExecutor(Law plugin) {
		this.plugin = plugin;
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
				this.law(sender, args);
			} else {
				// Otherwise the first argument is the name of the command (and method)
				// we actually want to execute.
				
				// So we first get this command name
				String lawCommandName = args[0].toLowerCase();
				
				plugin.logMessage(lawCommandName);
				
				// Then attempt to find the respective method on this class
				Method method = null;
				try {
					method = this.getClass().getMethod(lawCommandName, CommandSender.class, String[].class);
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
					}
				}
				
				// If the method resolution and invocation steps did not go to plan,
				// then this message will be sent.
				plugin.sendMessage(sender, "Unknown command. Type \"/law\" for the list.");
			}
			
			return true;
		}
		
		// Unusually we should return true implying that the command was typed
		// correctly even though it has not. This is so we can get more control
		// over the output.
		return true;
	}

	private void law(CommandSender sender, String[] args) {
		sender.sendMessage("/law imprison");
		sender.sendMessage("/law uninprison");
	}
	
	public void imprison(CommandSender sender, String[] args) {
		if (args.length == 2) {
			String prisonerName = args[1];
			plugin.sendMessage(sender, "imprisoned " + ChatColor.AQUA + prisonerName);
		} else {
			plugin.sendMessage(sender, ChatColor.RED + "usage: /law imprison <player>");
		}
	}
}
