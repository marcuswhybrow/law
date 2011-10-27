package net.marcuswhybrow.minecraft.law.commands.prison.cell;

import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;

public class CommandLawPrisonCell extends Command {
	public static final String DEFINITION = "law prison cell";
	public static final String PERMISSION_NODE = "prison.cell";
	
	private static final int SUCCESS = 0;
	
	public CommandLawPrisonCell() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setPermissionNode(PERMISSION_NODE);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		MessageDispatcher.sendMessage(sender, "Prison Cell commands:");
		
		if (Command.checkHasPermission(sender, CommandLawPrisonCellList.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCellList.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonCellCreate.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCellCreate.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonCellMove.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCellMove.DEFINITION);
		}
		if (Command.checkHasPermission(sender, CommandLawPrisonCellDelete.PERMISSION_NODE)) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "/" + CommandLawPrisonCellDelete.DEFINITION);
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
