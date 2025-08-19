package mods.Hileb.optirefine.mixin.defaults.minecraft.client.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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

/*

+++ net/minecraft/client/entity/AbstractClientPlayer.java	Tue Aug 19 14:59:58 2025
@@ -1,34 +1,53 @@
 package net.minecraft.client.entity;

 import com.mojang.authlib.GameProfile;
+import java.io.File;
 import javax.annotation.Nullable;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.network.NetworkPlayerInfo;
 import net.minecraft.client.renderer.ImageBufferDownload;
 import net.minecraft.client.renderer.ThreadDownloadImageData;
 import net.minecraft.client.renderer.texture.ITextureObject;
 import net.minecraft.client.renderer.texture.TextureManager;
 import net.minecraft.client.resources.DefaultPlayerSkin;
 import net.minecraft.entity.SharedMonsterAttributes;
 import net.minecraft.entity.ai.attributes.IAttributeInstance;
+import net.minecraft.entity.passive.EntityShoulderRiding;
 import net.minecraft.entity.player.EntityPlayer;
-import net.minecraft.init.Items;
+import net.minecraft.item.ItemBow;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.StringUtils;
 import net.minecraft.world.GameType;
 import net.minecraft.world.World;
+import net.optifine.player.CapeUtils;
+import net.optifine.player.PlayerConfigurations;
+import net.optifine.reflect.Reflector;

 public abstract class AbstractClientPlayer extends EntityPlayer {
    private NetworkPlayerInfo playerInfo;
    public float rotateElytraX;
    public float rotateElytraY;
    public float rotateElytraZ;
+   private ResourceLocation locationOfCape = null;
+   private long reloadCapeTimeMs = 0L;
+   private boolean elytraOfCape = false;
+   private String nameClear = null;
+   public EntityShoulderRiding entityShoulderLeft;
+   public EntityShoulderRiding entityShoulderRight;
+   private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");

    public AbstractClientPlayer(World var1, GameProfile var2) {
       super(var1, var2);
+      this.nameClear = var2.getName();
+      if (this.nameClear != null && !this.nameClear.isEmpty()) {
+         this.nameClear = StringUtils.stripControlCodes(this.nameClear);
+      }
+
+      CapeUtils.downloadCape(this);
+      PlayerConfigurations.getPlayerConfiguration(this);
    }

    public boolean isSpectator() {
       NetworkPlayerInfo var1 = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.getGameProfile().getId());
       return var1 != null && var1.getGameType() == GameType.SPECTATOR;
    }
@@ -60,14 +79,27 @@
       NetworkPlayerInfo var1 = this.getPlayerInfo();
       return var1 == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : var1.getLocationSkin();
    }

    @Nullable
    public ResourceLocation getLocationCape() {
-      NetworkPlayerInfo var1 = this.getPlayerInfo();
-      return var1 == null ? null : var1.getLocationCape();
+      if (!Config.isShowCapes()) {
+         return null;
+      } else {
+         if (this.reloadCapeTimeMs != 0L && System.currentTimeMillis() > this.reloadCapeTimeMs) {
+            CapeUtils.reloadCape(this);
+            this.reloadCapeTimeMs = 0L;
+         }
+
+         if (this.locationOfCape != null) {
+            return this.locationOfCape;
+         } else {
+            NetworkPlayerInfo var1 = this.getPlayerInfo();
+            return var1 == null ? null : var1.getLocationCape();
+         }
+      }
    }

    public boolean isPlayerInfoSet() {
       return this.getPlayerInfo() != null;
    }

@@ -79,13 +111,13 @@

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation var0, String var1) {
       TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
       Object var3 = var2.getTexture(var0);
       if (var3 == null) {
          var3 = new ThreadDownloadImageData(
-            null,
+            (File)null,
             String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(var1)),
             DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(var1)),
             new ImageBufferDownload()
          );
          var2.loadTexture(var0, (ITextureObject)var3);
       }
@@ -111,21 +143,58 @@
       IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
       var1 = (float)(var1 * ((var2.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0) / 2.0));
       if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1) || Float.isInfinite(var1)) {
          var1 = 1.0F;
       }

-      if (this.isHandActive() && this.getActiveItemStack().getItem() == Items.BOW) {
+      if (this.isHandActive() && this.getActiveItemStack().getItem() instanceof ItemBow) {
          int var3 = this.getItemInUseMaxCount();
          float var4 = var3 / 20.0F;
          if (var4 > 1.0F) {
             var4 = 1.0F;
          } else {
             var4 *= var4;
          }

          var1 *= 1.0F - var4 * 0.15F;
       }

-      return var1;
+      return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[]{this, var1}) : var1;
+   }
+
+   public String getNameClear() {
+      return this.nameClear;
+   }
+
+   public ResourceLocation getLocationOfCape() {
+      return this.locationOfCape;
+   }
+
+   public void setLocationOfCape(ResourceLocation var1) {
+      this.locationOfCape = var1;
+   }
+
+   public boolean hasElytraCape() {
+      ResourceLocation var1 = this.getLocationCape();
+      if (var1 == null) {
+         return false;
+      } else {
+         return var1 == this.locationOfCape ? this.elytraOfCape : true;
+      }
+   }
+
+   public void setElytraOfCape(boolean var1) {
+      this.elytraOfCape = var1;
+   }
+
+   public boolean isElytraOfCape() {
+      return this.elytraOfCape;
+   }
+
+   public long getReloadCapeTimeMs() {
+      return this.reloadCapeTimeMs;
+   }
+
+   public void setReloadCapeTimeMs(long var1) {
+      this.reloadCapeTimeMs = var1;
    }
 }

 */
