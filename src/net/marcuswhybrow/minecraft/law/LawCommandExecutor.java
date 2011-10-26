package net.marcuswhybrow.minecraft.law;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.commands.CommandLaw;
import net.marcuswhybrow.minecraft.law.commands.CommandLawFree;
import net.marcuswhybrow.minecraft.law.commands.CommandLawImprison;
import net.marcuswhybrow.minecraft.law.commands.CommandLawReportsPrisons;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrison;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonCreate;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonDelete;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonList;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonSelect;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonSetExit;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCell;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCellCreate;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCellDelete;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCellList;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCellMove;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LawCommandExecutor implements CommandExecutor {
	private Map<String, Command> commands;
	
	public LawCommandExecutor() {
		commands = new HashMap<String, Command>();
		setupCommands();
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		
		dispatchCommand(sender, getInput(command.getName(), args));
		return true;
	}
	
	/**
	 * Instantiates all command instances
	 */
	private void setupCommands() {
		try {
			// The base command
			add(new CommandLaw());
			
			// Imprisonment commands
			add(new CommandLawImprison());
			add(new CommandLawFree());
			
			// The prison commands
			add(new CommandLawPrison());
			add(new CommandLawPrisonList());
			add(new CommandLawPrisonCreate());
			add(new CommandLawPrisonSelect());
			add(new CommandLawPrisonDelete());
			add(new CommandLawPrisonSetExit());
			add(new CommandLawPrisonCell());
			add(new CommandLawPrisonCellList());
			add(new CommandLawPrisonCellCreate());
			add(new CommandLawPrisonCellMove());
			add(new CommandLawPrisonCellDelete());
			
			// The report commands
			add(new CommandLawReportsPrisons());
			
		} catch (IllegalCommandDefinitionException e) {
			MessageDispatcher.consoleSevere("In-game commands have failed to be setup correctly, and will not work. This is a problem with the plugin itself and should be reported to the developer.");
			MessageDispatcher.consoleSevere("Please quote the bukkit version (" + Bukkit.getVersion() + "), the Law plugin version (" + Law.getVersion() + ") and the following \"stack trace\" as this will aid in resolving the problem:");
			MessageDispatcher.consoleSevere(getStackTraceAsString(e));
		}
	}
	
	private void dispatchCommand(CommandSender sender, String[] args) {
		// Convert the input arguments into a string such as "cmdname bittwo bitthree arg1 arg2"
		
		String input;
		Command command = null;
		
		// TODO try and reduce the calls to getArgsAsString by using split instead after a single call to getArgsAsString
		for (int i = args.length; i > 0; i--) {
			input = getArgsAsString(args, i);
			command = commands.get(input);
			if (command != null) {
				break;
			}
		}
		
		if (command != null) {
			command.execute(sender, args);
		} else {
			MessageDispatcher.sendMessage(sender, Colorise.error("The " + Law.PLUGIN_NAME + " plugin could not find that command"));
		}
	}
	
	/**
	 * Combines the first name of a command with the rest of
	 * its input to create a single array
	 * 
	 * @param commandName The first name of the command
	 * @param args The rest of the command
	 * @return The full command as an array
	 */
	private String[] getInput(String commandName, String[] args) {
		String[] newArgs = new String[args.length + 1];
		newArgs[0] = commandName;
		for (int i = 0; i < args.length; i++) {
			newArgs[i+1] = args[i];
		}
		return newArgs;
	}
	
	/**
	 * Adds a command to the list of available commands.
	 * 
	 * @param command The command to add
	 */
	private void add(Command command) {
		commands.put(command.getName(), command);
	}
	
	/**
	 * Gets a string version of an exceptions stack trace
	 * 
	 * @param e The exception to get the stack trace from
	 * @return THe string version of the stack trace
	 */
	private String getStackTraceAsString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		return sw.toString();
	}
	
	private String getArgsAsString(String[] args, int limit) {
		StringBuilder sb = new StringBuilder(args[0]);
		for (int i = 1; i < Math.min(args.length, limit); i++) {
			sb.append(" ").append(args[i]);
		}
		return sb.toString();
	}
}
