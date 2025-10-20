package net.galbatronus.floricultura.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.galbatronus.floricultura.floricultura;
import net.galbatronus.floricultura.screen.renderer.EnergyInfoArea;
import net.galbatronus.floricultura.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;


public class BerryPressScreen extends AbstractContainerScreen<BerryPressMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(floricultura.MOD_ID, "textures/gui/berry_press_gui.png");


    private EnergyInfoArea energyArea;
    private FluidTankRenderer fluidTankRenderer;

    public BerryPressScreen(BerryPressMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }



    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        // Obtiene la energía del BlockEntity a través del menú
        IEnergyStorage energyStorage = this.menu.blockEntity.getEnergyStorage();
        // Coordenadas (x, y) donde quieres que se dibuje la barra de energía
        // (x = leftPos + 156, y = topPos + 13)
        this.energyArea = new EnergyInfoArea(this.leftPos + 22, this.topPos + 6, energyStorage, 131, 5);

        // Obtiene la capacidad del tanque
        int fluidCapacity = this.menu.blockEntity.getFluidCapacity();
        // Inicializa el renderizador del tanque de fluido
        this.fluidTankRenderer = new FluidTankRenderer(fluidCapacity, true, 53, 49);
    }

    private int getScaledEnergy() {
        // Obtiene la energía actual y máxima desde el menú.
        int energy = this.menu.data.get(2);
        int maxEnergy = this.menu.data.get(3);

        // El ancho total de la barra en píxeles (125 - 22 = 103).
        int barWidth = 130;

        // Evita la división por cero y devuelve el ancho calculado.
        if (maxEnergy == 0) {
            return 0;
        }

        // La fórmula clave: (energía_actual / energía_máxima) * ancho_total.
        // Se usa (float) para asegurar una división decimal precisa.
        return (int) (((float) energy / maxEnergy) * barWidth);
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Dibuja la barra de energía
        guiGraphics.blit(TEXTURE, x + 22, y + 6, 22, 6, 103, 4);

        // 2. 💡 Dibuja la porción de la barra llena encima.
        int scaledWidth = getScaledEnergy(); // Calcula qué tan ancha debe ser la barra.
        if (scaledWidth > 0) {
            // Dibuja solo una parte de la textura de la barra llena.
            // Posición en pantalla: (x + 22, y + 6).
            // Coordenadas en la textura (UV): (22, 178).
            // Tamaño: 'scaledWidth' de ancho, 4 de alto.
            guiGraphics.blit(TEXTURE, x + 22, y + 6, 22, 178, scaledWidth, 4);
        }

        // Dibuja el tanque de fluido
        FluidStack fluidStack = this.menu.blockEntity.getFluidStack();
        // Coordenadas (x, y) donde quieres que se dibuje el tanque
        // (x = leftPos + 100, y = topPos + 13)
        fluidTankRenderer.render(guiGraphics.pose(), this.leftPos + 53, this.topPos + 49, fluidStack);

        // Dibuja la flecha de progreso
        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 98, y + 17, 189, 1, 7, menu.getScaledProgress());
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);


        // Tooltip de Energía
        if(isHovering(energyArea.area.getX(), energyArea.area.getY(), energyArea.area.getWidth(), energyArea.area.getHeight(), mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(this.font, energyArea.getTooltips(), mouseX, mouseY);
        }

        // Tooltip del Tanque de Fluido
        // Coordenadas (x, y) donde dibujaste el tanque en renderBg
        int fluidTankX = this.leftPos + 53;
        int fluidTankY = this.topPos + 49;
        if(isHovering(fluidTankX, fluidTankY, fluidTankRenderer.getWidth(), fluidTankRenderer.getHeight(), mouseX, mouseY)) {
            FluidStack fluidStack = this.menu.blockEntity.getFluidStack();
            guiGraphics.renderComponentTooltip(this.font, fluidTankRenderer.getTooltip(fluidStack, TooltipFlag.Default.NORMAL), mouseX, mouseY);
        }
    }


}
