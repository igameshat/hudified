package com.swaphat.client.overlaymanager;

import com.swaphat.client.overlaymanager.config.ConfigManager;
import com.swaphat.client.overlaymanager.gui.widgets.screen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Overlaymanager implements ClientModInitializer {
	public static final String MOD_ID = "overlay-manager";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ConfigManager.load();
		LOGGER.info("Overlay Manager initialized.");

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("hi")
					.executes(context -> {
						// Use execute() to ensure the screen opens on the main render thread
						// and after the chat screen has begun closing.
						Minecraft.getInstance().execute(() -> {
							Minecraft.getInstance().setScreen(new screen(Minecraft.getInstance().screen));
						});

						LOGGER.info("Command /hi executed - Opening Screen");
						return 1;
					}));
		});
	}
}