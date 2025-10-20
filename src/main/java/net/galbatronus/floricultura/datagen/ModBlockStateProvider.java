package net.galbatronus.floricultura.datagen;

import net.galbatronus.floricultura.block.ModBlocks;
import net.galbatronus.floricultura.floricultura;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, floricultura.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels(){

        simpleBlockWithItem(ModBlocks.KANORA_BLOCK.get(), models().cross(blockTexture(ModBlocks.KANORA_BLOCK.get()).getPath(),
                blockTexture(ModBlocks.KANORA_BLOCK.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_KANORA.get(), models().singleTexture("potted_kanora", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.KANORA_BLOCK.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.CAMPANILLAS_CHINAS_BLOCK.get(), models().cross(blockTexture(ModBlocks.CAMPANILLAS_CHINAS_BLOCK.get()).getPath(),
                blockTexture(ModBlocks.CAMPANILLAS_CHINAS_BLOCK.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_CAMPANILLAS_CHINAS.get(), models().singleTexture("potted_campanillas_chinas", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.CAMPANILLAS_CHINAS_BLOCK.get())).renderType("cutout"));

    }





    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}