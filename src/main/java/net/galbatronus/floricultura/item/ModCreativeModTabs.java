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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.KANORA.get()))
                    .title(Component.translatable("creativetab.floricultura_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.KANORA.get());
                        pOutput.accept(ModItems.CAMPANILLAS_CHINAS.get());

                        pOutput.accept(ModBlocks.CAMPANORA_LEAVE.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
