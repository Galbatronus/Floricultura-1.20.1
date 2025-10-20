package net.galbatronus.floricultura.fluid;

import net.galbatronus.floricultura.floricultura;
import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, floricultura.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_PURPLE_GRAPE_JUICE = FLUIDS.register("purple_grape_juice_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PURPLE_GRAPE_JUICE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PURPLE_GRAPE_JUICE = FLUIDS.register("flowing_purple_grape_juice",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PURPLE_GRAPE_JUICE_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties PURPLE_GRAPE_JUICE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PURPLE_GRAPE_JUICE_FLUID_TYPE, SOURCE_PURPLE_GRAPE_JUICE, FLOWING_PURPLE_GRAPE_JUICE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.PURPLE_GRAPE_JUICE_BLOCK)
            .bucket(ModItems.PURPLE_GRAPE_JUICE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_WHITE_GRAPE_JUICE = FLUIDS.register("white_grape_juice_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.WHITE_GRAPE_JUICE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_WHITE_GRAPE_JUICE = FLUIDS.register("flowing_white_grape_juice",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.WHITE_GRAPE_JUICE_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties WHITE_GRAPE_JUICE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.WHITE_GRAPE_JUICE_FLUID_TYPE, SOURCE_WHITE_GRAPE_JUICE, FLOWING_WHITE_GRAPE_JUICE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.WHITE_GRAPE_JUICE_BLOCK)
            .bucket(ModItems.WHITE_GRAPE_JUICE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_PINK_GRAPE_JUICE = FLUIDS.register("pink_grape_juice_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PINK_GRAPE_JUICE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PINK_GRAPE_JUICE = FLUIDS.register("flowing_pink_grape_juice",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PINK_GRAPE_JUICE_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties PINK_GRAPE_JUICE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PINK_GRAPE_JUICE_FLUID_TYPE, SOURCE_PINK_GRAPE_JUICE, FLOWING_PINK_GRAPE_JUICE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.PINK_GRAPE_JUICE_BLOCK)
            .bucket(ModItems.PINK_GRAPE_JUICE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_RED_WINE = FLUIDS.register("red_wine_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.RED_WINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RED_WINE = FLUIDS.register("flowing_red_wine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.RED_WINE_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties RED_WINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.RED_WINE_FLUID_TYPE, SOURCE_RED_WINE, FLOWING_RED_WINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.RED_WINE_BLOCK)
            .bucket(ModItems.RED_WINE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_WHITE_WINE = FLUIDS.register("white_wine_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.WHITE_WINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_WHITE_WINE = FLUIDS.register("flowing_white_wine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.WHITE_WINE_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties WHITE_WINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.WHITE_WINE_FLUID_TYPE, SOURCE_WHITE_WINE, FLOWING_WHITE_WINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.WHITE_WINE_BLOCK)
            .bucket(ModItems.WHITE_WINE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_ROSE_WINE = FLUIDS.register("rose_wine_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ROSE_WINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ROSE_WINE = FLUIDS.register("flowing_rose_wine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ROSE_WINE_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties ROSE_WINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.ROSE_WINE_FLUID_TYPE, SOURCE_ROSE_WINE, FLOWING_ROSE_WINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.ROSE_WINE_BLOCK)
            .bucket(ModItems.ROSE_WINE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}