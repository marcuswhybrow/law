package net.marcuswhybrow.minecraft.law.commands.prison;

import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCell;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;

public class CommandLawPrison extends Command {
	public static final String DEFINITION = "law prison";
	
	private static final int SUCCESS = 0;
	
	public CommandLawPrison() throws IllegalCommandDefinitionException {
		super(DEFINITION);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		MessageDispatcher.sendMessage(sender, "Prison commands:");
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonList.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCreate.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonSelect.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonDelete.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonSetExit.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCell.DEFINITION + "...");
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		// Nothing
	}

	@Override
	public void onFailure(int reason) {
		// It cannot fail
	}

}
