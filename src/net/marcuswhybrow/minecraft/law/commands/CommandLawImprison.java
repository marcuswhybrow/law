package net.marcuswhybrow.minecraft.law.commands;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonSetExit;
import net.marcuswhybrow.minecraft.law.commands.prison.cell.CommandLawPrisonCellCreate;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.Commands;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawImprison extends Command {
	public static final String DEFINITION = "law imprison <player-name> [prison-name] [cell-name]";
	public static final String PERMISSION_NODE = "imprisonment.imprison";
	
	private static final int SUCCESS = 0;
	private static final int CREATE_PRISON_FIRST = 1;
	private static final int SELECT_PRISON_FIRST = 2;
	private static final int WORLD_DOES_NOT_HAVE_PRISON_WITH_THAT_NAME = 3;
	private static final int PLAYER_IS_ALREADY_IMPRISONED_IN_THAT_CELL = 4;
	private static final int PRISON_IS_NOT_OPERATIONAL = 5;
	private static final int PRISON_DOES_NOT_HAVE_CELL_WITH_THAT_NAME = 6;
	
	private Player player;
	private LawWorld lawWorld;
	private String targetPlayerName;
	private String targetPrisonName;
	private String targetCellName;
	private Prison targetPrison;
	
	public CommandLawImprison() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		lawWorld = null;
		targetPlayerName = null;
		targetPrisonName = null;
		targetCellName = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		targetPlayerName = args[2];
		targetPrisonName = args.length >= 4 ? args[3] : null;
		targetCellName = args.length >= 5 ? args[4] : PrisonCell.DEFAULT_NAME;
		player = (Player) sender;
		lawWorld = (LawWorld) Law.get().getLawWorldForPlayer(player);

		
		targetPrison = targetPrisonName == null ? lawWorld.getSelectedPrison(player.getName()) : lawWorld.getPrison(targetPrisonName);
		
		if (targetPrison == null) {
			if (targetPrisonName == null) {
				if (lawWorld.getPrisons().size() == 0) {
					return CREATE_PRISON_FIRST;
				} else {
					return SELECT_PRISON_FIRST;
				}
			} else {
				return WORLD_DOES_NOT_HAVE_PRISON_WITH_THAT_NAME;
			}
		}
		
		targetPrisonName = targetPrison.getName();
		
		if (targetPrison.isOperational() == false) {
			return PRISON_IS_NOT_OPERATIONAL;
		}
		
		PrisonCell targetCell = targetPrison.getCell(targetCellName);
		
		if (targetCell == null) {
			return PRISON_DOES_NOT_HAVE_CELL_WITH_THAT_NAME;
		}
		
		if (targetCell.hasPrisoner(targetPlayerName)) {
			return PLAYER_IS_ALREADY_IMPRISONED_IN_THAT_CELL;
		}
		
		Law.get().imprisonPlayer(targetPlayerName, targetCell);
		Law.get().save();
		
		MessageDispatcher.consoleInfo("world: " + lawWorld.getName());
		for (String s : lawWorld.getPrisoners()) {
			MessageDispatcher.consoleInfo("world inv: " + s);
		}
		
		MessageDispatcher.consoleInfo("prison: " + targetPrison.getName());
		for (String s : targetPrison.getPrisoners()) {
			MessageDispatcher.consoleInfo("prison inv: " + s);
		}
		
		MessageDispatcher.consoleInfo("cell: " + targetCell.getName());
		for (String s : targetCell.getPrisoners()) {
			MessageDispatcher.consoleInfo("cell inv: " + s);
		}
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		MessageDispatcher.consoleInfo(player.getName() + " imprisoned \"" + targetPlayerName + "\" in \"" + targetPrison.getName() + "\" prison");
		
		if (Bukkit.getPlayerExact(targetPlayerName) != null) {
			MessageDispatcher.sendMessage(player, "Imprisoned " + Colorise.entity(targetPlayerName) + " in " + Colorise.entity(targetPrison.getName()) + " prison.");
		} else {
			MessageDispatcher.sendMessage(player, "Imprisoned " + Colorise.entity(targetPlayerName) + " in " + Colorise.entity(targetPrison.getName()) + " prison. This player is offline but will be imprisoned when they return.");
		}
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
		case WORLD_DOES_NOT_HAVE_PRISON_WITH_THAT_NAME:
			MessageDispatcher.sendMessage(player, Colorise.error("This world does not have a prison with that name."));
			break;
		case PLAYER_IS_ALREADY_IMPRISONED_IN_THAT_CELL:
			MessageDispatcher.sendMessage(player, "The player " + Colorise.entity(targetPlayerName) + " is already imprisoned within that cell.");
			break;
		case PRISON_IS_NOT_OPERATIONAL:
			// Deliver appropriate messages to help the user make the prison operational.
			if (targetPrison.hasDefaultCell() == false) {
				MessageDispatcher.sendMessage(player, Colorise.error("Prison ") + Colorise.entity(targetPrison.getName()) + Colorise.error(" has no cell with name \"default\". Use ") + Colorise.command("/" + CommandLawPrisonCellCreate.DEFINITION) + Colorise.error("to rectify this."));
			}
			if (targetPrison.hasExitPoint() == false) {
				MessageDispatcher.sendMessage(player, Colorise.error("Prison ") + Colorise.entity(targetPrison.getName()) + Colorise.error(" has no exit location. Use ") + Colorise.command("/" + CommandLawPrisonSetExit.DEFINITION) + Colorise.error("to rectify this."));
			}
			break;
		case PRISON_DOES_NOT_HAVE_CELL_WITH_THAT_NAME:
			MessageDispatcher.sendMessage(player, Colorise.error("Prison " + Colorise.entity(targetPrison.getName()) + Colorise.error(" does not have a cell with that name.")));
			break;
		}
	}

}
