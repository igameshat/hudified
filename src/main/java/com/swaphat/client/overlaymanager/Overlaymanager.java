package com.swaphat.client.overlaymanager;

import com.swaphat.client.overlaymanager.config.ConfigManager;
import com.swaphat.client.overlaymanager.config.ConfigScreenFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Overlaymanager implements ClientModInitializer {
	public static final String MOD_ID = "overlay-manager";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ConfigManager.load();
		ConfigScreenFactory.registerKeybinds();
		LOGGER.info("Overlay Manager initialized.");

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("hi")
					.executes(context -> {
						return 1;
					}));
		});
	}
}