package net.galbatronus.floricultura.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.galbatronus.floricultura.floricultura;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.galbatronus.floricultura.util.RecipeSerializerHelper.readFluidStack;

public class BerryPressRecipe implements Recipe<SimpleContainer> {

    private final int processingTime;
    private final int energyRequired;
    private final int ingredientCount;
    private final @Nullable ChanceOutput chanceOutput;
    private final NonNullList<Ingredient> inputItems;
    private final FluidStack outputFluid;
    private final ItemStack containerOutput;
    private final ResourceLocation id;

    public BerryPressRecipe(NonNullList<Ingredient> inputItems, int ingredientCount, FluidStack outputFluid, ItemStack containerOutput,
                            @Nullable ChanceOutput chanceOutput, int processingTime, int energyRequired, ResourceLocation id) {
        this.inputItems = inputItems;
        this.ingredientCount = ingredientCount;
        this.outputFluid = outputFluid;
        this.containerOutput = containerOutput;
        this.chanceOutput = chanceOutput;
        this.processingTime = processingTime;
        this.energyRequired = energyRequired;
        this.id = id;
    }

    public int getProcessingTime() { return this.processingTime; }
    public int getEnergyRequired() { return this.energyRequired; }
    public @Nullable ChanceOutput getChanceOutput() { return this.chanceOutput; }
    public int getIngredientCount() { return this.ingredientCount; }

    public record ChanceOutput(ItemStack stack, float chance) {}


    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {

        if(inputItems.size() < 2) return false;
        boolean ingredient1Match = inputItems.get(0).test(pContainer.getItem(0));
        boolean ingredient2Match = inputItems.get(1).test(pContainer.getItem(1));
        return ingredient1Match && ingredient2Match;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) { return containerOutput.copy(); }
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) { return true; }
    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) { return containerOutput.copy(); }
    public FluidStack getOutputFluid() { return this.outputFluid.copy(); } // Return copy for safety
    @Override
    public ResourceLocation getId() { return id; }
    @Override
    public RecipeSerializer<?> getSerializer() { return Serializer.INSTANCE; }
    @Override
    public RecipeType<?> getType() { return Type.INSTANCE; }

    public @NotNull ItemStack getContainerOutput() { return this.containerOutput.copy(); } // Return copy
    @Override
    public NonNullList<Ingredient> getIngredients() { return this.inputItems; }




    public static class Type implements RecipeType<BerryPressRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "pressed";
    }

    public static class Serializer implements RecipeSerializer<BerryPressRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(floricultura.MOD_ID, "pressed");

        @Override
        public BerryPressRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            JsonArray ingredientsArray = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredientsArray.size(), Ingredient.EMPTY);

            int ingredientCount = 1;
            if (ingredientsArray.size() > 0) {

                JsonObject firstIngredientJson = ingredientsArray.get(0).getAsJsonObject();

                ingredientCount = GsonHelper.getAsInt(firstIngredientJson, "count", 1);

                inputs.set(0, Ingredient.fromJson(firstIngredientJson));
            }
            for (int i = 1; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredientsArray.get(i)));
            }

            JsonObject fluidObj = GsonHelper.getAsJsonObject(pSerializedRecipe, "outputFluid");
            FluidStack outputFluid = readFluidStack(fluidObj);
            ItemStack containerOutput = ItemStack.EMPTY;
            if (pSerializedRecipe.has("containerOutput")) {
                containerOutput = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "containerOutput"));
            }
            int time = GsonHelper.getAsInt(pSerializedRecipe, "processingTime", 40);
            int energy = GsonHelper.getAsInt(pSerializedRecipe, "energyRequired", 150);
            ChanceOutput chanceOutput = null;
            if (pSerializedRecipe.has("chanceOutput")) {
                JsonObject chanceObj = GsonHelper.getAsJsonObject(pSerializedRecipe, "chanceOutput");
                ItemStack stack = ShapedRecipe.itemStackFromJson(chanceObj);
                float chance = GsonHelper.getAsFloat(chanceObj, "chance", 0.0f);
                chanceOutput = new ChanceOutput(stack, chance);
            }
            return new BerryPressRecipe(inputs, ingredientCount, outputFluid, containerOutput, chanceOutput, time, energy, pRecipeId);
        }

        @Override
        public @Nullable BerryPressRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            // 1. Inputs
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            int ingredientCount = pBuffer.readInt();
            FluidStack outputFluid = FluidStack.loadFluidStackFromNBT(pBuffer.readNbt());
            ItemStack containerOutput = pBuffer.readItem();
            int time = pBuffer.readInt();
            int energy = pBuffer.readInt();
            ChanceOutput chanceOutput = null;
            if (pBuffer.readBoolean()) {
                chanceOutput = new ChanceOutput(pBuffer.readItem(), pBuffer.readFloat());
            }

            return new BerryPressRecipe(inputs, ingredientCount, outputFluid, containerOutput, chanceOutput, time, energy, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BerryPressRecipe pRecipe) {

            NonNullList<Ingredient> ingredients = pRecipe.getIngredients();
            pBuffer.writeInt(ingredients.size());
            for (Ingredient ingredient : ingredients) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeInt(pRecipe.ingredientCount);
            pBuffer.writeNbt(pRecipe.outputFluid.writeToNBT(new CompoundTag()));
            pBuffer.writeItemStack(pRecipe.containerOutput, false);
            pBuffer.writeInt(pRecipe.processingTime);
            pBuffer.writeInt(pRecipe.energyRequired);
            if (pRecipe.chanceOutput != null) {
                pBuffer.writeBoolean(true);
                pBuffer.writeItem(pRecipe.chanceOutput.stack());
                pBuffer.writeFloat(pRecipe.chanceOutput.chance());
            } else {
                pBuffer.writeBoolean(false);
            }
        }
    }
}
