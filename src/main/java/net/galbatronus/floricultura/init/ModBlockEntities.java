package net.galbatronus.floricultura.init;

import net.galbatronus.floricultura.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.galbatronus.floricultura.floricultura;
import net.galbatronus.floricultura.entities.blockentity.CampanoraLeaveBlockEntity;
import net.galbatronus.floricultura.block.custom.CampanoraLeaveBlock;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, floricultura.MOD_ID);

    public static final RegistryObject<BlockEntityType<CampanoraLeaveBlockEntity>> CAMPANORA_LEAVE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("campanora_leave_block_entity",
                    () -> BlockEntityType.Builder.of(
                            CampanoraLeaveBlockEntity::new,
                            ModBlocks.CAMPANORA_LEAVE.get()
                    ).build(null));
}
