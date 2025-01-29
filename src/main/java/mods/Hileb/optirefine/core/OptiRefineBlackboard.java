package mods.Hileb.optirefine.core;

import com.google.common.collect.Sets;

import java.util.HashSet;

public class OptiRefineBlackboard {
    public static final HashSet<String> CLASSES = Sets.newHashSet(
            "net.minecraft.block.material.MapColor", // Optifine -> remove final -> at
            "net.minecraft.block.state.BlockStateBase", // Optifine -> add new field and methods -> mixin
            "net.minecraft.block.state.BlockStateBase$1", // Optifine -> final -> skip
            "net.minecraft.block.state.BlockStateContainer", // Optifine -> redirected a construction -> skip
            "net.minecraft.block.state.BlockStateContainer$1", // Optifine -> final -> skip
            "net.minecraft.block.state.BlockStateContainer$Builder",
            "net.minecraft.block.state.BlockStateContainer$StateImplementation", // Optifine -> add new field and methods -> very bad -> skip
            "net.minecraft.block.BlockAir", // Optifine -> add new field and methods -> mixin
            "net.minecraft.client.LoadingScreenRenderer", // Optifine -> customGUI -> mixin FMLClientHandler
            "net.minecraft.client.entity.AbstractClientPlayer", // Optifine -> Capes -> mixin
            "net.minecraft.client.gui.FontRenderer", // Optifine -> custom font colors -> mixin
            "net.minecraft.client.gui.GuiCustomizeSkin", // Optifine -> add Capes button -> mixin
            "net.minecraft.client.gui.GuiCustomizeSkin$1",
            "net.minecraft.client.gui.GuiCustomizeSkin$ButtonPart",
            "net.minecraft.client.gui.GuiDownloadTerrain", // Optifine -> Custom Screen -> mixin
            "net.minecraft.client.gui.GuiIngame", // Optfine -> gui in game -> mixin GuiIngameForge
            "net.minecraft.client.gui.GuiIngame$1",
            "net.minecraft.client.gui.GuiMainMenu",
            "net.minecraft.client.gui.GuiOverlayDebug",
            "net.minecraft.client.gui.GuiOverlayDebug$1",
            "net.minecraft.client.gui.GuiScreenWorking",
            "net.minecraft.client.gui.GuiSlot",
            "net.minecraft.client.gui.GuiVideoSettings",
            "net.minecraft.client.model.ModelBox", // Optifine -> -this.bipedCape.rotationPointY -> skip
            "net.minecraft.client.model.ModelPlayer",
            "net.minecraft.client.model.ModelRenderer",
            "net.minecraft.client.model.TexturedQuad",
            "net.minecraft.client.multiplayer.WorldClient",
            "net.minecraft.client.particle.ParticleItemPickup",
            "net.minecraft.client.particle.ParticleManager",

            "net.minecraft.client.settings.GameSettings$Options",

            "net.minecraft.crash.CrashReport",
            "net.minecraft.entity.EntityLiving",
            "net.minecraft.potion.PotionUtils",
            "net.minecraft.profiler.Profiler",

            "net.minecraft.util.math.ChunkPos"
    );
}
