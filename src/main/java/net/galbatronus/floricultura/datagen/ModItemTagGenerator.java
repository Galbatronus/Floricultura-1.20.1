package net.galbatronus.floricultura.datagen;

import net.galbatronus.floricultura.floricultura;
import net.galbatronus.floricultura.item.ModItems;
import net.galbatronus.floricultura.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {

    public ModItemTagGenerator(PackOutput packOutput,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockTags, floricultura.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Items.FERMENTABLE_JUICES)
                .add(ModItems.PURPLE_GRAPES_BOTTLE.get())
                .add(ModItems.WHITE_GRAPES_BOTTLE.get())
                .add(ModItems.ROSE_GRAPES_BOTTLE.get());

        this.tag(ModTags.Items.WINE_INGREDIENTS)
                .add(Items.GLOW_BERRIES)
                .add(Items.SWEET_BERRIES)
                .add(Items.SUGAR);

        this.tag(ModTags.Items.PRESSABLE_BERRIES)
                .add(ModItems.PURPLE_GRAPES.get())
                .add(ModItems.WHITE_GRAPES.get())
                .add(ModItems.PINK_GRAPES.get());
    }
}
