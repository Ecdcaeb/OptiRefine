package mods.Hileb.optirefine.core;

import com.google.common.collect.Sets;

import java.util.HashSet;

public class OptiRefineBlackboard {
    public static final HashSet<String> CLASSES = Sets.newHashSet(
            "net.minecraft.block.BlockAir", // Optifine -> add new field and methods -> mixin
            "net.minecraft.block.material.MapColor", // Optifine -> remove final -> at
            "net.minecraft.block.state.BlockStateBase", // Optifine -> add new field and methods -> mixin
            "net.minecraft.block.state.BlockStateBase$1", // Optifine -> final -> skip
            "net.minecraft.block.state.BlockStateContainer$1", // Optifine -> final -> skip
            "net.minecraft.block.state.BlockStateContainer$StateImplementation", // Optifine -> add new field and methods -> very bad -> skip
            "net.minecraft.block.state.BlockStateContainer", // Optifine -> redirected a construction -> skip
            "net.minecraft.client.LoadingScreenRenderer", // Optifine -> customGUI -> mixin FMLClientHandler
            "net.minecraft.client.entity.AbstractClientPlayer", // Optifine -> Capes
            "net.minecraft.client.gui.FontRenderer" // Optifine -> custom font colors

    );
}
