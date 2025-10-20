package net.galbatronus.floricultura.entity;

import net.galbatronus.floricultura.entity.custom.SamoyedoEntity;
import net.galbatronus.floricultura.floricultura;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, floricultura.MOD_ID);

    public static final RegistryObject<EntityType<SamoyedoEntity>> SAMOYEDO =
            ENTITY_TYPES.register("samoyedo", () -> EntityType.Builder.of(SamoyedoEntity::new, MobCategory.CREATURE)
                    .sized(0.625f, 0.5f) .build("samoyedo"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
