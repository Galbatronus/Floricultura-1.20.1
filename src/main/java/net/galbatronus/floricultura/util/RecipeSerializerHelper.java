package net.galbatronus.floricultura.util;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerHelper {

    /**
     * Lee un FluidStack desde un objeto JSON.
     * El objeto JSON debe contener:
     * - "fluid": String (la ID del fluido, ej. "minecraft:water")
     * - "amount": int (la cantidad en mB)
     *
     * @param json El objeto JSON que contiene la información del fluido.
     * @return Un FluidStack creado a partir de los datos JSON.
     */
    public static FluidStack readFluidStack(JsonObject json) {
        // 1. Obtener la ID del fluido como una ResourceLocation
        ResourceLocation fluidId = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));

        // 2. Obtener la cantidad (mB)
        int amount = GsonHelper.getAsInt(json, "amount", 1000); // 1000 mB por defecto (1 Bucket)

        // 3. Buscar y obtener la instancia del fluido
        // Usamos ForgeRegistries.FLUIDS para obtener el objeto Fluid.
        net.minecraft.world.level.material.Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);

        if (fluid == null) {
            // Si el fluido no existe, devolvemos un FluidStack vacío (seguro)
            throw new IllegalStateException("Fluid with ID " + fluidId + " not found!");
        }

        // 4. Crear y devolver el FluidStack
        return new FluidStack(fluid, amount);
    }

    /**
     * Escribe un FluidStack en un FriendlyByteBuf para la serialización de red.
     * Esto se implementa comúnmente usando NBT.
     *
     * @param pBuffer El buffer de red.
     * @param stack El FluidStack a escribir.
     */
    public static void writeFluidStack(FriendlyByteBuf pBuffer, FluidStack stack) {
        // La forma más estándar y robusta de enviar FluidStack por red es serializarlo a NBT.
        // Se asume que el método ya existe en tu configuración de Forge.
        pBuffer.writeNbt(stack.writeToNBT(new CompoundTag()));
    }
}
