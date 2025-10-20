package net.galbatronus.floricultura.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties PURPLE_GRAPES = new FoodProperties.Builder().nutrition(1)
            .saturationMod(0.3f).build();

    public static final FoodProperties WHITE_GRAPES = new FoodProperties.Builder().nutrition(1)
            .saturationMod(0.3f).build();

    public static final FoodProperties PINK_GRAPES = new FoodProperties.Builder().nutrition(1)
            .saturationMod(0.3f).build();

    public static final FoodProperties PURPLE_GRAPES_BOTTLE = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.5f).build();

    public static final FoodProperties WHITE_GRAPES_BOTTLE = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.5f).build();

    public static final FoodProperties ROSE_GRAPES_BOTTLE = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.5f).build();

    public static final FoodProperties RED_WINE = new FoodProperties.Builder().nutrition(6)
            .saturationMod(1.6f).build();

    public static final FoodProperties WHITE_WINE = new FoodProperties.Builder().nutrition(6)
            .saturationMod(1.6f).build();

    public static final FoodProperties ROSE_WINE = new FoodProperties.Builder().nutrition(6)
            .saturationMod(1.6f).build();
}
