package net.galbatronus.floricultura.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.galbatronus.floricultura.block.entity.WoodenVesselBlockEntity;
import net.galbatronus.floricultura.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.util.Map;

public class WoodenVesselBlockEntityRenderer implements BlockEntityRenderer<WoodenVesselBlockEntity> {
    public WoodenVesselBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    private static final Map<Item, Item> GRAPE_TO_MANY_GRAPES = Map.of(
            ModItems.PURPLE_GRAPES.get(), ModItems.MANY_PURPLE_GRAPES.get(),
            ModItems.WHITE_GRAPES.get(), ModItems.MANY_WHITE_GRAPES.get(),
            ModItems.PINK_GRAPES.get(), ModItems.MANY_PINK_GRAPES.get()
    );

    private static final Map<Item, Item> CRUSH_STAGE_1 = Map.of(
            ModItems.PURPLE_GRAPES.get(), ModItems.PURPLE_GRAPES_STAGE1.get(),
            ModItems.WHITE_GRAPES.get(), ModItems.WHITE_GRAPES_STAGE1.get(),
            ModItems.PINK_GRAPES.get(), ModItems.PINK_GRAPES_STAGE1.get()
    );

    private static final Map<Item, Item> CRUSH_STAGE_2 = Map.of(
            ModItems.PURPLE_GRAPES.get(), ModItems.PURPLE_GRAPES_STAGE2.get(),
            ModItems.WHITE_GRAPES.get(), ModItems.WHITE_GRAPES_STAGE2.get(),
            ModItems.PINK_GRAPES.get(), ModItems.PINK_GRAPES_STAGE2.get()
    );

    @Override
    public void render(WoodenVesselBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderStack();
        ItemStack baseStack = pBlockEntity.getRenderStack();
        if (itemStack.isEmpty()) return;
        if (baseStack.isEmpty()) return;

        int fill = pBlockEntity.getItemFillPercentage();
        int crush = pBlockEntity.getCrushCounter();
        if (fill == 0) return;

        float yOffset = 0.0f;
        float sxOffset = 0.0f;
        float syOffset = 0.0f;
        float szOffset = 0.0f;
        float rOffset = 0;
        ItemStack renderStack;


        if (fill < 33) {

            Item original = baseStack.getItem();
            if (crush <= 2 && CRUSH_STAGE_1.containsKey(original)) {
                renderStack = new ItemStack(CRUSH_STAGE_1.get(original));
            } else if (crush <= 5 && CRUSH_STAGE_2.containsKey(original)) {
                renderStack = new ItemStack(CRUSH_STAGE_2.get(original));
            }


            renderStack = itemStack.copy();

        } else {
            Item manyGrapes = GRAPE_TO_MANY_GRAPES.get(itemStack.getItem());
            if (manyGrapes == null) return; // no hay versión "many" para este item
            renderStack = new ItemStack(manyGrapes);

            yOffset = 0.3f;
            sxOffset = 1.2f;
            syOffset = 1.2f;
            szOffset = 1.2f;
            rOffset = 90;

            if (fill > 66) {
                yOffset = 0.4f;
                sxOffset = 1.2f;
                syOffset = 1.2f;
                szOffset = 1.2f;
                rOffset = 90;
            }
        }

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.25f + yOffset, 0.5f);
        pPoseStack.scale(0.35f + sxOffset, 0.35f + syOffset, 0.35f + szOffset);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(90 - rOffset));

        itemRenderer.renderStatic(
                renderStack,
                ItemDisplayContext.FIXED,
                getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(),
                1
        );

        pPoseStack.popPose();
    }



    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
