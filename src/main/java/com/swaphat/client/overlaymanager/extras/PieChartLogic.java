package com.swaphat.client.overlaymanager.extras;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.Minecraft;

public class PieChartLogic {
    public static void updateValues() {
        Minecraft client = Minecraft.getInstance();
        int width = client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();

        // Check if we need to initialize or handle a resize
        if (ConfigInstance.PieChart.oldWindowWidth == -1) {
            ConfigInstance.PieChart.oldWindowWidth = width;
            ConfigInstance.PieChart.oldWindowHeight = height;
        }

        // Logic for relative movement goes here if window size changes
        if (width != ConfigInstance.PieChart.oldWindowWidth || height != ConfigInstance.PieChart.oldWindowHeight) {
            // Handle scaling coordinates if window changes
            ConfigInstance.PieChart.oldWindowWidth = width;
            ConfigInstance.PieChart.oldWindowHeight = height;
        }
    }
}