package net.marcuswhybrow.minecraft.law.commands.prison;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;
import net.marcuswhybrow.minecraft.law.utilities.Validate;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawPrisonCreate extends Command {
	public static final String DEFINITION = "law prison create <prison-name>"; 
	public static final String PERMISSION_NODE = "prison.create";
	
	private static final int SUCCESS = 0;
	private static final int INVALID_PRISON_NAME = 1;
	private static final int PRISON_ALREADY_EXISTS = 2;
	
	private Player player;
	private Prison createdPrison;
	
	public CommandLawPrisonCreate() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		createdPrison = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		player = (Player) sender;
		String prisonName = args[3];
		LawWorld lawWorld = Law.get().getLawWorldForPlayer(player);
		
		if (Validate.name(prisonName) == false) {
			return INVALID_PRISON_NAME;
		}
		
		if (lawWorld.hasPrison(prisonName)) {
			return PRISON_ALREADY_EXISTS;
		}
		
		createdPrison = new Prison(lawWorld, prisonName);
		lawWorld.addPrison(createdPrison);
		
		lawWorld.setSelectedPrison(player.getDisplayName(), createdPrison.getName());
		lawWorld.save();
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		MessageDispatcher.consoleInfo(player.getName() + " created prison \"" + createdPrison.getName() + "\"");
		MessageDispatcher.sendMessage(player, "Prison " + Colorise.entity(createdPrison.getName()) + " has been created.");
	}

	@Override
	public void onFailure(int reason) {
		switch (reason) {
		case INVALID_PRISON_NAME:
			MessageDispatcher.sendMessage(player, Colorise.error("A prison name must contain only letters, numbers, dashes or under scores."));
			break;
		case PRISON_ALREADY_EXISTS:
			MessageDispatcher.sendMessage(player, Colorise.error("A prison with that name already exists."));
			break;
		}
	}

}
