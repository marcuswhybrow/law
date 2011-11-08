package net.marcuswhybrow.minecraft.law.commands;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.events.LawDetainEndEvent;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawFree extends Command {
	public static final String DEFINITION = "law free <player-name>";
	public static final String PERMISSION_NODE = "imprisonment.free";
	
	private static final int SUCCESS = 0;
	private static final int PLAYER_IS_NOT_IMPRISONED_IN_THIS_WORLD = 1;
	
	private Player player;
	private LawWorld lawWorld;
	private String targetPlayerName;
	private PrisonDetainee detainee;
	
	public CommandLawFree() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		lawWorld = null;
		targetPlayerName = null;
		detainee = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		targetPlayerName = args[2];
		player = (Player) sender;
		lawWorld = (LawWorld) Law.get().getLawWorldForPlayer(player);
		
		detainee = lawWorld.getDetainee(targetPlayerName);
		
		if (detainee == null) {
			return PLAYER_IS_NOT_IMPRISONED_IN_THIS_WORLD;
		}
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		Law.fireEvent(new LawDetainEndEvent(player, detainee));
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
