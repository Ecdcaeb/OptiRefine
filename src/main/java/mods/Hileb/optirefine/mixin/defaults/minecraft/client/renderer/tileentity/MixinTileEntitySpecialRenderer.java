package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.IEntityRenderer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
// [AUDIT-FIXED 2026-08-14] standard @Implements: IEntityRenderer via prefix mapping; the four
// @Unique accessors are prefix-stripped on merge so the target class methods match the interface names
@Implements(@Interface(iface = IEntityRenderer.class, prefix = "optiRefine$"))
@Mixin(TileEntitySpecialRenderer.class)
public abstract class MixinTileEntitySpecialRenderer{
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added IEntityRenderer fields/methods (OF TESR:30-31,90-103); @Implements matches OF 'implements IEntityRenderer'
    @Unique
    // [AUDIT-FIXED] three-way audit (P18): dropped '= null' initializer per convention
    private Class<?> tileEntityClass;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private ResourceLocation locationTextureCustom;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public Class<?> optiRefine$getEntityClass() {
        return this.tileEntityClass;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void optiRefine$setEntityClass(Class<?> tileEntityClass) {
        this.tileEntityClass = tileEntityClass;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public ResourceLocation optiRefine$getLocationTextureCustom() {
        return this.locationTextureCustom;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void optiRefine$setLocationTextureCustom(ResourceLocation locationTextureCustom) {
        this.locationTextureCustom = locationTextureCustom;
    }


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.locationTextureCustom = null;
    }
}
