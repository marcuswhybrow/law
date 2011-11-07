package net.marcuswhybrow.minecraft.law.commands.prison.cell;

import java.util.Collection;

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

public class CommandLawPrisonCellList extends Command {
	public static final String DEFINITION = "law prison cell list";
	public static final String PERMISSION_NODE = "prison.cell.list";
	
	private static final int SUCCESS = 0;
	private static final int CREATE_PRISON_FIRST = 1;
	private static final int SELECT_PRISON_FIRST = 2;
	
	private Player player;
	private LawWorld lawWorld;
	private Prison selectedPrison;
	
	public CommandLawPrisonCellList() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		lawWorld = null;
		selectedPrison = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
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
		
		MessageDispatcher.sendMessage(player, "Cell list for " + Colorise.entity(selectedPrison.getName()) + " prison:");
		
		Collection<PrisonCell> cells = selectedPrison.getCells();
		
		if (cells.size() == 0) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "  There are no cells yet. Use " + Colorise.command(CommandLawPrisonCellCreate.DEFINITION) + " to get started.");
		}
		
		for (PrisonCell cell : cells) {
			MessageDispatcher.sendMessageWithoutPrefix(player, "  " + cell.getName() + " - " + cell.getPrisoners().size() + " prisoners");
		}
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		// Nothing
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
		}
	}

}
