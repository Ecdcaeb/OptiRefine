package mods.Hileb.optirefine.mixin.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Unique
    @SuppressWarnings("all")
    private ResourceLocation locationOfCape = null;

    @Unique
    @SuppressWarnings("all")
    private long reloadCapeTimeMs = 0L;

    @Unique
    @SuppressWarnings("all")
    private boolean elytraOfCape = false;

    @Unique
    @SuppressWarnings("all")
    private String nameClear = null;

    @Unique
    @SuppressWarnings("all")
    public EntityShoulderRiding entityShoulderLeft;

    @Unique
    @SuppressWarnings("all")
    public EntityShoulderRiding entityShoulderRight;

    @Unique
    @SuppressWarnings("all")
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectInit(World worldIn, GameProfile playerProfile, CallbackInfo ci){
        this.nameClear = playerProfile.getName();
        if (this.nameClear != null && !this.nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes(this.nameClear);
        }
        CapeUtils.downloadCape((AbstractClientPlayer)(Object)this);
        PlayerConfigurations.getPlayerConfiguration((AbstractClientPlayer)(Object)this);
    }

    @Inject(method = " getLocationCape", at = @AT("HEAD"), cancellable = true)
    public void injectGetLocationCape(CallbackInfoReturnable<ResourceLocation> cir){
        if (!Config.isShowCapes()) {
            cir.setReturnValue(null);
        } else {
            if (this.reloadCapeTimeMs != 0L && System.currentTimeMillis() > this.reloadCapeTimeMs) {
                CapeUtils.reloadCape((AbstractClientPlayer) (Object) this);
                this.reloadCapeTimeMs = 0L;
            }
            if (this.locationOfCape != null) {
                cir.setReturnValue(this.locationOfCape);
            }
        }
    }

    @Unique
    @SuppressWarnings("all")
    public String getNameClear() {
        return this.nameClear;
    }

    @Unique
    @SuppressWarnings("all")
    public ResourceLocation getLocationOfCape() {
        return this.locationOfCape;
    }

    @Unique
    @SuppressWarnings("all")
    public void setLocationOfCape(ResourceLocation locationOfCape) {
        this.locationOfCape = locationOfCape;
    }

    @Unique
    @SuppressWarnings("all")
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
    @SuppressWarnings("all")
    public void setElytraOfCape(boolean elytraOfCape) {
        this.elytraOfCape = elytraOfCape;
    }

    @Unique
    @SuppressWarnings("all")
    public boolean isElytraOfCape() {
        return this.elytraOfCape;
    }

    @Unique
    @SuppressWarnings("all")
    public long getReloadCapeTimeMs() {
        return this.reloadCapeTimeMs;
    }

    @Unique
    @SuppressWarnings("all")
    public void setReloadCapeTimeMs(long reloadCapeTimeMs) {
        this.reloadCapeTimeMs = reloadCapeTimeMs;
    }


}
