package net.galbatronus.floricultura.screen;

import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.block.entity.BerryPressBlockEntity;
import net.galbatronus.floricultura.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import static net.galbatronus.floricultura.screen.FermentationBarrelMenu.VANILLA_FIRST_SLOT_INDEX;

public class BerryPressMenu extends AbstractContainerMenu {

    public final BerryPressBlockEntity blockEntity;
    private final Level level;
    final ContainerData data;
    private FluidStack fluidStack;

    // Constantes de Slots de la Máquina (Para Claridad)
    private static final int INPUT_FUEL_SLOT = 0;
    private static final int INPUT_INGREDIENT_SLOT = 1;
    private static final int INPUT_CONTAINER_SLOT = 2;
    // Slot 3 es el tanque de fluido, no un slot de ItemHandler visible
    private static final int OUTPUT_SLOT = 4;

    private static final int MACHINE_SLOT_COUNT = 5; // Total de slots en el ItemHandler

    public BerryPressMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {

        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public BerryPressBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public FluidStack getFluidStack() {
        // blockEntity es la referencia al BlockEntity que se pasa al constructor del menú
        return this.blockEntity.getFluidStack();
    }


    public void setFluid(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }


    // Slot 0: OVERLOADED_COPPER (Combustible)
    public class FuelSlot extends SlotItemHandler {
        public FuelSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            // Asume que ModItems.OVERLOADED_COPPER.get() es el cobre de energía
            return stack.is(ModItems.OVERLOADED_COPPER.get());
        }


    }

    // Slot 1: Bayas (Ingrediente 1)
    public class IngredientSlot extends SlotItemHandler {
        public IngredientSlot(IItemHandler itemHandler, int index, int x, int y) { super(itemHandler, index, x, y); }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return true;
        }
    }

    // Slot 2: Contenedor (glass_bottle)
    public class ContainerSlot extends SlotItemHandler {
        public ContainerSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.is(Items.GLASS_BOTTLE);
        }

    }

    // Slot 4: Salida (Item Crafteado / Contenedor de Retorno)
    public class OutputOnlySlot extends SlotItemHandler {
        public OutputOnlySlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false; // Nunca se puede colocar manualmente
        }
    }


    public BerryPressMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.BERRY_PRESS_MENU.get(), pContainerId);
        // La comprobación debería ser contra el tamaño del ItemHandler de la BE.
        // checkContainerSize(entity, MACHINE_SLOT_COUNT);
        blockEntity = (BerryPressBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;
        this.fluidStack = blockEntity.getFluidStack();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            // Slot 0: Combustible (OVERLOADED_COPPER)
            this.addSlot(new FuelSlot(iItemHandler, INPUT_FUEL_SLOT, 17, 17));
            // Slot 1: Ingrediente (IngredientSlot)
            this.addSlot(new IngredientSlot(iItemHandler, INPUT_INGREDIENT_SLOT, 79, 17));
            // Slot 2: Contenedor (GLASS_BOTTLE)
            this.addSlot(new ContainerSlot(iItemHandler, INPUT_CONTAINER_SLOT, 143, 17));
            // Slot 4: Salida (Item Crafteado)
            this.addSlot(new OutputOnlySlot(iItemHandler, OUTPUT_SLOT, 102, 51));
        });

        addDataSlots(data);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 5;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Lógica para mover desde el jugador HACIA la máquina
        if (index >= VANILLA_FIRST_SLOT_INDEX && index < VANILLA_SLOT_COUNT) {
            // Prioridad 1: Mover combustible al slot de combustible
            if (sourceStack.is(ModItems.OVERLOADED_COPPER.get())) {
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + INPUT_FUEL_SLOT, TE_INVENTORY_FIRST_SLOT_INDEX + INPUT_FUEL_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
                // Prioridad 2: Mover botellas al slot de contenedor
            } else if (sourceStack.is(Items.GLASS_BOTTLE)) {
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + INPUT_CONTAINER_SLOT, TE_INVENTORY_FIRST_SLOT_INDEX + INPUT_CONTAINER_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
                // ✅ SOLUCIÓN: Prioridad 3 (Por defecto) - Mover cualquier otro ítem al slot de ingrediente
            } else {
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + INPUT_INGREDIENT_SLOT, TE_INVENTORY_FIRST_SLOT_INDEX + INPUT_INGREDIENT_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Lógica para mover desde la máquina HACIA el jugador
        } else if (index >= TE_INVENTORY_FIRST_SLOT_INDEX && index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        if (sourceStack.getCount() == copyOfSourceStack.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 26;
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.BERRY_PRESS.get());
    }

    // Métodos para añadir el inventario del jugador (mantener al final)
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
