package net.galbatronus.floricultura.fluid;

import net.galbatronus.floricultura.floricultura;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

import java.util.Map;


public class ModFluidTypes {

    public static final ResourceLocation JUICE_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation JUICE_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation JUICE_OVERLAY_RL = new ResourceLocation(floricultura.MOD_ID, "fluid/juice_overlay");
    private static final Map<String, Integer> NAME_TO_TINT = Map.of(
            "purple_grape_juice_type", 0xE136FF,
            "white_grape_juice_type",  0xDDFF36,
            "pink_grape_juice_type",   0xFC5BCF,
            "red_wine_type", 0xB632B8,
            "white_wine_type",  0xEBFF9E,
            "rose_wine_type",   0xF783DC
    );

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, floricultura.MOD_ID);

    public static final RegistryObject<FluidType> PURPLE_GRAPE_JUICE_FLUID_TYPE = register("purple_grape_juice_type",
            FluidType.Properties.create().lightLevel(0).density(10).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    public static final RegistryObject<FluidType> WHITE_GRAPE_JUICE_FLUID_TYPE = register("white_grape_juice_type",
            FluidType.Properties.create().lightLevel(0).density(10).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    public static final RegistryObject<FluidType> PINK_GRAPE_JUICE_FLUID_TYPE = register("pink_grape_juice_type",
            FluidType.Properties.create().lightLevel(0).density(10).viscosity(5) .sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    public static final RegistryObject<FluidType> RED_WINE_FLUID_TYPE = register("red_wine_type",
            FluidType.Properties.create().lightLevel(0).density(11).viscosity(6).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    public static final RegistryObject<FluidType> WHITE_WINE_FLUID_TYPE = register("white_wine_type",
            FluidType.Properties.create().lightLevel(0).density(11).viscosity(6).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    public static final RegistryObject<FluidType> ROSE_WINE_FLUID_TYPE = register("rose_wine_type",
            FluidType.Properties.create().lightLevel(0).density(11).viscosity(6) .sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));


    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        int tint = NAME_TO_TINT.getOrDefault(name, 0xFFFFFFFF);
        return FLUID_TYPES.register(name, () -> new BaseFluidType(
                JUICE_STILL_RL, JUICE_FLOWING_RL, JUICE_OVERLAY_RL,
                tint, new Vector3f(224f / 255f, 56f / 255f, 208f / 255f), properties));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}