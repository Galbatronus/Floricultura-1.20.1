package net.galbatronus.floricultura;

import com.mojang.logging.LogUtils;
import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.item.ModCreativeModTabs;
import net.galbatronus.floricultura.item.ModItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(floricultura.MOD_ID)
public class floricultura
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "floricultura";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public floricultura()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.regiter(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
          ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.KANORA_BLOCK.getId(), ModBlocks.POTTED_KANORA);
      });

        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CAMPANILLAS_CHINAS_BLOCK.getId(), ModBlocks.POTTED_CAMPANILLAS_CHINAS);
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }

    @Mod.EventBusSubscriber(modid = floricultura.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {

            event.enqueueWork(() -> {

            });
        }
    }
}



