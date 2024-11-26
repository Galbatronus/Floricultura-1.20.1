package net.galbatronus.floricultura.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.galbatronus.floricultura.entities.blockentity.CampanoraLeaveBlockEntity;

public class CampanoraLeaveRenderer implements BlockEntityRenderer<CampanoraLeaveBlockEntity> {

    private final CustomOBJRenderer objRenderer;

    public CampanoraLeaveRenderer(BlockEntityRendererProvider.Context context) {
        this.objRenderer = new CustomOBJRenderer();

        objRenderer.loadModel(
                "floricultura:models/block/campanora_leave.obj",
                "floricultura:textures/block/leave.png"
        );
    }

    @Override
    public void render(CampanoraLeaveBlockEntity blockEntity, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {

        poseStack.pushPose();



        objRenderer.render(poseStack, buffer);

        poseStack.popPose();
    }
}