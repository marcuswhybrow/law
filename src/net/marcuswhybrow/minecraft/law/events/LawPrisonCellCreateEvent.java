package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

public class LawPrisonCellCreateEvent extends LawPrisonCellEvent {
	private static final long serialVersionUID = 2946894571419504837L;

	public LawPrisonCellCreateEvent(final Player sourcePlayer, final PrisonCell prisonCell) {
		super("LawPrisonCelLCreateEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrisonCell(prisonCell);
	}

}
