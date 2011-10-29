package net.marcuswhybrow.minecraft.law.commands;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.exceptions.IllegalCommandDefinitionException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLawReportsPrisons extends Command {
	public static final String DEFINITION = "law reports prisons";
	public static final String PERMISSION_NODE = "reports.prisons";
	
	private static final int SUCCESS = 0;
	
	public CommandLawReportsPrisons() throws IllegalCommandDefinitionException {
		super(DEFINITION);
		setPermissionNode(PERMISSION_NODE);
	}

	@Override
	public int onExecute(CommandSender sender, String[] args) {
		Player player = sender instanceof Player ? (Player) sender: null;
		
		Prison selectedPrison;
		
		MessageDispatcher.sendMessage(sender, "Prison Report: ");
		
		for (LawWorld lawWorld : Law.get().getWorlds()) {
			MessageDispatcher.sendMessageWithoutPrefix(sender, "  " + lawWorld.getName() + ":");
			selectedPrison = player != null ? lawWorld.getSelectedPrison(player.getName()) : null;
			for (Prison prison : lawWorld.getPrisons()) {
				if (prison == selectedPrison) {
					MessageDispatcher.sendMessageWithoutPrefix(sender, "    " + Colorise.highlight(prison.getName()) + " (selected)");
				} else {
					MessageDispatcher.sendMessageWithoutPrefix(sender, "    " + prison.getName());
				}
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
