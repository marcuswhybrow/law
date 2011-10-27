package net.marcuswhybrow.minecraft.law.commands.prison;

import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCell;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;

public class CommandLawPrison extends Command {
	public static final String DEFINITION = "law prison";
	public static final String PERMISSION_NODE = "prison";
	
	private static final int SUCCESS = 0;
	
	public CommandLawPrison() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setPermissionNode(PERMISSION_NODE);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		MessageDispatcher.sendMessage(sender, "Prison commands:");
		
		if (Command.checkHasPermission(sender, CommandLawPrisonList.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonList.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonCreate.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCreate.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonSelect.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonSelect.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonDelete.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonDelete.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonSetExit.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonSetExit.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonCell.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCell.DEFINITION + " ...");
		}
		
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
