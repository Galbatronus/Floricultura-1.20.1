package net.galbatronus.floricultura.item;

import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.entity.ModEntities;
import net.galbatronus.floricultura.floricultura;
import net.galbatronus.floricultura.fluid.ModFluids;
import net.galbatronus.floricultura.item.custom.FuelItem;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, floricultura.MOD_ID);

    public static final RegistryObject<Item> SAMOYEDO_SPAWN_EGG = ITEMS.register("samoyedo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SAMOYEDO, 0x7e9680, 0xc5d1c5,
                    new Item.Properties()));

    public static final RegistryObject<Item> STAKE_ITEM = ITEMS.register("stake",
            () -> new BlockItem(ModBlocks.STAKE.get(), new Item.Properties()));

    public static final RegistryObject<Item> PURPLE_GRAPES = ITEMS.register("purple_grapes",
            () -> new Item(new Item.Properties().food(ModFoods.PURPLE_GRAPES)));

    public static final RegistryObject<Item> MANY_PURPLE_GRAPES = ITEMS.register("many_purple_grapes",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PURPLE_GRAPES_STAGE1 = ITEMS.register("purple_grapes_stage1",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PURPLE_GRAPES_STAGE2 = ITEMS.register("purple_grapes_stage2",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHITE_GRAPES = ITEMS.register("white_grapes",
            () -> new Item(new Item.Properties().food(ModFoods.WHITE_GRAPES)));

    public static final RegistryObject<Item> MANY_WHITE_GRAPES = ITEMS.register("many_white_grapes",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHITE_GRAPES_STAGE1 = ITEMS.register("white_grapes_stage1",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHITE_GRAPES_STAGE2 = ITEMS.register("white_grapes_stage2",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PINK_GRAPES = ITEMS.register("pink_grapes",
            () -> new Item(new Item.Properties().food(ModFoods.PINK_GRAPES)));

    public static final RegistryObject<Item> MANY_PINK_GRAPES = ITEMS.register("many_pink_grapes",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PINK_GRAPES_STAGE1 = ITEMS.register("pink_grapes_stage1",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PINK_GRAPES_STAGE2 = ITEMS.register("ppink_grapes_stage2",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PURPLE_GRAPES_SEEDS = ITEMS.register("purple_grapes_seeds",
            () -> new ItemNameBlockItem(ModBlocks.PURPLE_GRAPES_CROP.get(),new Item.Properties()));

    public static final RegistryObject<Item> WHITE_GRAPES_SEEDS = ITEMS.register("white_grapes_seeds",
            () -> new ItemNameBlockItem(ModBlocks.WHITE_GRAPES_CROP.get(),new Item.Properties()));

    public static final RegistryObject<Item> PINK_GRAPES_SEEDS = ITEMS.register("pink_grapes_seeds",
            () -> new ItemNameBlockItem(ModBlocks.PINK_GRAPES_CROP.get(),new Item.Properties()));

    public static final RegistryObject<Item> WINE_BOTTLE = ITEMS.register("wine_bottle",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> PURPLE_GRAPES_BOTTLE = ITEMS.register("purple_grapes_bottle",
            () -> new Item(new Item.Properties().food(ModFoods.PURPLE_GRAPES_BOTTLE).stacksTo(16)));

    public static final RegistryObject<Item> WHITE_GRAPES_BOTTLE = ITEMS.register("white_grapes_bottle",
            () -> new Item(new Item.Properties().food(ModFoods.WHITE_GRAPES_BOTTLE).stacksTo(16)));

    public static final RegistryObject<Item> ROSE_GRAPES_BOTTLE = ITEMS.register("rose_grapes_bottle",
            () -> new Item(new Item.Properties().food(ModFoods.ROSE_GRAPES_BOTTLE).stacksTo(16)));

    public static final RegistryObject<Item> RED_WINE = ITEMS.register("red_wine",
            () -> new Item(new Item.Properties().food(ModFoods.RED_WINE).stacksTo(3)));

    public static final RegistryObject<Item> WHITE_WINE = ITEMS.register("white_wine",
            () -> new Item(new Item.Properties().food(ModFoods.WHITE_WINE).stacksTo(3)));

    public static final RegistryObject<Item> ROSE_WINE = ITEMS.register("rose_wine",
            () -> new Item(new Item.Properties().food(ModFoods.ROSE_WINE).stacksTo(3)));

    public static final RegistryObject<Item> PURPLE_GRAPE_JUICE_BUCKET = ITEMS.register("purple_grape_juice_bucket",
            () -> new BucketItem(ModFluids.SOURCE_PURPLE_GRAPE_JUICE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> WHITE_GRAPE_JUICE_BUCKET = ITEMS.register("white_grape_juice_bucket",
            () -> new BucketItem(ModFluids.SOURCE_WHITE_GRAPE_JUICE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> PINK_GRAPE_JUICE_BUCKET = ITEMS.register("pink_grape_juice_bucket",
            () -> new BucketItem(ModFluids.SOURCE_PINK_GRAPE_JUICE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> RED_WINE_BUCKET = ITEMS.register("red_wine_bucket",
            () -> new BucketItem(ModFluids.SOURCE_RED_WINE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> WHITE_WINE_BUCKET = ITEMS.register("white_wine_bucket",
            () -> new BucketItem(ModFluids.SOURCE_WHITE_WINE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> ROSE_WINE_BUCKET = ITEMS.register("rose_wine_bucket",
            () -> new BucketItem(ModFluids.SOURCE_ROSE_WINE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> OVERLOADED_COPPER = ITEMS.register("overloaded_copper",
            () -> new FuelItem(new Item.Properties(), 400));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
