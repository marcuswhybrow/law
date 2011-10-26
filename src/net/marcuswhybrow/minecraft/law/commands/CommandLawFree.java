package net.marcuswhybrow.minecraft.law.commands;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawFree extends Command {
	public static final String DEFINITION = "law free <player-name>";
	
	private static final int SUCCESS = 0;
	private static final int PLAYER_IS_NOT_IMPRISONED_IN_THIS_WORLD = 1;
	
	private Player player;
	private LawWorld lawWorld;
	private String targetPlayerName;
	private PrisonCell cell;
	
	public CommandLawFree() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		
		player = null;
		lawWorld = null;
		targetPlayerName = null;
		cell = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		targetPlayerName = args[2];
		player = (Player) sender;
		lawWorld = (LawWorld) Law.get().getLawWorldForPlayer(player);
		
		cell = lawWorld.getPrisonerCell(targetPlayerName);
		
		if (cell == null) {
			return PLAYER_IS_NOT_IMPRISONED_IN_THIS_WORLD;
		}
		
		Law.get().freePlayer(targetPlayerName, cell);
		Law.get().save();
			
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		MessageDispatcher.consoleInfo(player.getName() + " freed \"" + targetPlayerName + "\" from \"" + cell.getPrison().getName() + "\" prison");
		
		String message = "Freed " + Colorise.entity(targetPlayerName) + " from " + Colorise.entity(cell.getPrison().getName()) + " prison.";
		if (Bukkit.getPlayerExact(targetPlayerName) == null) {
			message += " This player is offline but will be free when they return.";
		}
		MessageDispatcher.sendMessage(player, message);
	}

	@Override
	public void onFailure(int reason) {
		switch (reason) {
		case PLAYER_IS_NOT_IMPRISONED_IN_THIS_WORLD:
			MessageDispatcher.sendMessage(player, Colorise.error("The player ") + Colorise.highlight(targetPlayerName) + Colorise.error(" was not imprisoned in the world."));
			break;
		}
	}

}
