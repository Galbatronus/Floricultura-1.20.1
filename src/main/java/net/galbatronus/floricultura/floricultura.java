package net.galbatronus.floricultura;

import com.mojang.logging.LogUtils;
import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.block.entity.ModBlockEntities;
import net.galbatronus.floricultura.entity.ModEntities;
import net.galbatronus.floricultura.entity.client.SamoyedoRenderer;
import net.galbatronus.floricultura.fluid.ModFluidTypes;
import net.galbatronus.floricultura.fluid.ModFluids;
import net.galbatronus.floricultura.item.ModCreativeModTabs;
import net.galbatronus.floricultura.item.ModItems;
import net.galbatronus.floricultura.recipe.ModRecipes;
import net.galbatronus.floricultura.screen.BerryPressScreen;
import net.galbatronus.floricultura.screen.FermentationBarrelScreen;
import net.galbatronus.floricultura.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.galbatronus.floricultura.screen.BerryPressMenu;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.inventory.MenuType;
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

@Mod(floricultura.MOD_ID)
public class floricultura
{
    public static final String MOD_ID = "floricultura";

    private static final Logger LOGGER = LogUtils.getLogger();

    // Constructor del mod
    public floricultura()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registro de pestañas creativas personalizadas
        ModCreativeModTabs.register(modEventBus);

        // Registro de ítems, bloques, entidades y block entities personalizados
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        // Añade listener para configurar lógica común
        modEventBus.addListener(this::commonSetup);

        // Registro de tipos de menú/pantalla
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        // Registra eventos generales del mod en el bus global de Forge
        MinecraftForge.EVENT_BUS.register(this);

        // Listener para agregar contenido a pestañas creativas
        modEventBus.addListener(this::addCreative);


    }

    // Lógica común al cargar el mod
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            // Añade tus plantas personalizadas al FlowerPot vanilla
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.KANORA_BLOCK.getId(), ModBlocks.POTTED_KANORA);
        });

      event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CAMPANILLAS_CHINAS_BLOCK.getId(), ModBlocks.POTTED_CAMPANILLAS_CHINAS);
      });

      event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.DALIA_ROJA_BLOCK.getId(), ModBlocks.POTTED_DALIA_ROJA);
      });

      event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.FRESIA_AMARILLA_BLOCK.getId(), ModBlocks.POTTED_FRESIA_AMARILLA);
      });

    }

    // Evento llamado al construir contenido para las pestañas creativas personalizadas
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
        public static void onClientSetup(FMLClientSetupEvent event) {

            // Registrar renderizador para entidad Samoyedo
            EntityRenderers.register(ModEntities.SAMOYEDO.get(), SamoyedoRenderer::new);

            // Registrar pantalla GUI para el Fermentation_Barrel
            MenuScreens.register(ModMenuTypes.FERMENTATION_BARREL_MENU.get(), FermentationBarrelScreen::new);

            MenuScreens.register(ModMenuTypes.BERRY_PRESS_MENU.get(), BerryPressScreen::new);
        }

    }

    // Clase interna adicional para ejecutar tareas comunes (servidor o cliente) durante el setup
    @Mod.EventBusSubscriber(modid = floricultura.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {

            event.enqueueWork(() -> {

            });
        }


    }
}