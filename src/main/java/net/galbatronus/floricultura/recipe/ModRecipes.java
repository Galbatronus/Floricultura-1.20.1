package net.galbatronus.floricultura.recipe;

import net.galbatronus.floricultura.floricultura;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, floricultura.MOD_ID);


    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, floricultura.MOD_ID);

    public static final RegistryObject<RecipeType<FermentationBarrelRecipe>> FERMENTATION_TYPE =
            RECIPE_TYPES.register("fermentation", () -> FermentationBarrelRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<BerryPressRecipe>> PRESSED_TYPE =
            RECIPE_TYPES.register("pressed", () -> BerryPressRecipe.Type.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}