package net.galbatronus.floricultura.util;

import net.galbatronus.floricultura.floricultura;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> FLORES = tag("flores");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(floricultura.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> FERMENTABLE_JUICES = tag("fermentable_juices");
        public static final TagKey<Item> WINE_INGREDIENTS = tag("wine_ingredients");
        public static final TagKey<Item> PRESSABLE_BERRIES = tag("pressable_berries");


        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(floricultura.MOD_ID, name));
        }
    }
}