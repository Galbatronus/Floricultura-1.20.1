package net.galbatronus.floricultura.block.entity;

import net.galbatronus.floricultura.recipe.FermentationBarrelRecipe;
import net.galbatronus.floricultura.recipe.ModRecipes;
import net.galbatronus.floricultura.screen.FermentationBarrelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FermentationBarrelBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            cachedRecipe = Optional.empty(); // Se invalida la caché cuando el inventario cambia
            setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            // El límite de slot por el jugo es 1 (botella)
            if (slot == INPUT_SLOT2) {
                return 1;
            }
            return super.getSlotLimit(slot);
        }
    };

    private final int INPUT_SLOT = 0;
    private final int INPUT_SLOT2 = 4;
    private final int OUTPUT_SLOT = 1;
    private final int OUTPUT_SLOT2 = 2;
    private final int OUTPUT_SLOT3 = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 60;

    // La caché de la receta
    private Optional<FermentationBarrelRecipe> cachedRecipe = Optional.empty();

    public FermentationBarrelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.WINE_FERMENTATION.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex){
                    case 0 -> FermentationBarrelBlockEntity.this.progress;
                    case 1 -> FermentationBarrelBlockEntity.this.maxProgress;
                    default -> 0;

                };

            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> FermentationBarrelBlockEntity.this.progress = pValue;
                    case 1 -> FermentationBarrelBlockEntity.this.maxProgress = pValue;
                }

            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.floricultura.fermentation_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FermentationBarrelMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory",itemHandler.serializeNBT());
        pTag.putInt("fermentation_barrel.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("fermentation_barrel.progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        // Usa la receta que ya encontramos y está en la caché
        cachedRecipe.ifPresent(recipe -> {
            ItemStack result = recipe.getResultItem(this.level.registryAccess());

            // Consume los ingredientes
            this.itemHandler.extractItem(INPUT_SLOT, 1, false);
            this.itemHandler.extractItem(INPUT_SLOT2, 1, false);

            // Devuelve la botella vacía
            this.itemHandler.setStackInSlot(INPUT_SLOT2, new ItemStack(Items.GLASS_BOTTLE));

            // Busca un slot de salida disponible
            int targetSlot = -1;
            for (int slot : new int[]{OUTPUT_SLOT, OUTPUT_SLOT2, OUTPUT_SLOT3}) {
                ItemStack current = this.itemHandler.getStackInSlot(slot);
                if (current.isEmpty()) {
                    targetSlot = slot;
                    break;
                }
                if (current.is(result.getItem()) && current.getCount() + result.getCount() <= current.getMaxStackSize()) {
                    targetSlot = slot;
                    break;
                }
            }

            if (targetSlot == -1) {
                // No debería pasar si hasRecipe() funcionó bien, pero es una buena salvaguarda
                return;
            }

            // Inserta el resultado en el slot encontrado
            ItemStack currentInOutput = this.itemHandler.getStackInSlot(targetSlot);
            if (currentInOutput.isEmpty()) {
                this.itemHandler.setStackInSlot(targetSlot, result.copy());
            } else {
                currentInOutput.grow(result.getCount());
            }

            // No es estrictamente necesario invalidar la caché aquí, porque onContentsChanged ya lo hará,
            // pero resetear el progreso es importante
            resetProgress();
        });
    }


    private boolean hasRecipe() {
        // Si la caché está vacía, intenta encontrar una nueva receta
        if (this.cachedRecipe.isEmpty()) {
            SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, itemHandler.getStackInSlot(i));
            }
            // Usa el RecipeType REGISTRADO, no la instancia directa
            this.cachedRecipe = this.level.getRecipeManager()
                    .getRecipeFor(ModRecipes.FERMENTATION_TYPE.get(), inventory, this.level);
        }

        // Si después de buscar, la caché sigue vacía, entonces no hay receta
        if (this.cachedRecipe.isEmpty()) {
            return false;
        }

        // Si encontró una receta, ahora comprueba si hay espacio en la salida
        ItemStack result = this.cachedRecipe.get().getResultItem(this.level.registryAccess());
        return canInsertAmountIntoOutputSlot(result.getCount()) &&
                canInsertItemIntoOutputSlot(result.getItem());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        for (int slot : new int[]{OUTPUT_SLOT, OUTPUT_SLOT2, OUTPUT_SLOT3}) {
            ItemStack stack = this.itemHandler.getStackInSlot(slot);
            if (stack.isEmpty() || stack.is(item)) {
                return true;
            }
        }
        return false;
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        for (int slot : new int[]{OUTPUT_SLOT, OUTPUT_SLOT2, OUTPUT_SLOT3}) {
            ItemStack stack = this.itemHandler.getStackInSlot(slot);
            if (stack.isEmpty() || (stack.getCount() + count <= stack.getMaxStackSize())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }
}