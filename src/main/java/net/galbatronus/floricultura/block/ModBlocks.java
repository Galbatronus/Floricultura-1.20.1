package net.galbatronus.floricultura.block;

import net.galbatronus.floricultura.block.custom.*;
import net.galbatronus.floricultura.floricultura;
import net.galbatronus.floricultura.fluid.ModFluids;
import net.galbatronus.floricultura.item.ModItems;

import net.minecraft.client.resources.model.Material;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, floricultura.MOD_ID);


    public static final RegistryObject<Block> KANORA_BLOCK = registerBlock("kanora_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission()));

    public static final RegistryObject<Block> POTTED_KANORA = BLOCKS.register("potted_kanora",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.KANORA_BLOCK,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM).noOcclusion()));


    public static final RegistryObject<Block> CAMPANILLAS_CHINAS_BLOCK = registerBlock("campanillas_chinas_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> POTTED_CAMPANILLAS_CHINAS = BLOCKS.register("potted_campanillas_chinas",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.CAMPANILLAS_CHINAS_BLOCK,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_CORNFLOWER).noOcclusion()));

    public static final RegistryObject<Block> DALIA_ROJA_BLOCK = registerBlock("dalia_roja_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> POTTED_DALIA_ROJA = BLOCKS.register("potted_dalia_roja",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.DALIA_ROJA_BLOCK,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_CORNFLOWER).noOcclusion()));

    public static final RegistryObject<Block> DALIA_AMARILLA_BLOCK = registerBlock("dalia_amarilla_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> DALIA_BLANCA_BLOCK = registerBlock("dalia_blanca_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> DALIA_MORADA_BLOCK = registerBlock("dalia_morada_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> DALIA_NARANJA_BLOCK = registerBlock("dalia_naranja_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> DALIA_NEGRA_BLOCK = registerBlock("dalia_negra_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> DALIA_ROSADA_BLOCK = registerBlock("dalia_rosada_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));


    public static final RegistryObject<Block> FRESIA_AMARILLA_BLOCK = registerBlock("fresia_amarilla_block",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 1,
                    BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> POTTED_FRESIA_AMARILLA = BLOCKS.register("potted_fresia_amarilla",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.FRESIA_AMARILLA_BLOCK,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_CORNFLOWER).noOcclusion()));


    public static final RegistryObject<Block> CAMPANORA_LEAVE = registerBlock("campanora_leave",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> STAKE = BLOCKS.register("stake",
            () -> new ModStakeBlock(BlockBehaviour.Properties.of().strength(1f).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> PURPLE_GRAPES_CROP = BLOCKS.register("purple_grapes_crop",
            () -> new GrapesCropBlock(BlockBehaviour.Properties.of().sound(SoundType.CROP), ModItems.PURPLE_GRAPES));

    public static final RegistryObject<Block> WHITE_GRAPES_CROP = BLOCKS.register("white_grapes_crop",
            () -> new GrapesCropBlock(BlockBehaviour.Properties.of().sound(SoundType.CROP), ModItems.WHITE_GRAPES));

    public static final RegistryObject<Block> PINK_GRAPES_CROP = BLOCKS.register("pink_grapes_crop",
            () -> new GrapesCropBlock(BlockBehaviour.Properties.of().sound(SoundType.CROP), ModItems.PINK_GRAPES));

    public static final RegistryObject<Block> WOODEN_VESSEL = registerBlock("wooden_vessel",
            () -> new WoodenVesselBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB).noOcclusion()));

    public static final RegistryObject<Block> FERMENTATION_BARREL = registerBlock("fermentation_barrel",
            () -> new FermentationBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion()));

    public static final RegistryObject<Block> BERRY_PRESS = registerBlock("berry_press",
            () -> new BerryPressBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion()));

    public static final RegistryObject<LiquidBlock> PURPLE_GRAPE_JUICE_BLOCK = BLOCKS.register("purple_grape_juice_block",
            () -> new LiquidBlock(ModFluids.SOURCE_PURPLE_GRAPE_JUICE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> WHITE_GRAPE_JUICE_BLOCK = BLOCKS.register("white_grape_juice_block",
            () -> new LiquidBlock(ModFluids.SOURCE_WHITE_GRAPE_JUICE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> PINK_GRAPE_JUICE_BLOCK = BLOCKS.register("pink_grape_juice_block",
            () -> new LiquidBlock(ModFluids.SOURCE_PINK_GRAPE_JUICE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> RED_WINE_BLOCK = BLOCKS.register("red_wine_block",
            () -> new LiquidBlock(ModFluids.SOURCE_RED_WINE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> WHITE_WINE_BLOCK = BLOCKS.register("white_wine_block",
            () -> new LiquidBlock(ModFluids.SOURCE_WHITE_WINE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> ROSE_WINE_BLOCK = BLOCKS.register("rose_wine_block",
            () -> new LiquidBlock(ModFluids.SOURCE_ROSE_WINE, BlockBehaviour.Properties.copy(Blocks.WATER)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

    }


            public static void register(IEventBus eventBus){
                BLOCKS.register(eventBus);
            }
 }
