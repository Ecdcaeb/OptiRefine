package mods.Hileb.optirefine.mixin.minecraft.client.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations.Public;
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

@SuppressWarnings("all")
@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Unique
    private ResourceLocation locationOfCape = null;

    @Unique
    private long reloadCapeTimeMs = 0L;

    @Unique
    private boolean elytraOfCape = false;

    @Unique
    private String nameClear = null;

    @Unique
    @Public
    public EntityShoulderRiding entityShoulderLeft;

    @Unique
    @Public
    public EntityShoulderRiding entityShoulderRight;

    @Unique
    @Public
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectInit(World worldIn, GameProfile playerProfile, CallbackInfo ci){
        this.nameClear = playerProfile.getName();
        if (this.nameClear != null && !this.nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes(this.nameClear);
        }

        CapeUtils.downloadCape((AbstractClientPlayer)(Object) this);
        PlayerConfigurations.getPlayerConfiguration((AbstractClientPlayer)(Object) this);
    }

    @ModifyReturnValue(method = "getLocationCape", at = @At("RETURN"))
    public ResourceLocation injectGetLocationCape(ResourceLocation cir){
        if (!Config.isShowCapes()) {
            return null;
        } else {
            if (this.reloadCapeTimeMs != 0L && System.currentTimeMillis() > this.reloadCapeTimeMs) {
                CapeUtils.reloadCape((AbstractClientPlayer)(Object) this);
                this.reloadCapeTimeMs = 0L;
            }
            if (this.locationOfCape != null) {
                return this.locationOfCape;
            }
        }
        return cir;
    }

    @Unique
    @Public
    public String getNameClear() {
        return this.nameClear;
    }

    @Unique
    @Public
    public ResourceLocation getLocationOfCape() {
        return this.locationOfCape;
    }

    @Unique
    @Public
    public void setLocationOfCape(ResourceLocation locationOfCape) {
        this.locationOfCape = locationOfCape;
    }

    @Unique
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

    @Unique
    @Public
    public void setElytraOfCape(boolean elytraOfCape) {
        this.elytraOfCape = elytraOfCape;
    }

    @Unique
    @Public
    public boolean isElytraOfCape() {
        return this.elytraOfCape;
    }

    @Unique
    @Public
    public long getReloadCapeTimeMs() {
        return this.reloadCapeTimeMs;
    }

    @Unique
    @Public
    public void setReloadCapeTimeMs(long reloadCapeTimeMs) {
        this.reloadCapeTimeMs = reloadCapeTimeMs;
    }

}
