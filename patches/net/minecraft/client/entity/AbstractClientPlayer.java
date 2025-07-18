package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public abstract class AbstractClientPlayer extends EntityPlayer {
   private NetworkPlayerInfo playerInfo;
   public float rotateElytraX;
   public float rotateElytraY;
   public float rotateElytraZ;

   public AbstractClientPlayer(World var1, GameProfile var2) {
      super(☃, ☃);
   }

   @Override
   public boolean isSpectator() {
      NetworkPlayerInfo ☃ = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.getGameProfile().getId());
      return ☃ != null && ☃.getGameType() == GameType.SPECTATOR;
   }

   @Override
   public boolean isCreative() {
      NetworkPlayerInfo ☃ = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.getGameProfile().getId());
      return ☃ != null && ☃.getGameType() == GameType.CREATIVE;
   }

   public boolean hasPlayerInfo() {
      return this.getPlayerInfo() != null;
   }

   @Nullable
   protected NetworkPlayerInfo getPlayerInfo() {
      if (this.playerInfo == null) {
         this.playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.getUniqueID());
      }

      return this.playerInfo;
   }

   public boolean hasSkin() {
      NetworkPlayerInfo ☃ = this.getPlayerInfo();
      return ☃ != null && ☃.hasLocationSkin();
   }

   public ResourceLocation getLocationSkin() {
      NetworkPlayerInfo ☃ = this.getPlayerInfo();
      return ☃ == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : ☃.getLocationSkin();
   }

   @Nullable
   public ResourceLocation getLocationCape() {
      NetworkPlayerInfo ☃ = this.getPlayerInfo();
      return ☃ == null ? null : ☃.getLocationCape();
   }

   public boolean isPlayerInfoSet() {
      return this.getPlayerInfo() != null;
   }

   @Nullable
   public ResourceLocation getLocationElytra() {
      NetworkPlayerInfo ☃ = this.getPlayerInfo();
      return ☃ == null ? null : ☃.getLocationElytra();
   }

   public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation var0, String var1) {
      TextureManager ☃ = Minecraft.getMinecraft().getTextureManager();
      ITextureObject ☃x = ☃.getTexture(☃);
      if (☃x == null) {
         ☃x = new ThreadDownloadImageData(
            null,
            String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(☃)),
            DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(☃)),
            new ImageBufferDownload()
         );
         ☃.loadTexture(☃, ☃x);
      }

      return (ThreadDownloadImageData)☃x;
   }

   public static ResourceLocation getLocationSkin(String var0) {
      return new ResourceLocation("skins/" + StringUtils.stripControlCodes(☃));
   }

   public String getSkinType() {
      NetworkPlayerInfo ☃ = this.getPlayerInfo();
      return ☃ == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID()) : ☃.getSkinType();
   }

   public float getFovModifier() {
      float ☃ = 1.0F;
      if (this.capabilities.isFlying) {
         ☃ *= 1.1F;
      }

      IAttributeInstance ☃x = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      ☃ = (float)(☃ * ((☃x.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0) / 2.0));
      if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(☃) || Float.isInfinite(☃)) {
         ☃ = 1.0F;
      }

      if (this.isHandActive() && this.getActiveItemStack().getItem() == Items.BOW) {
         int ☃xx = this.getItemInUseMaxCount();
         float ☃xxx = ☃xx / 20.0F;
         if (☃xxx > 1.0F) {
            ☃xxx = 1.0F;
         } else {
            ☃xxx *= ☃xxx;
         }

         ☃ *= 1.0F - ☃xxx * 0.15F;
      }

      return ☃;
   }
}
