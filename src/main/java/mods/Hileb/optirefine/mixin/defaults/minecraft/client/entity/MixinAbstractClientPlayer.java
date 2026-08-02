package mods.Hileb.optirefine.mixin.defaults.minecraft.client.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.optifine.player.CapeUtils;
import net.optifine.player.PlayerConfigurations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] OF-added member (OF private ResourceLocation locationOfCape), not in baseline
    @Unique
    private ResourceLocation locationOfCape;

// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private long reloadCapeTimeMs;

// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private boolean elytraOfCape;

// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private String nameClear;

// [AUDIT-OK] OF-added public field, not in baseline; @Public needed for OF-jar access
    @Public
    public EntityShoulderRiding entityShoulderLeft;

// [AUDIT-OK] OF-added public field, not in baseline; @Public needed for OF-jar access
    @Public
    public EntityShoulderRiding entityShoulderRight;

// [AUDIT-OK] OF-added member, not in baseline; @Public informational (OF keeps private)
    @Public
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");

// [AUDIT-OK] target ctor (Lnet/minecraft/world/World;Lcom/mojang/authlib/GameProfile;)V matches baseline
    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectInit(World worldIn, GameProfile playerProfile, CallbackInfo ci){
        this.nameClear = playerProfile.getName();
        if (this.nameClear != null && !this.nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes(this.nameClear);
        }

        CapeUtils.downloadCape((AbstractClientPlayer)(Object) this);
        PlayerConfigurations.getPlayerConfiguration((AbstractClientPlayer)(Object) this);
    }


// [AUDIT-OK] target getLocationCape()Lnet/minecraft/util/ResourceLocation; declared in baseline; instance handler
    @WrapMethod(method = "getLocationCape")
    public ResourceLocation injectGetLocationCape(Operation<ResourceLocation> original){
        if (!Config.isShowCapes()) {
            return null;
        } else {
            if (this.reloadCapeTimeMs != 0L && System.currentTimeMillis() > this.reloadCapeTimeMs) {
                CapeUtils.reloadCape((AbstractClientPlayer) (Object)this);
                this.reloadCapeTimeMs = 0L;
            }
            if (this.locationOfCape != null) {
                return this.locationOfCape;
            } else {
                return original.call();
            }
        }
    }

// [AUDIT-OK] OF-added API (getNameClear..setReloadCapeTimeMs), not in baseline; @Public for OF-jar access
    @Public
    public String getNameClear() {
        return this.nameClear;
    }

// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public ResourceLocation getLocationOfCape() {
        return this.locationOfCape;
    }

// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public void setLocationOfCape(ResourceLocation locationOfCape) {
        this.locationOfCape = locationOfCape;
    }

// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public boolean hasElytraCape() {
        ResourceLocation loc = ((AbstractClientPlayer)(Object)this).getLocationCape();
        if (loc == null) {
            return false;
        }
        if (loc == this.locationOfCape) {
            return this.elytraOfCape;
        }
        return true;
    }

// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public void setElytraOfCape(boolean elytraOfCape) {
        this.elytraOfCape = elytraOfCape;
    }

// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public boolean isElytraOfCape() {
        return this.elytraOfCape;
    }

// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public long getReloadCapeTimeMs() {
        return this.reloadCapeTimeMs;
    }

// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public void setReloadCapeTimeMs(long reloadCapeTimeMs) {
        this.reloadCapeTimeMs = reloadCapeTimeMs;
    }


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.locationOfCape = null;
        this.reloadCapeTimeMs = 0L;
        this.elytraOfCape = false;
        this.nameClear = null;
    }
}
