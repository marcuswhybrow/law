package net.marcuswhybrow.minecraft.law.utilities;

import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonCreate;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonSelect;

public class Commands {
	public static final String CREATE_PRISON_MESSAGE = Colorise.error("You must ") + Colorise.highlight("create a prison") +  Colorise.error(" before using this command. Use ") + Colorise.command("/" + CommandLawPrisonCreate.DEFINITION) + Colorise.error(".");
	public static final String SELECT_PRISON_MESSAGE = Colorise.error("You must ") + Colorise.highlight("select a prison") +  Colorise.error(" before using this command. Use ") + Colorise.command("/" + CommandLawPrisonSelect.DEFINITION) + Colorise.error(".");
}
