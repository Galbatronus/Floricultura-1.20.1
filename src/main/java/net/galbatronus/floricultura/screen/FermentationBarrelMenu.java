package net.galbatronus.floricultura.screen;

import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.block.entity.FermentationBarrelBlockEntity;
import net.galbatronus.floricultura.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import net.galbatronus.floricultura.util.ModTags;

public class FermentationBarrelMenu extends AbstractContainerMenu {
    public final FermentationBarrelBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public FermentationBarrelMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(8));
    }

    public class GlassBottleSlot extends SlotItemHandler {
        public GlassBottleSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.is(ModItems.WINE_BOTTLE.get());
        }
    }

    public class OutputSlot extends SlotItemHandler {
        public OutputSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }

    public class FermentableJuiceSlot extends SlotItemHandler {

        public FermentableJuiceSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }


        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.is(ModTags.Items.FERMENTABLE_JUICES);
        }
    }

    public class WineIngredientsSlot extends SlotItemHandler {

        public WineIngredientsSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.is(ModTags.Items.WINE_INGREDIENTS);
        }
    }

    public FermentationBarrelMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.FERMENTATION_BARREL_MENU.get(), pContainerId);
        checkContainerSize(inv, 8);
        blockEntity = ((FermentationBarrelBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);



        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new GlassBottleSlot(iItemHandler, 0, 126, 13));
            this.addSlot(new OutputSlot(iItemHandler, 1, 103, 47));
            this.addSlot(new OutputSlot(iItemHandler, 2, 149, 47));
            this.addSlot(new OutputSlot(iItemHandler, 3, 126, 54));
            this.addSlot(new FermentableJuiceSlot(iItemHandler, 4, 17, 13));
            this.addSlot(new WineIngredientsSlot(iItemHandler, 5, 75, 13));
            this.addSlot(new WineIngredientsSlot(iItemHandler, 6, 75, 31));
            this.addSlot(new WineIngredientsSlot(iItemHandler, 7, 75, 49));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 26; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 8;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Jugador → máquina
        if (index < TE_INVENTORY_FIRST_SLOT_INDEX) {
            if (sourceStack.is(ModTags.Items.FERMENTABLE_JUICES)) {
                // INPUT_SLOT2 (índice 4)
                if (!this.moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 4, TE_INVENTORY_FIRST_SLOT_INDEX + 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (sourceStack.getItem() == ModItems.WINE_BOTTLE.get()) {
                // INPUT_SLOT (índice 0)
                if (!this.moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 0, TE_INVENTORY_FIRST_SLOT_INDEX + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }

        } else {
            // Máquina → jugador
            if (!this.moveItemStackTo(sourceStack, 0, TE_INVENTORY_FIRST_SLOT_INDEX, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.FERMENTATION_BARREL.get());
    }

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
