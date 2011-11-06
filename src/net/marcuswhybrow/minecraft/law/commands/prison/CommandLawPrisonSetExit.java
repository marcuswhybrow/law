package net.marcuswhybrow.minecraft.law.commands.prison;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.Commands;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawPrisonSetExit extends Command {
	public static final String DEFINITION = "law prison setexit";
	public static final String PERMISSION_NODE = "prison.setexit";
	
	private static final int SUCCESS = 0;
	private static final int CREATE_PRISON_FIRST = 1;
	private static final int SELECT_PRISON_FIRST = 2;
	
	private Player player;
	private LawWorld lawWorld;
	private Prison prison;
	
	public CommandLawPrisonSetExit() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		lawWorld = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		player = (Player) sender;
		lawWorld = Law.get().getLawWorldForPlayer(player);
		prison = lawWorld.getSelectedPrison(player.getName());
	
		if (prison == null) {
			if (lawWorld.getPrisons().size() == 0) {
				return CREATE_PRISON_FIRST;
			} else {
				return SELECT_PRISON_FIRST;
			}
		}
		
		prison.setExitPoint(player.getLocation());
		Law.get().save();
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		MessageDispatcher.consoleInfo(player.getName() + " set the exit point location for prison \"" + prison.getName() + "\"");
		MessageDispatcher.sendMessage(player, "The " + Colorise.entity("exit point") + " for " + Colorise.entity(prison.getName()) + " prison has been set.");
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
