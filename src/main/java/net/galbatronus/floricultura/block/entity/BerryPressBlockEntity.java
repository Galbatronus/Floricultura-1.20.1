package net.galbatronus.floricultura.block.entity;

import net.galbatronus.floricultura.item.ModItems;
import net.galbatronus.floricultura.networking.ModMessages;
import net.galbatronus.floricultura.networking.packet.FluidSyncS2CPacket;
import net.galbatronus.floricultura.recipe.BerryPressRecipe;
import net.galbatronus.floricultura.recipe.ModRecipes;
import net.galbatronus.floricultura.screen.BerryPressMenu;
import net.galbatronus.floricultura.util.ModEnergyStorage;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BerryPressBlockEntity extends BlockEntity implements MenuProvider {

    // --- CONSTANTES DE SLOTS ---
    private static final int INPUT_SLOT = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int INPUT_SLOT3 = 2;
    private static final int OUTPUT_SLOT = 3;
    private static final int OUTPUT_SLOT2 = 4;

    // --- HANDLERS Y CAPACIDADES ---
    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {


        @Override
        protected void onContentsChanged(int slot) {
            setChanged();


        }


        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case INPUT_SLOT -> stack.is(ModItems.OVERLOADED_COPPER.get());

                case INPUT_SLOT2 -> true;

                case INPUT_SLOT3 -> stack.is(Items.GLASS_BOTTLE);

                case OUTPUT_SLOT, OUTPUT_SLOT2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            // Lógica de Límite para el Slot de Combustible (INPUT_SLOT = 0)
            if (slot == INPUT_SLOT && stack.is(ModItems.OVERLOADED_COPPER.get())) {

                // Energía que la máquina recibirá si se inserta el stack completo:
                int incomingEnergy = stack.getCount() * BerryPressBlockEntity.this.fuelEnergyValue;

                // Máxima energía que la máquina puede recibir:
                int energyNeeded = ENERGY_STORAGE.getMaxEnergyStored() - ENERGY_STORAGE.getEnergyStored();

                // Cantidad de cobre que se necesita para llenar la máquina:
                int itemsNeeded = (int) Math.ceil((double) energyNeeded / BerryPressBlockEntity.this.fuelEnergyValue);

                // Si no se necesita energía, no se acepta el stack.
                if (itemsNeeded <= 0) {
                    return stack; // Devuelve todo el stack, no se inserta nada
                }

                // Limitar la cantidad de ítems a la cantidad que se necesita o al tamaño máximo del slot.
                int itemsToInsert = Math.min(stack.getCount(), itemsNeeded);
                itemsToInsert = Math.min(itemsToInsert, getSlotLimit(slot));

                if (itemsToInsert < stack.getCount()) {
                    // Si la cantidad a insertar es menor que la cantidad del stack (por límite de energía)
                    ItemStack remainder = stack.copy();
                    remainder.shrink(itemsToInsert);

                    // Intenta insertar solo la cantidad limitada
                    super.insertItem(slot, stack.copyWithCount(itemsToInsert), simulate);
                    return remainder;
                }
            }

            // Para todos los demás slots o si el límite de energía no aplica:
            return super.insertItem(slot, stack, simulate);
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(60000, 500) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }


    };
    private final FluidTank FLUID_TANK = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged(); // Marca que el bloque ha cambiado (para guardado y sync)

            // **Asegúrate que estas clases (ModMessages y FluidSyncS2CPacket) están importadas.**
            if (level != null && !level.isClientSide()) {
                ModMessages.sendToClients(
                        new FluidSyncS2CPacket(this.getFluid(), worldPosition) // <-- Corregido: usa this.getFluid()
                );
            }
        }
    };


    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    // --- DATOS DE PROGRESO Y RECETA ---
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;
    private int fuelEnergyValue = 1000; // Energía que da un OVERLOADED_COPPER
    private Optional<BerryPressRecipe> cachedRecipe = Optional.empty();


    public BerryPressBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PRESS_BERRIES.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> BerryPressBlockEntity.this.progress;
                    case 1 -> BerryPressBlockEntity.this.maxProgress;
                    case 2 -> BerryPressBlockEntity.this.ENERGY_STORAGE.getEnergyStored();
                    case 3 -> BerryPressBlockEntity.this.ENERGY_STORAGE.getMaxEnergyStored();
                    default -> 0;
                };
            }



            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> BerryPressBlockEntity.this.progress = pValue;
                    case 1 -> BerryPressBlockEntity.this.maxProgress = pValue;
                    case 2 -> BerryPressBlockEntity.this.ENERGY_STORAGE.setEnergy(pValue);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

    }

    // --- LÓGICA DE TICK ---
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide()) return;

        // 1. Generar energía (sin cambios)
        if (ENERGY_STORAGE.getEnergyStored() < ENERGY_STORAGE.getMaxEnergyStored()) {
            ItemStack fuel = itemHandler.getStackInSlot(INPUT_SLOT);
            if (fuel.is(ModItems.OVERLOADED_COPPER.get())) {
                itemHandler.extractItem(INPUT_SLOT, 1, false);
                ENERGY_STORAGE.receiveEnergy(fuelEnergyValue, false);
                setChanged();
            }
        }

        // 2. Procesar receta
        Optional<BerryPressRecipe> recipeOpt = getCurrentRecipe();
        if (recipeOpt.isPresent()) {
            BerryPressRecipe recipe = recipeOpt.get();
            int processingTime = recipe.getProcessingTime();

            // Comprobar si el tiempo es válido
            if (processingTime <= 0) {
                resetProgress(); // Resetea si la receta es inválida
                return;
            }

            this.maxProgress = processingTime; // Actualiza maxProgress para la GUI

            // Calcular energía necesaria para este tick de forma segura
            int totalEnergyNeeded = recipe.getEnergyRequired();
            int energyRequiredThisTick = 0;
            if (totalEnergyNeeded > 0) {
                // Calcula cuánto debería haberse consumido hasta el tick anterior
                int energyShouldHaveConsumedBefore = (this.progress == 0) ? 0 : (int) (((float)(this.progress) / this.maxProgress) * totalEnergyNeeded);
                // Calcula cuánto debería haberse consumido hasta este tick
                int energyShouldHaveConsumedNow = (int) (((float)(this.progress + 1) / this.maxProgress) * totalEnergyNeeded);
                // La diferencia es lo que se necesita este tick
                energyRequiredThisTick = energyShouldHaveConsumedNow - energyShouldHaveConsumedBefore;
                // Asegurar que al menos consuma 1 si no es el último tick y se necesita energía
                if (energyRequiredThisTick <= 0 && this.progress < this.maxProgress -1) energyRequiredThisTick = 1;
                // Asegurar que en el último tick consuma exactamente lo que falta
                if (this.progress == this.maxProgress -1) energyRequiredThisTick = totalEnergyNeeded - energyShouldHaveConsumedBefore;
            }


            // Comprobar si se puede procesar (energía Y condiciones de crafteo)
            System.out.println("Tick - Energy Check: Stored=" + this.ENERGY_STORAGE.getEnergyStored() + ", Needed=" + energyRequiredThisTick + ", CanCraft=" + canCraft(recipe));
            if (this.ENERGY_STORAGE.getEnergyStored() >= energyRequiredThisTick && canCraft(recipe)) {
                // Consumir energía si es necesario
                if (energyRequiredThisTick > 0) {
                    this.ENERGY_STORAGE.extractEnergy(energyRequiredThisTick, false);
                }
                // Avanzar progreso
                this.progress++;
                setChanged();

                // Si el progreso terminó, craftear
                if (this.progress >= this.maxProgress) {
                    craftItem(recipe);
                    resetProgress();
                }
            } else {
                // Opcional: Pausar en lugar de resetear si solo falta energía temporalmente?
                // Si no se puede craftear (ej. output lleno), sí conviene resetear.
                if (!canCraft(recipe) && this.progress > 0) {
                    resetProgress();
                }
            }

        } else {
            // No hay receta válida para los inputs actuales
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
        this.maxProgress = 72;
        setChanged();
    }

    private boolean canCraft(BerryPressRecipe recipe) {
        // 1. Verificar cantidad de ingredientes
        Ingredient mainIngredient = recipe.getIngredients().get(0);
        ItemStack inputSlotStack = itemHandler.getStackInSlot(INPUT_SLOT2);

        boolean hasEnoughIngredients = mainIngredient.test(inputSlotStack) && inputSlotStack.getCount() >= recipe.getIngredientCount();

        // 2. Verificar espacio en el tanque de fluido
        FluidStack outputFluid = recipe.getOutputFluid();
        // Comprueba si la cantidad COMPLETA cabe
        boolean hasSpaceInTank = FLUID_TANK.fill(outputFluid, IFluidHandler.FluidAction.SIMULATE) == outputFluid.getAmount();

        // 3. Verificar si el slot de salida para el contenedor está libre o puede apilar

        ItemStack containerOutput = recipe.getContainerOutput();
        ItemStack outputSlotStack = itemHandler.getStackInSlot(OUTPUT_SLOT2);
        boolean outputSlotCanAccept;
        if (outputSlotStack.isEmpty()) { outputSlotCanAccept = true; }
        else {
            outputSlotCanAccept = ItemStack.isSameItemSameTags(outputSlotStack, containerOutput) &&
                    outputSlotStack.getCount() + containerOutput.getCount() <= itemHandler.getSlotLimit(OUTPUT_SLOT2);
        }
        boolean chanceOutputCanFit = true;
        BerryPressRecipe.ChanceOutput chanceOutput = recipe.getChanceOutput();
        if(chanceOutput != null && !outputSlotStack.isEmpty()) {
            chanceOutputCanFit = ItemStack.isSameItemSameTags(outputSlotStack, chanceOutput.stack()) &&
                    outputSlotStack.getCount() + chanceOutput.stack().getCount() <= itemHandler.getSlotLimit(OUTPUT_SLOT2);
        }

        System.out.println("canCraft Result: Ingredients=" + hasEnoughIngredients /* ... otras condiciones ... */ );
        return hasEnoughIngredients && hasSpaceInTank && outputSlotCanAccept && chanceOutputCanFit;

    }

    private void craftItem(BerryPressRecipe recipe) {

        itemHandler.extractItem(INPUT_SLOT2, recipe.getIngredientCount(), false);
        itemHandler.extractItem(INPUT_SLOT3, 1, false);

        // 2. Llenar el tanque de fluido
        FLUID_TANK.fill(recipe.getOutputFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
        ItemStack containerOutput = recipe.getContainerOutput().copy();
        ItemStack existingOutput = itemHandler.getStackInSlot(OUTPUT_SLOT2);
        if (existingOutput.isEmpty()) { itemHandler.setStackInSlot(OUTPUT_SLOT2, containerOutput); }
        else if (ItemStack.isSameItemSameTags(existingOutput, containerOutput)) { existingOutput.grow(containerOutput.getCount()); }
        BerryPressRecipe.ChanceOutput chanceOutput = recipe.getChanceOutput();
        if (chanceOutput != null && this.level.random.nextFloat() < chanceOutput.chance()) {
            ItemStack chanceStack = chanceOutput.stack().copy();
            ItemStack currentOutputStack = itemHandler.getStackInSlot(OUTPUT_SLOT2);
            if (currentOutputStack.isEmpty()) { itemHandler.setStackInSlot(OUTPUT_SLOT2, chanceStack); }
            else if (ItemStack.isSameItemSameTags(currentOutputStack, chanceStack)) {
                int space = itemHandler.getSlotLimit(OUTPUT_SLOT2) - currentOutputStack.getCount();
                if (space > 0) { currentOutputStack.grow(Math.min(space, chanceStack.getCount())); }
            }
        }
        this.cachedRecipe = Optional.empty();
        setChanged();
    }

    private Optional<BerryPressRecipe> getCurrentRecipe() {
        // Crea un contenedor temporal con el tamaño exacto de los ingredientes (2)
        SimpleContainer inventory = new SimpleContainer(2);
        // Coloca los ítems de los slots de ingredientes en los índices correctos del contenedor temporal
        inventory.setItem(0, itemHandler.getStackInSlot(INPUT_SLOT2)); // Ingrediente 1 -> índice 0
        inventory.setItem(1, itemHandler.getStackInSlot(INPUT_SLOT3)); // Ingrediente 2 -> índice 1

        // Busca la receta usando el RecipeManager
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.PRESSED_TYPE.get(), inventory, this.level);
    }



    // --- MANEJO DE CAPACIDADES ---
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER) return lazyFluidHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    // --- GUARDADO Y CARGA (NBT) ---
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("berry_press.progress", progress);
        pTag.putInt("energy", ENERGY_STORAGE.getEnergyStored());
        pTag.put("fluid", FLUID_TANK.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("berry_press.progress");
        ENERGY_STORAGE.setEnergy(pTag.getInt("energy"));
        FLUID_TANK.readFromNBT(pTag.getCompound("fluid"));
    }

    // --- INTERFAZ (MENU PROVIDER) ---
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.floricultura.berry_press");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new BerryPressMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public IFluidHandler getFluidHandler() {
        return this.FLUID_TANK;
    }


    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }


    public int getFluidCapacity() {
        return this.FLUID_TANK.getCapacity();
    }


    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }
}