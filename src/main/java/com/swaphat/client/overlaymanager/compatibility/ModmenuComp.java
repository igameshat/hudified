package com.swaphat.client.overlaymanager.compatibility;

import com.swaphat.client.overlaymanager.gui.screens.screen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModmenuComp implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen::new;
    }
}
