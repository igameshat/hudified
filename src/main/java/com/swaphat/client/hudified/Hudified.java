package com.swaphat.client.hudified;

import com.swaphat.client.hudified.config.ConfigManager;
import com.swaphat.client.hudified.config.ConfigScreenFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hudified implements ClientModInitializer {
	public static final String MOD_ID = "hudified";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ConfigManager.load();
		ConfigScreenFactory.registerKeybinds();
		LOGGER.info("Overlay Manager initialized.");
	}
}