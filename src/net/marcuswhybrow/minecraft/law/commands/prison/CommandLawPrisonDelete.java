package net.marcuswhybrow.minecraft.law.commands.prison;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.events.LawPrisonDeleteEvent;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawPrisonDelete extends Command {
	public static final String DEFINITION = "law prison delete <prison-name>";
	public static final String PERMISSION_NODE = "prison.delete";
	
	public static final int SUCCESS = 0;
	public static final int PRISON_DOES_NOT_EXIST = 1;
	public static final int PRISON_HAS_PRISONERS = 2;
	
	private Player player;
	private Prison prison;
	private LawWorld lawWorld;
	
	public CommandLawPrisonDelete() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		setPermissionNode(PERMISSION_NODE);
		
		player = null;
		prison = null;
		lawWorld = null;
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		player = (Player) sender;
		String prisonName = args[3].toLowerCase();
		lawWorld = Law.get().getLawWorldForPlayer(player);
		prison = lawWorld.getPrison(prisonName);
		
		if (prison == null) {
			return PRISON_DOES_NOT_EXIST;
		}
		
		if (prison.hasPrisoners()) {
			return PRISON_HAS_PRISONERS;
		}
			
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		Law.fireEvent(new LawPrisonDeleteEvent(player, prison));
	}

	@Override
	public void onFailure(int reason) {
		switch (reason) {
		case PRISON_DOES_NOT_EXIST:
			MessageDispatcher.sendMessage(player, Colorise.error("A prison with that name does not exist."));
			break;
		case PRISON_HAS_PRISONERS:
			MessageDispatcher.sendMessage(player, Colorise.error(Colorise.error("Prison ") + Colorise.entity(prison.getName()) + Colorise.error(" has prisoners, you must free them before deleting it.")));
			break;
		}
		
	}

}
