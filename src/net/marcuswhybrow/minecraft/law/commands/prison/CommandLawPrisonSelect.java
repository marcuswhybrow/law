package net.marcuswhybrow.minecraft.law.commands.prison;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawPrisonSelect extends Command {
	public static final String DEFINITION = "law prison select <prison-name>";
	public static final String PERMISSION_NODE = "prison.select";
	
	private static final int SUCCESS = 0;
	private static final int PRISON_DOES_NOT_EXIST = 1;
	
	private Player player;
	private Prison selectedPrison;
	
	public CommandLawPrisonSelect() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		selectedPrison = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		String prisonName = args[3].toLowerCase();
		player = (Player) sender;
		LawWorld lawWorld = Law.get().getLawWorldForPlayer(player);
		selectedPrison = lawWorld.getPrison(prisonName);
		
		if (selectedPrison == null) {
			return PRISON_DOES_NOT_EXIST;
		}
		
		lawWorld.setSelectedPrison(player.getName(), selectedPrison.getName());
		Law.get().save();
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		MessageDispatcher.sendMessage(player, "The prison " + Colorise.entity(selectedPrison.getName()) + " has been selected. All prison commands now apply to this prison.");
	}

	@Override
	public void onFailure(int reason) {
		// TODO Auto-generated method stub
		switch (reason) {
		case PRISON_DOES_NOT_EXIST:
			MessageDispatcher.sendMessage(player, Colorise.error("A prison with that name does not exist."));
			break;
		}
	}

}
