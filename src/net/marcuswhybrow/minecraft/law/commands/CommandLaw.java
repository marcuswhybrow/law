package net.marcuswhybrow.minecraft.law.commands;

import org.bukkit.command.CommandSender;

import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrison;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

public class CommandLaw extends Command {
	public static final String DEFINITION = "law";
	public static final String PERMISSION_NODE = "law";
	
	private static final int SUCCESS = 0;
	
	public CommandLaw() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setPermissionNode(PERMISSION_NODE);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		MessageDispatcher.sendMessage(sender, "Command list:");
		
		if (Command.checkHasPermission(sender, CommandLawImprison.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawImprison.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawFree.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawFree.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrison.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrison.DEFINITION + " ...");
		}
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		// Do nothing
	}

	@Override
	public void onFailure(int reason) {
		// Cannot fail
	}
}
