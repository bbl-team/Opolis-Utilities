package com.benbenlaw.opolisutilities.screen.utils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class OpolisUtilitiesWidget extends AbstractWidget {

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public OpolisUtilitiesWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
