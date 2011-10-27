package net.marcuswhybrow.minecraft.law.commands.prison.cell;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.Commands;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawPrisonCellCreate extends Command {
	public static final String DEFINITION = "law prison cell create <cell-name>";
	public static final String PERMISSION_NODE = "prison.cell.create";
	
	private static final int SUCCESS = 0;
	private static final int CREATE_PRISON_FIRST = 1;
	private static final int SELECT_PRISON_FIRST = 2;
	private static final int PRISON_HAS_CELL_WITH_THAT_NAME = 3;
	
	private Player player;
	private LawWorld lawWorld;
	private Prison selectedPrison;
	private PrisonCell createdCell, existingCell;
	
	public CommandLawPrisonCellCreate() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		lawWorld = null;
		selectedPrison = null;
		createdCell = null;
		existingCell = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		String cellName = args[4];
		player = (Player) sender;
		lawWorld = Law.get().getLawWorldForPlayer(player);
		selectedPrison = lawWorld.getSelectedPrison(player.getName());
		
		if (selectedPrison == null) {
			if (lawWorld.getPrisons().size() == 0) {
				return CREATE_PRISON_FIRST;
			} else {
				return SELECT_PRISON_FIRST;
			}
		}
		
		existingCell = selectedPrison.getCell(cellName);
		
		if (existingCell != null) {
			return PRISON_HAS_CELL_WITH_THAT_NAME;
		}
		
		createdCell = new PrisonCell(selectedPrison, cellName, player.getLocation());
		selectedPrison.addCell(createdCell);
		selectedPrison.save();
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		MessageDispatcher.consoleInfo(player.getName() + " created the cell \"" + createdCell.getName() + "\" for \"" + selectedPrison.getName() + "\" prison.");
		MessageDispatcher.sendMessage(player, "The cell " + Colorise.entity(createdCell.getName()) + " has been created for " + Colorise.entity(selectedPrison.getName()) + " prison.");
	}

	@Override
	public void onFailure(int reason) {
		switch (reason) {
		case CREATE_PRISON_FIRST:
			MessageDispatcher.sendMessage(player, Commands.CREATE_PRISON_MESSAGE);
			break;
		case SELECT_PRISON_FIRST:
			MessageDispatcher.sendMessage(player, Commands.SELECT_PRISON_MESSAGE);
			break;
		case PRISON_HAS_CELL_WITH_THAT_NAME:
			MessageDispatcher.sendMessage(player, Colorise.error("The cell ") + Colorise.entity(existingCell.getName()) + Colorise.error(" already exists for ") + Colorise.entity(selectedPrison.getName()) + Colorise.error(" prison."));
			break;
		}
	}

}
