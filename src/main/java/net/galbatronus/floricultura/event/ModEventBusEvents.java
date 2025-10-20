package net.galbatronus.floricultura.event;

import net.galbatronus.floricultura.entity.ModEntities;
import net.galbatronus.floricultura.entity.custom.SamoyedoEntity;
import net.galbatronus.floricultura.floricultura;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = floricultura.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SAMOYEDO.get(), SamoyedoEntity.createAttributes().build());
    }
}
