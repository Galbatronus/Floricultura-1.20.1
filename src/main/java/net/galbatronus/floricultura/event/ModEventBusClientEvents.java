package net.galbatronus.floricultura.event;

import net.galbatronus.floricultura.block.entity.ModBlockEntities;
import net.galbatronus.floricultura.block.entity.renderer.WoodenVesselBlockEntityRenderer;
import net.galbatronus.floricultura.entity.ModEntities;
import net.galbatronus.floricultura.entity.client.ModModelLayers;
import net.galbatronus.floricultura.entity.client.SamoyedoModel;
import net.galbatronus.floricultura.entity.custom.SamoyedoEntity;
import net.galbatronus.floricultura.floricultura;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = floricultura.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SAMOYEDO_LAYER, SamoyedoModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CRUSHING_GRAPES.get(), WoodenVesselBlockEntityRenderer::new);

    }
}