package net.galbatronus.floricultura.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.galbatronus.floricultura.entity.custom.SamoyedoEntity;
import net.galbatronus.floricultura.floricultura;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SamoyedoRenderer extends MobRenderer<SamoyedoEntity, SamoyedoModel<SamoyedoEntity>> {

    public SamoyedoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SamoyedoModel<>(pContext.bakeLayer(ModModelLayers.SAMOYEDO_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SamoyedoEntity samoyedoEntity) {
        return new ResourceLocation(floricultura.MOD_ID, "textures/entity/samoyedo.png");
    }

    @Override
    public void render(SamoyedoEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()){
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }


        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
