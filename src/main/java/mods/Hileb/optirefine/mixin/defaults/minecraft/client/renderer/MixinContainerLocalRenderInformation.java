package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumFacing;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(targets = "net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation")
public abstract class MixinContainerLocalRenderInformation {
// [AUDIT] 2026-08-03 — publicize private 3-arg ctor via cursed AT (FML AT <init> entries are not applied in Cleanroom 0.6.7; cursed postApply reliably widens it so the RenderChunk NEW bridge can INVOKESPECIAL it)

    @SuppressWarnings({"unused", "MissingUnique"})
    @Unique
    @AccessTransformer(name = "<init>", access = Opcodes.ACC_PUBLIC)
    public void acc_ctor(RenderGlobal renderGlobal, RenderChunk renderChunk, EnumFacing facing, int counter) {
    }
}
