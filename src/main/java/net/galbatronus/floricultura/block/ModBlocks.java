package net.galbatronus.floricultura.block;

import net.galbatronus.floricultura.block.custom.CampanoraLeaveBlock;
import net.galbatronus.floricultura.floricultura;
import net.galbatronus.floricultura.item.ModItems;

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

    public static final RegistryObject<Block> CAMPANORA_LEAVE = BLOCKS.register("campanora_leave",
            () -> new CampanoraLeaveBlock(BlockBehaviour.Properties.of()
                    .strength(0.2f)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks()));



    public static class CustomLeavesBlock extends LeavesBlock {

        public CustomLeavesBlock(Properties pProperties) {
            super(pProperties);
        }
    }



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

    }


            public static void regiter(IEventBus eventBus){
                BLOCKS.register(eventBus);
            }
 }
