package net.galbatronus.floricultura.screen.renderer;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.gui.GuiGraphics;


public abstract class InfoArea {
    public final Rect2i area;

    protected InfoArea(Rect2i area) {
        this.area = area;
    }

    public abstract void draw(GuiGraphics guiGraphics);

}
