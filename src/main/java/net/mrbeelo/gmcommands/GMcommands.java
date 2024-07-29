package net.mrbeelo.gmcommands;

import net.fabricmc.api.ModInitializer;
import net.mrbeelo.gmcommands.modaddons.ModCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GMcommands implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("gmcommands");
	public static final String MOD_ID = "gmcommands";

	@Override
	public void onInitialize() {
		ModCommands.load();
	}
}