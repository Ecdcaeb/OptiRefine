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
            "net.minecraft.client.multiplayer.WorldClient$1",
            "net.minecraft.client.multiplayer.WorldClient$2",
            "net.minecraft.client.multiplayer.WorldClient$3",
            "net.minecraft.client.multiplayer.WorldClient$4",
            "net.minecraft.client.particle.ParticleItemPickup",
            "net.minecraft.client.particle.ParticleManager",
            "net.minecraft.client.particle.ParticleManager$1",
            "net.minecraft.client.particle.ParticleManager$2",
            "net.minecraft.client.particle.ParticleManager$3",
            "net.minecraft.client.particle.ParticleManager$4",

            "net.minecraft.client.resources.AbstractResourcePack", // Optifine : access -> ignored
            "net.minecraft.client.resources.DefaultResourcePack", //
            "net.minecraft.client.resources.I18n",

            "net.minecraft.client.resources.ResourcePackRepository$1",
            "net.minecraft.client.resources.ResourcePackRepository$2",
            "net.minecraft.client.resources.ResourcePackRepository$3",
            "net.minecraft.client.resources.ResourcePackRepository$Entry",
            "net.minecraft.client.settings.GameSettings$1",
            "net.minecraft.client.settings.GameSettings$2",
            "net.minecraft.client.settings.GameSettings$Options",



            "net.minecraft.crash.CrashReport",
            "net.minecraft.crash.CrashReport$1",
            "net.minecraft.crash.CrashReport$2",
            "net.minecraft.crash.CrashReport$3",
            "net.minecraft.crash.CrashReport$4",
            "net.minecraft.crash.CrashReport$5",
            "net.minecraft.crash.CrashReport$6",
            "net.minecraft.crash.CrashReport$7",
            "net.minecraft.entity.EntityLiving",
            "net.minecraft.entity.EntityLiving$1",
            "net.minecraft.entity.EntityLiving$SpawnPlacementType",
            "net.minecraft.network.datasync.EntityDataManager",
            "net.minecraft.network.datasync.EntityDataManager$1",
            "net.minecraft.network.PacketThreadUtil",
            "net.minecraft.network.PacketThreadUtil$1",
            "net.minecraft.potion.PotionUtils",
            "net.minecraft.profiler.Profiler",
            "net.minecraft.profiler.Profiler$Result",


            "net.minecraft.util.math.ChunkPos"
    );
}
