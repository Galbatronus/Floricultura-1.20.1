package net.galbatronus.floricultura.block.entity;

import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.floricultura;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities  {
    public static final DeferredRegister<BlockEntityType<?>>  BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, floricultura.MOD_ID);

    public static final RegistryObject<BlockEntityType<WoodenVesselBlockEntity>> CRUSHING_GRAPES =
            BLOCK_ENTITIES.register("crushing_grapes", () ->
                    BlockEntityType.Builder.of(WoodenVesselBlockEntity::new,
                            ModBlocks.WOODEN_VESSEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<FermentationBarrelBlockEntity>> WINE_FERMENTATION =
            BLOCK_ENTITIES.register("wine_fermentation", () ->
                    BlockEntityType.Builder.of(FermentationBarrelBlockEntity::new,
                            ModBlocks.FERMENTATION_BARREL.get()).build(null));

    public static final RegistryObject<BlockEntityType<BerryPressBlockEntity>> PRESS_BERRIES =
            BLOCK_ENTITIES.register("press_berries", () ->
                    BlockEntityType.Builder.of(BerryPressBlockEntity::new,
                            ModBlocks.BERRY_PRESS.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
