package net.marcuswhybrow.minecraft.law.commands.prison.cell;

import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;

public class CommandLawPrisonCell extends Command {
	public static final String DEFINITION = "law prison cell";
	
	private static final int SUCCESS = 0;
	
	public CommandLawPrisonCell() throws IllegalCommandDefinitionException {
		super(DEFINITION);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		MessageDispatcher.sendMessage(sender, "Prison Cell commands:");
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCellCreate.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCellMove.DEFINITION);
		MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCellDelete.DEFINITION);
		
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
