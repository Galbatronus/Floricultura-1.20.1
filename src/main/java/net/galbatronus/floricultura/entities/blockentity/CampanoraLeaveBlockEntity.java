package net.galbatronus.floricultura.entities.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.galbatronus.floricultura.init.ModBlockEntities;

public class CampanoraLeaveBlockEntity extends BlockEntity {
    public CampanoraLeaveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAMPANORA_LEAVE_BLOCK_ENTITY.get(), pos, state);
    }
}
