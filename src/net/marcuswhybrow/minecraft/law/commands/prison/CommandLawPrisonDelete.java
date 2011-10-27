package net.marcuswhybrow.minecraft.law.commands.prison;

import java.util.Collection;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.Command;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawPrisonDelete extends Command {
	public static final String DEFINITION = "law prison delete <prison-name>";
	
	public static final int SUCCESS = 0;
	public static final int PRISON_DOES_NOT_EXIST = 1;
	public static final int PRISON_HAS_PRISONERS = 2;
	
	private Player player;
	private Prison prison;
	private LawWorld lawWorld;
	private Prison prevSelectedPrison;
	
	public CommandLawPrisonDelete() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		
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
		
		if (prison.canDelete() == false) {
			return PRISON_HAS_PRISONERS;
		}
		
		// Delete but don't save yet
		prevSelectedPrison = lawWorld.getSelectedPrison(player.getName());
		lawWorld.deletePrison(prison);
			
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		// Success messages
		MessageDispatcher.consoleInfo(player.getName() + " deleted prison \"" + prison.getName() + "\"");
		MessageDispatcher.sendMessage(player, "The prison " + Colorise.entity(prison.getName()) + " has been deleted.");
		
		Collection<Prison> prisonCollection = lawWorld.getPrisons();
		
		Prison[] prisons = prisonCollection.toArray(new Prison[prisonCollection.size()]);
		int numPrisons = prisons.length;
		
		if (prevSelectedPrison == prison) {
			if (numPrisons >= 2) {
				MessageDispatcher.sendMessage(player, "Use " + Colorise.command(CommandLawPrisonSelect.DEFINITION) + " to choose one of the remaining" + Colorise.highlight(" " + prisons.length) + " prisons to work on.");
			} else if (numPrisons == 1) {
				MessageDispatcher.sendMessage(player, "The only remaining prison " + Colorise.entity(prisons[0].getName()) + " is now the selected prison.");
			}
		} else if (prevSelectedPrison == null && numPrisons == 1) {
			// When there is no previously selected prison, one has to
			// set the new selected prison manually, as it cannot be inferred from
			// the current state in the deletePrison method.
			lawWorld.setSelectedPrison(player.getName(), prisons[0].getName());
			MessageDispatcher.sendMessage(player, "The only remaining prison " + Colorise.entity(prisons[0].getName()) + " is now the selected prison.");
		}
		
		if (numPrisons == 0) {
			MessageDispatcher.sendMessage(player, "There are no remaining prisons. Use " + Colorise.command(CommandLawPrisonCreate.DEFINITION) + " to start a new one.");
		}
		lawWorld.save();
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
