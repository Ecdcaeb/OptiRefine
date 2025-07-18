package net.minecraft.client.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

public class BlockRendererDispatcher implements IResourceManagerReloadListener {
   private final BlockModelShapes blockModelShapes;
   private final BlockModelRenderer blockModelRenderer;
   private final ChestRenderer chestRenderer = new ChestRenderer();
   private final BlockFluidRenderer fluidRenderer;

   public BlockRendererDispatcher(BlockModelShapes var1, BlockColors var2) {
      this.blockModelShapes = ☃;
      this.blockModelRenderer = new BlockModelRenderer(☃);
      this.fluidRenderer = new BlockFluidRenderer(☃);
   }

   public BlockModelShapes getBlockModelShapes() {
      return this.blockModelShapes;
   }

   public void renderBlockDamage(IBlockState var1, BlockPos var2, TextureAtlasSprite var3, IBlockAccess var4) {
      if (☃.getRenderType() == EnumBlockRenderType.MODEL) {
         ☃ = ☃.getActualState(☃, ☃);
         IBakedModel ☃ = this.blockModelShapes.getModelForState(☃);
         IBakedModel ☃x = new SimpleBakedModel.Builder(☃, ☃, ☃, ☃).makeBakedModel();
         this.blockModelRenderer.renderModel(☃, ☃x, ☃, ☃, Tessellator.getInstance().getBuffer(), true);
      }
   }

   public boolean renderBlock(IBlockState var1, BlockPos var2, IBlockAccess var3, BufferBuilder var4) {
      try {
         EnumBlockRenderType ☃ = ☃.getRenderType();
         if (☃ == EnumBlockRenderType.INVISIBLE) {
            return false;
         } else {
            if (☃.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
               try {
                  ☃ = ☃.getActualState(☃, ☃);
               } catch (Exception var8) {
               }
            }

            switch (☃) {
               case MODEL:
                  return this.blockModelRenderer.renderModel(☃, this.getModelForState(☃), ☃, ☃, ☃, true);
               case ENTITYBLOCK_ANIMATED:
                  return false;
               case LIQUID:
                  return this.fluidRenderer.renderFluid(☃, ☃, ☃, ☃);
               default:
                  return false;
            }
         }
      } catch (Throwable var9) {
         CrashReport ☃x = CrashReport.makeCrashReport(var9, "Tesselating block in world");
         CrashReportCategory ☃xx = ☃x.makeCategory("Block being tesselated");
         CrashReportCategory.addBlockInfo(☃xx, ☃, ☃.getBlock(), ☃.getBlock().getMetaFromState(☃));
         throw new ReportedException(☃x);
      }
   }

   public BlockModelRenderer getBlockModelRenderer() {
      return this.blockModelRenderer;
   }

   public IBakedModel getModelForState(IBlockState var1) {
      return this.blockModelShapes.getModelForState(☃);
   }

   public void renderBlockBrightness(IBlockState var1, float var2) {
      EnumBlockRenderType ☃ = ☃.getRenderType();
      if (☃ != EnumBlockRenderType.INVISIBLE) {
         switch (☃) {
            case MODEL:
               IBakedModel ☃x = this.getModelForState(☃);
               this.blockModelRenderer.renderModelBrightness(☃x, ☃, ☃, true);
               break;
            case ENTITYBLOCK_ANIMATED:
               this.chestRenderer.renderChestBrightness(☃.getBlock(), ☃);
            case LIQUID:
         }
      }
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      this.fluidRenderer.initAtlasSprites();
   }
}
