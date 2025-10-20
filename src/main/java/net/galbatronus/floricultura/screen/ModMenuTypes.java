package net.galbatronus.floricultura.screen;

import net.galbatronus.floricultura.floricultura;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, floricultura.MOD_ID);

    public static final RegistryObject<MenuType<FermentationBarrelMenu>> FERMENTATION_BARREL_MENU =
            registerMenuType("fermentation_barrel_menu", FermentationBarrelMenu::new);

    public static final RegistryObject<MenuType<BerryPressMenu>> BERRY_PRESS_MENU =
            registerMenuType("berry_press_menu", BerryPressMenu::new);


    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}