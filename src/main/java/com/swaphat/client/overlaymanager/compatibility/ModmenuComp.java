package com.swaphat.client.overlaymanager.compatibility;

import com.swaphat.client.overlaymanager.config.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModmenuComp implements ModMenuApi {
    @Override
    public com.terraformersmc.modmenu.api.ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreenFactory::create;
    }
}