package net.galbatronus.floricultura.block.custom;

import net.galbatronus.floricultura.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class GrapesCropBlock  extends CropBlock {

    private final Supplier<Item> grapeItem;

    public GrapesCropBlock(Properties pProperties, Supplier<Item> grapeItem) {
        super(pProperties);
        this.grapeItem = grapeItem;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModBlocks.STAKE.get());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;

        if (level.getRawBrightness(pos.above(), 0) >= 9) {
            float chance = CropBlock.getGrowthSpeed(this, level, pos);


            Biome biome = level.getBiome(pos).value();
            if (biome.getBaseTemperature() < 0.15F) {
                return;
            }


            if (random.nextFloat() < (chance / 4.0F)) {
                int age = getAge(state);
                if (age < getMaxAge()) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
                }
            }
        }
    }
    @Override
    public int getMaxAge() {
        return 3;
    }

    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    @Override
    public VoxelShape getShape (BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (getAge(state) >= getMaxAge()) {
            int grapesAmount = 2 + level.random.nextInt(2); // 2 o 3
            ItemStack grapes = new ItemStack(this.grapeItem.get(), grapesAmount);
            Block.popResource(level, pos, grapes);
            level.setBlock(pos, this.getStateForAge(1), 2);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
