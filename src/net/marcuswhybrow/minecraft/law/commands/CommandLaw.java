package net.marcuswhybrow.minecraft.law.commands;

import org.bukkit.command.CommandSender;

import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrison;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

public class CommandLaw extends Command {
	public static final String DEFINITION = "law";
	
	private static final int SUCCESS = 0;
	
	public CommandLaw() throws IllegalCommandDefinitionException {
		super(DEFINITION);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		MessageDispatcher.sendMessage(sender, "Command list:");
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawImprison.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawFree.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrison.DEFINITION + " ...");
		
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
