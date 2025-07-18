package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntitySkullRenderer extends TileEntitySpecialRenderer<TileEntitySkull> {
   private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
   private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
   private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
   private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
   private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");
   private final ModelDragonHead dragonHead = new ModelDragonHead(0.0F);
   public static TileEntitySkullRenderer instance;
   private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
   private final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();

   public void render(TileEntitySkull var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      EnumFacing ☃ = EnumFacing.byIndex(☃.getBlockMetadata() & 7);
      float ☃x = ☃.getAnimationProgress(☃);
      this.renderSkull((float)☃, (float)☃, (float)☃, ☃, ☃.getSkullRotation() * 360 / 16.0F, ☃.getSkullType(), ☃.getPlayerProfile(), ☃, ☃x);
   }

   @Override
   public void setRendererDispatcher(TileEntityRendererDispatcher var1) {
      super.setRendererDispatcher(☃);
      instance = this;
   }

   public void renderSkull(float var1, float var2, float var3, EnumFacing var4, float var5, int var6, @Nullable GameProfile var7, int var8, float var9) {
      ModelBase ☃ = this.skeletonHead;
      if (☃ >= 0) {
         this.bindTexture(DESTROY_STAGES[☃]);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(4.0F, 2.0F, 1.0F);
         GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.matrixMode(5888);
      } else {
         switch (☃) {
            case 0:
            default:
               this.bindTexture(SKELETON_TEXTURES);
               break;
            case 1:
               this.bindTexture(WITHER_SKELETON_TEXTURES);
               break;
            case 2:
               this.bindTexture(ZOMBIE_TEXTURES);
               ☃ = this.humanoidHead;
               break;
            case 3:
               ☃ = this.humanoidHead;
               ResourceLocation ☃x = DefaultPlayerSkin.getDefaultSkinLegacy();
               if (☃ != null) {
                  Minecraft ☃xx = Minecraft.getMinecraft();
                  Map<Type, MinecraftProfileTexture> ☃xxx = ☃xx.getSkinManager().loadSkinFromCache(☃);
                  if (☃xxx.containsKey(Type.SKIN)) {
                     ☃x = ☃xx.getSkinManager().loadSkin(☃xxx.get(Type.SKIN), Type.SKIN);
                  } else {
                     UUID ☃xxxx = EntityPlayer.getUUID(☃);
                     ☃x = DefaultPlayerSkin.getDefaultSkin(☃xxxx);
                  }
               }

               this.bindTexture(☃x);
               break;
            case 4:
               this.bindTexture(CREEPER_TEXTURES);
               break;
            case 5:
               this.bindTexture(DRAGON_TEXTURES);
               ☃ = this.dragonHead;
         }
      }

      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      if (☃ == EnumFacing.UP) {
         GlStateManager.translate(☃ + 0.5F, ☃, ☃ + 0.5F);
      } else {
         switch (☃) {
            case NORTH:
               GlStateManager.translate(☃ + 0.5F, ☃ + 0.25F, ☃ + 0.74F);
               break;
            case SOUTH:
               GlStateManager.translate(☃ + 0.5F, ☃ + 0.25F, ☃ + 0.26F);
               ☃ = 180.0F;
               break;
            case WEST:
               GlStateManager.translate(☃ + 0.74F, ☃ + 0.25F, ☃ + 0.5F);
               ☃ = 270.0F;
               break;
            case EAST:
            default:
               GlStateManager.translate(☃ + 0.26F, ☃ + 0.25F, ☃ + 0.5F);
               ☃ = 90.0F;
         }
      }

      float ☃x = 0.0625F;
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
      GlStateManager.enableAlpha();
      if (☃ == 3) {
         GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
      }

      ☃.render(null, ☃, 0.0F, 0.0F, ☃, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
      if (☃ >= 0) {
         GlStateManager.matrixMode(5890);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
      }
   }
}
