package net.galbatronus.floricultura.item;

import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.floricultura;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, floricultura.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FLORICULTURA_TAB = CREATIVE_MODE_TABS.register("floricultura_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.KANORA_BLOCK.get()))
                    .title(Component.translatable("creativetab.floricultura_tab"))
                    .displayItems((pParameters, pOutput) -> {


                        
                        pOutput.accept(ModBlocks.KANORA_BLOCK.get());
                        pOutput.accept(ModBlocks.CAMPANILLAS_CHINAS_BLOCK.get());
                        pOutput.accept(ModBlocks.DALIA_AMARILLA_BLOCK.get());
                        pOutput.accept(ModBlocks.DALIA_BLANCA_BLOCK.get());
                        pOutput.accept(ModBlocks.DALIA_MORADA_BLOCK.get());
                        pOutput.accept(ModBlocks.DALIA_NARANJA_BLOCK.get());
                        pOutput.accept(ModBlocks.DALIA_NEGRA_BLOCK.get());
                        pOutput.accept(ModBlocks.DALIA_ROJA_BLOCK.get());
                        pOutput.accept(ModBlocks.DALIA_ROSADA_BLOCK.get());
                        pOutput.accept(ModBlocks.FRESIA_AMARILLA_BLOCK.get());

                        pOutput.accept(ModBlocks.CAMPANORA_LEAVE.get());
                        pOutput.accept(ModBlocks.STAKE.get());

                        pOutput.accept(ModItems.SAMOYEDO_SPAWN_EGG.get());

                        pOutput.accept(ModItems.PURPLE_GRAPES_SEEDS.get());
                        pOutput.accept(ModItems.WHITE_GRAPES_SEEDS.get());
                        pOutput.accept(ModItems.PINK_GRAPES_SEEDS.get());
                        pOutput.accept(ModItems.PURPLE_GRAPES.get());
                        pOutput.accept(ModItems.WHITE_GRAPES.get());
                        pOutput.accept(ModItems.PINK_GRAPES.get());

                        pOutput.accept(ModItems.WINE_BOTTLE.get());

                        pOutput.accept(ModItems.PURPLE_GRAPES_BOTTLE.get());
                        pOutput.accept(ModItems.WHITE_GRAPES_BOTTLE.get());
                        pOutput.accept(ModItems.ROSE_GRAPES_BOTTLE.get());

                        pOutput.accept(ModItems.RED_WINE.get());
                        pOutput.accept(ModItems.WHITE_WINE.get());
                        pOutput.accept(ModItems.ROSE_WINE.get());

                        pOutput.accept(ModBlocks.WOODEN_VESSEL.get());
                        pOutput.accept(ModBlocks.FERMENTATION_BARREL.get());
                        pOutput.accept(ModBlocks.BERRY_PRESS.get());

                        pOutput.accept(ModItems.OVERLOADED_COPPER.get());


                        pOutput.accept(ModItems.PURPLE_GRAPE_JUICE_BUCKET.get());
                        pOutput.accept(ModItems.WHITE_GRAPE_JUICE_BUCKET.get());
                        pOutput.accept(ModItems.PINK_GRAPE_JUICE_BUCKET.get());
                        pOutput.accept(ModItems.RED_WINE_BUCKET.get());
                        pOutput.accept(ModItems.WHITE_WINE_BUCKET.get());
                        pOutput.accept(ModItems.ROSE_WINE_BUCKET.get());


                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
