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

public class CommandLawPrisonList extends Command {
	public static final String DEFINITION = "law prison list";
	public static final String PERMISSION_NODE = "prison.list";
	
	private static final int SUCCESS = 0;
	
	public CommandLawPrisonList() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setType(Type.IN_GAME_ONLY);
		setPermissionNode(PERMISSION_NODE);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		LawWorld lawWorld = Law.get().getLawWorldForPlayer(player);
		Prison selectedPrison = lawWorld.getSelectedPrison(player.getName());
		
		MessageDispatcher.sendMessage(sender,"Prison list for world " + Colorise.entity(lawWorld.getName()) + ": ");
		
		Collection<Prison> prisons = lawWorld.getPrisons();
		if (prisons.size() == 0) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "  There are no prisons yet. Use " + Colorise.command("/law prison create <prison-name>") + " to get started.");
		}
		
		for (Prison prison : lawWorld.getPrisons()) {
			if (prison == selectedPrison) {
				MessageDispatcher.sendMessageWithoutPrefix(sender, "  " + Colorise.highlight(prison.getName()) + " - " + prison.getPrisoners().size() + " prisoners");
			} else {
				MessageDispatcher.sendMessageWithoutPrefix(sender, "  " + prison.getName() + " - " + prison.getPrisoners().size() + " prisoners");
			}
		}
		
		return SUCCESS;
	}

	@Override
	public void onSuccess() {
		// Nothing
	}

	@Override
	public void onFailure(int reason) {
		// Cannot fail
	}

}
