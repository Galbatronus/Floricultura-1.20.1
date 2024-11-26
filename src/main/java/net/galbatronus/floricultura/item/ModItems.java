package net.galbatronus.floricultura.item;

import net.galbatronus.floricultura.floricultura;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.galbatronus.floricultura.block.ModBlocks.CAMPANILLAS_CHINAS_BLOCK;
import static net.galbatronus.floricultura.block.ModBlocks.KANORA_BLOCK;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, floricultura.MOD_ID);


    public static final RegistryObject<Item> KANORA = ITEMS.register("kanora",
            () -> new BlockItem(KANORA_BLOCK.get(), new Item.Properties()));


    public static final RegistryObject<Item> CAMPANILLAS_CHINAS = ITEMS.register("campanillas_chinas",
            () -> new BlockItem(CAMPANILLAS_CHINAS_BLOCK.get(), new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
