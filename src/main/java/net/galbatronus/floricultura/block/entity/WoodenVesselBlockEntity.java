package net.galbatronus.floricultura.block.entity;

import net.galbatronus.floricultura.fluid.ModFluids;
import net.galbatronus.floricultura.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


public class WoodenVesselBlockEntity extends BlockEntity {

    private static final int GRAPE_SLOT = 0;

    // Función de ayuda para sincronizar cambios entre el servidor y el cliente.
    // Usado por los handlers de inventario y fluido.
    private void sync() {
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluid();
    }

    public int getFluidCapacity() {
        return this.tank.getCapacity();
    }

    private final ItemStackHandler grapeInputHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync(); // Llama a la función de ayuda
        }
    };

    private final FluidTank tank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            sync(); // Llama a la función de ayuda
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            Fluid fluid = stack.getFluid();
            return fluid == ModFluids.SOURCE_PURPLE_GRAPE_JUICE.get() ||
                    fluid == ModFluids.SOURCE_WHITE_GRAPE_JUICE.get() ||
                    fluid == ModFluids.SOURCE_PINK_GRAPE_JUICE.get();
        }
    };

    // --- Mapeos estáticos ---
    private static final java.util.Map<Item, Supplier<Fluid>> GRAPE_TO_JUICE_MAP = java.util.Map.of(
            ModItems.PURPLE_GRAPES.get(), () -> ModFluids.SOURCE_PURPLE_GRAPE_JUICE.get(),
            ModItems.WHITE_GRAPES.get(), () -> ModFluids.SOURCE_WHITE_GRAPE_JUICE.get(),
            ModItems.PINK_GRAPES.get(), () -> ModFluids.SOURCE_PINK_GRAPE_JUICE.get()
    );

    private static final java.util.Map<Fluid, Supplier<Item>> JUICE_TO_BOTTLE_MAP = java.util.Map.of(
            ModFluids.SOURCE_PURPLE_GRAPE_JUICE.get(), ModItems.PURPLE_GRAPES_BOTTLE,
            ModFluids.SOURCE_WHITE_GRAPE_JUICE.get(), ModItems.WHITE_GRAPES_BOTTLE,
            ModFluids.SOURCE_PINK_GRAPE_JUICE.get(), ModItems.ROSE_GRAPES_BOTTLE
    );
    // ------------------------

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    private static final int GRAPES_REQUIRED_PER_CRUSH = 16;
    private static final int JUICE_AMOUNT_PER_CRUSH_MB = 333;
    private static final int CRUSH_THRESHOLD = 4;
    private int crushCounter = 0;

    // Asegúrate de que este constructor tenga la entidad de bloque correcta
    public WoodenVesselBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CRUSHING_GRAPES.get(), pPos, pBlockState);
    }

    public ItemStack getRenderStack() {
        return grapeInputHandler.getStackInSlot(GRAPE_SLOT);
    }

    // --- CAPABILITIES ---
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> grapeInputHandler);
        lazyFluidHandler = LazyOptional.of(() -> tank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    // --- GUARDADO / CARGA ---
    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", grapeInputHandler.serializeNBT());
        tag.put("fluid", tank.writeToNBT(new CompoundTag()));
        tag.putInt("crush_counter", crushCounter);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        grapeInputHandler.deserializeNBT(tag.getCompound("inventory"));
        tank.readFromNBT(tag.getCompound("fluid"));
        crushCounter = tag.getInt("crush_counter");
    }

    // --- LÓGICA DEL JUEGO ---

    // Método tick, llamado por el Block, para lógica de servidor recurrente (ej. fermentación).
    public static void tick(Level level, BlockPos pos, BlockState state, WoodenVesselBlockEntity pEntity) {
        if (level.isClientSide()) return;
        // La lógica de 'tryCrush' NO va aquí; se llama por la interacción del jugador.
    }


    public void tryCrush(Player player) {
        crushCounter++;
        if (crushCounter < CRUSH_THRESHOLD) {
            setChanged();
            return;
        }

        crushCounter = 0;

        ItemStack stack = grapeInputHandler.getStackInSlot(GRAPE_SLOT);

        if (stack.isEmpty() || !isValidGrape(stack) || stack.getCount() < GRAPES_REQUIRED_PER_CRUSH) {
            return;
        }

        Fluid juice = getJuiceFromGrape(stack.getItem());
        if (juice == null) {
            return;
        }

        FluidStack fluidToFill = new FluidStack(juice, JUICE_AMOUNT_PER_CRUSH_MB);
        int filledAmount = tank.fill(fluidToFill, IFluidHandler.FluidAction.SIMULATE);

        if (filledAmount < JUICE_AMOUNT_PER_CRUSH_MB) {
            return;
        }

        stack.shrink(GRAPES_REQUIRED_PER_CRUSH);
        tank.fill(fluidToFill, IFluidHandler.FluidAction.EXECUTE);
        setChanged();
        sync(); // Asegúrate de sincronizar tras el machacado
    }

    public boolean tryInsertItem(ItemStack stack) {
        if (!isValidGrape(stack)) return false;

        ItemStack remaining = grapeInputHandler.insertItem(GRAPE_SLOT, stack.copy().split(1), false);

        return remaining.isEmpty();
    }

    public ItemStack tryDrainToBottle() {
        final int requiredAmount = JUICE_AMOUNT_PER_CRUSH_MB;
        FluidStack drained = tank.drain(requiredAmount, IFluidHandler.FluidAction.SIMULATE);

        if (drained.isEmpty() || drained.getAmount() < requiredAmount) return ItemStack.EMPTY;

        Fluid juice = drained.getFluid();
        Supplier<Item> bottleSupplier = JUICE_TO_BOTTLE_MAP.get(juice);

        if (bottleSupplier != null) {
            // Drena REALMENTE y devuelve la botella
            tank.drain(requiredAmount, IFluidHandler.FluidAction.EXECUTE);
            sync(); // Sincroniza tras el drenaje
            return bottleSupplier.get().getDefaultInstance();
        }

        return ItemStack.EMPTY;
    }

    // --- Métodos de utilidad y visualización ---

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(grapeInputHandler.getSlots());
        for (int i = 0; i < grapeInputHandler.getSlots(); i++) {
            inventory.setItem(i, grapeInputHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public boolean isValidGrape(ItemStack stack) {
        return false;
    }

    private Fluid getJuiceFromGrape(Item grape) {
        Supplier<Fluid> fluidSupplier = GRAPE_TO_JUICE_MAP.get(grape);
        return fluidSupplier != null ? fluidSupplier.get() : null;
    }

    // ... (otros getters y métodos de paquete)

    public int getItemFillPercentage() {
        ItemStack stack = grapeInputHandler.getStackInSlot(GRAPE_SLOT);
        int max = stack.getMaxStackSize();
        if (stack.isEmpty() || max == 0) return 0;
        return (int) ((float) stack.getCount() / max * 100);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    public int getCrushCounter() {
        return this.crushCounter;
    }
}