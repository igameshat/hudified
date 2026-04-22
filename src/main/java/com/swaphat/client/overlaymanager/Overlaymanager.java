package com.swaphat.client.overlaymanager;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import com.swaphat.client.overlaymanager.gui.screens.ConfigScreen;
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
						Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen)));
						return 1;
					}));
		});
	}
}