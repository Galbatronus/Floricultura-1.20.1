package net.galbatronus.floricultura.block.custom;

import net.galbatronus.floricultura.block.entity.WoodenVesselBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class WoodenVesselBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Stream.of(
            Block.box(0, 0, 0, 16, 8, 2),   // parte frontal
            Block.box(0, 0, 14, 16, 8, 16), // parte trasera
            Block.box(0, 0, 2, 2, 8, 14),   // pilar izquierdo
            Block.box(14, 0, 2, 16, 8, 14), // pilar derecho
            Block.box(2, 0, 2, 14, 2, 14)   // centro inferior
    ).reduce(Shapes::or).get();

    public WoodenVesselBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenVesselBlockEntity(pos, state);
    }


    // Lógica para que el BE dropee su contenido al ser roto
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (oldState.getBlock() != newState.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof WoodenVesselBlockEntity vessel) {
                vessel.drops();
            }
        }
        super.onRemove(oldState, level, pos, newState, isMoving);
    }

    // Lógica para "machacar" las uvas al caer sobre el bloque
    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        // Asegura que solo se ejecute en el servidor, que sea un jugador y que la distancia de caída sea suficiente
        if (!level.isClientSide && entity instanceof Player player && fallDistance > 0.5f) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WoodenVesselBlockEntity vessel) {
                vessel.tryCrush(player); // Llama al método de la entidad de bloque
            }
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }



    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        ItemStack heldItem = player.getItemInHand(hand);
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof WoodenVesselBlockEntity vessel)) return InteractionResult.PASS;

        // 1. INTENTAR INSERTAR UVA
        if (!heldItem.isEmpty() && vessel.isValidGrape(heldItem)) {
            if (vessel.tryInsertItem(heldItem)) {
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1); // Consume solo 1 uva, ya que tryInsertItem maneja la inserción de 1
                }
                return InteractionResult.CONSUME;
            }
        }

        // 2. INTENTAR DRENAR A BOTELLA
        if (heldItem.is(Items.GLASS_BOTTLE)) {
            ItemStack filledBottle = vessel.tryDrainToBottle();

            if (!filledBottle.isEmpty()) {
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1); // Consume la botella vacía
                }

                // Intenta añadir la botella llena al inventario, si falla, la dropea
                if (!player.getInventory().add(filledBottle)) {
                    player.drop(filledBottle, false);
                }

                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }
}