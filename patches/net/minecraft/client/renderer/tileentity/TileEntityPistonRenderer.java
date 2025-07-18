package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston> {
   private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

   public void render(TileEntityPiston var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      BlockPos ☃ = ☃.getPos();
      IBlockState ☃x = ☃.getPistonState();
      Block ☃xx = ☃x.getBlock();
      if (☃x.getMaterial() != Material.AIR && !(☃.getProgress(☃) >= 1.0F)) {
         Tessellator ☃xxx = Tessellator.getInstance();
         BufferBuilder ☃xxxx = ☃xxx.getBuffer();
         this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         GlStateManager.enableBlend();
         GlStateManager.disableCull();
         if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(7425);
         } else {
            GlStateManager.shadeModel(7424);
         }

         ☃xxxx.begin(7, DefaultVertexFormats.BLOCK);
         ☃xxxx.setTranslation(☃ - ☃.getX() + ☃.getOffsetX(☃), ☃ - ☃.getY() + ☃.getOffsetY(☃), ☃ - ☃.getZ() + ☃.getOffsetZ(☃));
         World ☃xxxxx = this.getWorld();
         if (☃xx == Blocks.PISTON_HEAD && ☃.getProgress(☃) <= 0.25F) {
            ☃x = ☃x.withProperty(BlockPistonExtension.SHORT, true);
            this.renderStateModel(☃, ☃x, ☃xxxx, ☃xxxxx, true);
         } else if (☃.shouldPistonHeadBeRendered() && !☃.isExtending()) {
            BlockPistonExtension.EnumPistonType ☃xxxxxx = ☃xx == Blocks.STICKY_PISTON
               ? BlockPistonExtension.EnumPistonType.STICKY
               : BlockPistonExtension.EnumPistonType.DEFAULT;
            IBlockState ☃xxxxxxx = Blocks.PISTON_HEAD
               .getDefaultState()
               .withProperty(BlockPistonExtension.TYPE, ☃xxxxxx)
               .withProperty(BlockPistonExtension.FACING, ☃x.getValue(BlockPistonBase.FACING));
            ☃xxxxxxx = ☃xxxxxxx.withProperty(BlockPistonExtension.SHORT, ☃.getProgress(☃) >= 0.5F);
            this.renderStateModel(☃, ☃xxxxxxx, ☃xxxx, ☃xxxxx, true);
            ☃xxxx.setTranslation(☃ - ☃.getX(), ☃ - ☃.getY(), ☃ - ☃.getZ());
            ☃x = ☃x.withProperty(BlockPistonBase.EXTENDED, true);
            this.renderStateModel(☃, ☃x, ☃xxxx, ☃xxxxx, true);
         } else {
            this.renderStateModel(☃, ☃x, ☃xxxx, ☃xxxxx, false);
         }

         ☃xxxx.setTranslation(0.0, 0.0, 0.0);
         ☃xxx.draw();
         RenderHelper.enableStandardItemLighting();
      }
   }

   private boolean renderStateModel(BlockPos var1, IBlockState var2, BufferBuilder var3, World var4, boolean var5) {
      return this.blockRenderer.getBlockModelRenderer().renderModel(☃, this.blockRenderer.getModelForState(☃), ☃, ☃, ☃, ☃);
   }
}
