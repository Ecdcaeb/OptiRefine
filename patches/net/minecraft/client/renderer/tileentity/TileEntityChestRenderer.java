package net.minecraft.client.renderer.tileentity;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest> {
   private static final ResourceLocation TEXTURE_TRAPPED_DOUBLE = new ResourceLocation("textures/entity/chest/trapped_double.png");
   private static final ResourceLocation TEXTURE_CHRISTMAS_DOUBLE = new ResourceLocation("textures/entity/chest/christmas_double.png");
   private static final ResourceLocation TEXTURE_NORMAL_DOUBLE = new ResourceLocation("textures/entity/chest/normal_double.png");
   private static final ResourceLocation TEXTURE_TRAPPED = new ResourceLocation("textures/entity/chest/trapped.png");
   private static final ResourceLocation TEXTURE_CHRISTMAS = new ResourceLocation("textures/entity/chest/christmas.png");
   private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("textures/entity/chest/normal.png");
   private final ModelChest simpleChest = new ModelChest();
   private final ModelChest largeChest = new ModelLargeChest();
   private boolean isChristmas;

   public TileEntityChestRenderer() {
      Calendar ☃ = Calendar.getInstance();
      if (☃.get(2) + 1 == 12 && ☃.get(5) >= 24 && ☃.get(5) <= 26) {
         this.isChristmas = true;
      }
   }

   public void render(TileEntityChest var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      GlStateManager.enableDepth();
      GlStateManager.depthFunc(515);
      GlStateManager.depthMask(true);
      int ☃;
      if (☃.hasWorld()) {
         Block ☃x = ☃.getBlockType();
         ☃ = ☃.getBlockMetadata();
         if (☃x instanceof BlockChest && ☃ == 0) {
            ((BlockChest)☃x).checkForSurroundingChests(☃.getWorld(), ☃.getPos(), ☃.getWorld().getBlockState(☃.getPos()));
            ☃ = ☃.getBlockMetadata();
         }

         ☃.checkForAdjacentChests();
      } else {
         ☃ = 0;
      }

      if (☃.adjacentChestZNeg == null && ☃.adjacentChestXNeg == null) {
         ModelChest ☃x;
         if (☃.adjacentChestXPos == null && ☃.adjacentChestZPos == null) {
            ☃x = this.simpleChest;
            if (☃ >= 0) {
               this.bindTexture(DESTROY_STAGES[☃]);
               GlStateManager.matrixMode(5890);
               GlStateManager.pushMatrix();
               GlStateManager.scale(4.0F, 4.0F, 1.0F);
               GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
               GlStateManager.matrixMode(5888);
            } else if (this.isChristmas) {
               this.bindTexture(TEXTURE_CHRISTMAS);
            } else if (☃.getChestType() == BlockChest.Type.TRAP) {
               this.bindTexture(TEXTURE_TRAPPED);
            } else {
               this.bindTexture(TEXTURE_NORMAL);
            }
         } else {
            ☃x = this.largeChest;
            if (☃ >= 0) {
               this.bindTexture(DESTROY_STAGES[☃]);
               GlStateManager.matrixMode(5890);
               GlStateManager.pushMatrix();
               GlStateManager.scale(8.0F, 4.0F, 1.0F);
               GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
               GlStateManager.matrixMode(5888);
            } else if (this.isChristmas) {
               this.bindTexture(TEXTURE_CHRISTMAS_DOUBLE);
            } else if (☃.getChestType() == BlockChest.Type.TRAP) {
               this.bindTexture(TEXTURE_TRAPPED_DOUBLE);
            } else {
               this.bindTexture(TEXTURE_NORMAL_DOUBLE);
            }
         }

         GlStateManager.pushMatrix();
         GlStateManager.enableRescaleNormal();
         if (☃ < 0) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, ☃);
         }

         GlStateManager.translate((float)☃, (float)☃ + 1.0F, (float)☃ + 1.0F);
         GlStateManager.scale(1.0F, -1.0F, -1.0F);
         GlStateManager.translate(0.5F, 0.5F, 0.5F);
         int ☃xx = 0;
         if (☃ == 2) {
            ☃xx = 180;
         }

         if (☃ == 3) {
            ☃xx = 0;
         }

         if (☃ == 4) {
            ☃xx = 90;
         }

         if (☃ == 5) {
            ☃xx = -90;
         }

         if (☃ == 2 && ☃.adjacentChestXPos != null) {
            GlStateManager.translate(1.0F, 0.0F, 0.0F);
         }

         if (☃ == 5 && ☃.adjacentChestZPos != null) {
            GlStateManager.translate(0.0F, 0.0F, -1.0F);
         }

         GlStateManager.rotate(☃xx, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
         float ☃xxx = ☃.prevLidAngle + (☃.lidAngle - ☃.prevLidAngle) * ☃;
         if (☃.adjacentChestZNeg != null) {
            float ☃xxxx = ☃.adjacentChestZNeg.prevLidAngle + (☃.adjacentChestZNeg.lidAngle - ☃.adjacentChestZNeg.prevLidAngle) * ☃;
            if (☃xxxx > ☃xxx) {
               ☃xxx = ☃xxxx;
            }
         }

         if (☃.adjacentChestXNeg != null) {
            float ☃xxxx = ☃.adjacentChestXNeg.prevLidAngle + (☃.adjacentChestXNeg.lidAngle - ☃.adjacentChestXNeg.prevLidAngle) * ☃;
            if (☃xxxx > ☃xxx) {
               ☃xxx = ☃xxxx;
            }
         }

         ☃xxx = 1.0F - ☃xxx;
         ☃xxx = 1.0F - ☃xxx * ☃xxx * ☃xxx;
         ☃x.chestLid.rotateAngleX = -(☃xxx * (float) (Math.PI / 2));
         ☃x.renderAll();
         GlStateManager.disableRescaleNormal();
         GlStateManager.popMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (☃ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
         }
      }
   }
}
