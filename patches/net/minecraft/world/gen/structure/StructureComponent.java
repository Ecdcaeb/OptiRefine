package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponent {
   protected StructureBoundingBox boundingBox;
   @Nullable
   private EnumFacing coordBaseMode;
   private Mirror mirror;
   private Rotation rotation;
   protected int componentType;

   public StructureComponent() {
   }

   protected StructureComponent(int var1) {
      this.componentType = ☃;
   }

   public final NBTTagCompound createStructureBaseNBT() {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setString("id", MapGenStructureIO.getStructureComponentName(this));
      ☃.setTag("BB", this.boundingBox.toNBTTagIntArray());
      EnumFacing ☃x = this.getCoordBaseMode();
      ☃.setInteger("O", ☃x == null ? -1 : ☃x.getHorizontalIndex());
      ☃.setInteger("GD", this.componentType);
      this.writeStructureToNBT(☃);
      return ☃;
   }

   protected abstract void writeStructureToNBT(NBTTagCompound var1);

   public void readStructureBaseNBT(World var1, NBTTagCompound var2) {
      if (☃.hasKey("BB")) {
         this.boundingBox = new StructureBoundingBox(☃.getIntArray("BB"));
      }

      int ☃ = ☃.getInteger("O");
      this.setCoordBaseMode(☃ == -1 ? null : EnumFacing.byHorizontalIndex(☃));
      this.componentType = ☃.getInteger("GD");
      this.readStructureFromNBT(☃, ☃.getSaveHandler().getStructureTemplateManager());
   }

   protected abstract void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2);

   public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
   }

   public abstract boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3);

   public StructureBoundingBox getBoundingBox() {
      return this.boundingBox;
   }

   public int getComponentType() {
      return this.componentType;
   }

   public static StructureComponent findIntersecting(List<StructureComponent> var0, StructureBoundingBox var1) {
      for (StructureComponent ☃ : ☃) {
         if (☃.getBoundingBox() != null && ☃.getBoundingBox().intersectsWith(☃)) {
            return ☃;
         }
      }

      return null;
   }

   protected boolean isLiquidInStructureBoundingBox(World var1, StructureBoundingBox var2) {
      int ☃ = Math.max(this.boundingBox.minX - 1, ☃.minX);
      int ☃x = Math.max(this.boundingBox.minY - 1, ☃.minY);
      int ☃xx = Math.max(this.boundingBox.minZ - 1, ☃.minZ);
      int ☃xxx = Math.min(this.boundingBox.maxX + 1, ☃.maxX);
      int ☃xxxx = Math.min(this.boundingBox.maxY + 1, ☃.maxY);
      int ☃xxxxx = Math.min(this.boundingBox.maxZ + 1, ☃.maxZ);
      BlockPos.MutableBlockPos ☃xxxxxx = new BlockPos.MutableBlockPos();

      for (int ☃xxxxxxx = ☃; ☃xxxxxxx <= ☃xxx; ☃xxxxxxx++) {
         for (int ☃xxxxxxxx = ☃xx; ☃xxxxxxxx <= ☃xxxxx; ☃xxxxxxxx++) {
            if (☃.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃x, ☃xxxxxxxx)).getMaterial().isLiquid()) {
               return true;
            }

            if (☃.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃xxxx, ☃xxxxxxxx)).getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      for (int ☃xxxxxxx = ☃; ☃xxxxxxx <= ☃xxx; ☃xxxxxxx++) {
         for (int ☃xxxxxxxx = ☃x; ☃xxxxxxxx <= ☃xxxx; ☃xxxxxxxx++) {
            if (☃.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃xxxxxxxx, ☃xx)).getMaterial().isLiquid()) {
               return true;
            }

            if (☃.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxx)).getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      for (int ☃xxxxxxx = ☃xx; ☃xxxxxxx <= ☃xxxxx; ☃xxxxxxx++) {
         for (int ☃xxxxxxxx = ☃x; ☃xxxxxxxx <= ☃xxxx; ☃xxxxxxxx++) {
            if (☃.getBlockState(☃xxxxxx.setPos(☃, ☃xxxxxxxx, ☃xxxxxxx)).getMaterial().isLiquid()) {
               return true;
            }

            if (☃.getBlockState(☃xxxxxx.setPos(☃xxx, ☃xxxxxxxx, ☃xxxxxxx)).getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      return false;
   }

   protected int getXWithOffset(int var1, int var2) {
      EnumFacing ☃ = this.getCoordBaseMode();
      if (☃ == null) {
         return ☃;
      } else {
         switch (☃) {
            case NORTH:
            case SOUTH:
               return this.boundingBox.minX + ☃;
            case WEST:
               return this.boundingBox.maxX - ☃;
            case EAST:
               return this.boundingBox.minX + ☃;
            default:
               return ☃;
         }
      }
   }

   protected int getYWithOffset(int var1) {
      return this.getCoordBaseMode() == null ? ☃ : ☃ + this.boundingBox.minY;
   }

   protected int getZWithOffset(int var1, int var2) {
      EnumFacing ☃ = this.getCoordBaseMode();
      if (☃ == null) {
         return ☃;
      } else {
         switch (☃) {
            case NORTH:
               return this.boundingBox.maxZ - ☃;
            case SOUTH:
               return this.boundingBox.minZ + ☃;
            case WEST:
            case EAST:
               return this.boundingBox.minZ + ☃;
            default:
               return ☃;
         }
      }
   }

   protected void setBlockState(World var1, IBlockState var2, int var3, int var4, int var5, StructureBoundingBox var6) {
      BlockPos ☃ = new BlockPos(this.getXWithOffset(☃, ☃), this.getYWithOffset(☃), this.getZWithOffset(☃, ☃));
      if (☃.isVecInside(☃)) {
         if (this.mirror != Mirror.NONE) {
            ☃ = ☃.withMirror(this.mirror);
         }

         if (this.rotation != Rotation.NONE) {
            ☃ = ☃.withRotation(this.rotation);
         }

         ☃.setBlockState(☃, ☃, 2);
      }
   }

   protected IBlockState getBlockStateFromPos(World var1, int var2, int var3, int var4, StructureBoundingBox var5) {
      int ☃ = this.getXWithOffset(☃, ☃);
      int ☃x = this.getYWithOffset(☃);
      int ☃xx = this.getZWithOffset(☃, ☃);
      BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
      return !☃.isVecInside(☃xxx) ? Blocks.AIR.getDefaultState() : ☃.getBlockState(☃xxx);
   }

   protected int getSkyBrightness(World var1, int var2, int var3, int var4, StructureBoundingBox var5) {
      int ☃ = this.getXWithOffset(☃, ☃);
      int ☃x = this.getYWithOffset(☃ + 1);
      int ☃xx = this.getZWithOffset(☃, ☃);
      BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
      return !☃.isVecInside(☃xxx) ? EnumSkyBlock.SKY.defaultLightValue : ☃.getLightFor(EnumSkyBlock.SKY, ☃xxx);
   }

   protected void fillWithAir(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
         for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
            for (int ☃xx = ☃; ☃xx <= ☃; ☃xx++) {
               this.setBlockState(☃, Blocks.AIR.getDefaultState(), ☃x, ☃, ☃xx, ☃);
            }
         }
      }
   }

   protected void fillWithBlocks(
      World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockState var9, IBlockState var10, boolean var11
   ) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
         for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
            for (int ☃xx = ☃; ☃xx <= ☃; ☃xx++) {
               if (!☃ || this.getBlockStateFromPos(☃, ☃x, ☃, ☃xx, ☃).getMaterial() != Material.AIR) {
                  if (☃ != ☃ && ☃ != ☃ && ☃x != ☃ && ☃x != ☃ && ☃xx != ☃ && ☃xx != ☃) {
                     this.setBlockState(☃, ☃, ☃x, ☃, ☃xx, ☃);
                  } else {
                     this.setBlockState(☃, ☃, ☃x, ☃, ☃xx, ☃);
                  }
               }
            }
         }
      }
   }

   protected void fillWithRandomizedBlocks(
      World var1,
      StructureBoundingBox var2,
      int var3,
      int var4,
      int var5,
      int var6,
      int var7,
      int var8,
      boolean var9,
      Random var10,
      StructureComponent.BlockSelector var11
   ) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
         for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
            for (int ☃xx = ☃; ☃xx <= ☃; ☃xx++) {
               if (!☃ || this.getBlockStateFromPos(☃, ☃x, ☃, ☃xx, ☃).getMaterial() != Material.AIR) {
                  ☃.selectBlocks(☃, ☃x, ☃, ☃xx, ☃ == ☃ || ☃ == ☃ || ☃x == ☃ || ☃x == ☃ || ☃xx == ☃ || ☃xx == ☃);
                  this.setBlockState(☃, ☃.getBlockState(), ☃x, ☃, ☃xx, ☃);
               }
            }
         }
      }
   }

   protected void generateMaybeBox(
      World var1,
      StructureBoundingBox var2,
      Random var3,
      float var4,
      int var5,
      int var6,
      int var7,
      int var8,
      int var9,
      int var10,
      IBlockState var11,
      IBlockState var12,
      boolean var13,
      int var14
   ) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
         for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
            for (int ☃xx = ☃; ☃xx <= ☃; ☃xx++) {
               if (!(☃.nextFloat() > ☃)
                  && (!☃ || this.getBlockStateFromPos(☃, ☃x, ☃, ☃xx, ☃).getMaterial() != Material.AIR)
                  && (☃ <= 0 || this.getSkyBrightness(☃, ☃x, ☃, ☃xx, ☃) < ☃)) {
                  if (☃ != ☃ && ☃ != ☃ && ☃x != ☃ && ☃x != ☃ && ☃xx != ☃ && ☃xx != ☃) {
                     this.setBlockState(☃, ☃, ☃x, ☃, ☃xx, ☃);
                  } else {
                     this.setBlockState(☃, ☃, ☃x, ☃, ☃xx, ☃);
                  }
               }
            }
         }
      }
   }

   protected void randomlyPlaceBlock(World var1, StructureBoundingBox var2, Random var3, float var4, int var5, int var6, int var7, IBlockState var8) {
      if (☃.nextFloat() < ☃) {
         this.setBlockState(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   protected void randomlyRareFillWithBlocks(
      World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockState var9, boolean var10
   ) {
      float ☃ = ☃ - ☃ + 1;
      float ☃x = ☃ - ☃ + 1;
      float ☃xx = ☃ - ☃ + 1;
      float ☃xxx = ☃ + ☃ / 2.0F;
      float ☃xxxx = ☃ + ☃xx / 2.0F;

      for (int ☃xxxxx = ☃; ☃xxxxx <= ☃; ☃xxxxx++) {
         float ☃xxxxxx = (☃xxxxx - ☃) / ☃x;

         for (int ☃xxxxxxx = ☃; ☃xxxxxxx <= ☃; ☃xxxxxxx++) {
            float ☃xxxxxxxx = (☃xxxxxxx - ☃xxx) / (☃ * 0.5F);

            for (int ☃xxxxxxxxx = ☃; ☃xxxxxxxxx <= ☃; ☃xxxxxxxxx++) {
               float ☃xxxxxxxxxx = (☃xxxxxxxxx - ☃xxxx) / (☃xx * 0.5F);
               if (!☃ || this.getBlockStateFromPos(☃, ☃xxxxxxx, ☃xxxxx, ☃xxxxxxxxx, ☃).getMaterial() != Material.AIR) {
                  float ☃xxxxxxxxxxx = ☃xxxxxxxx * ☃xxxxxxxx + ☃xxxxxx * ☃xxxxxx + ☃xxxxxxxxxx * ☃xxxxxxxxxx;
                  if (☃xxxxxxxxxxx <= 1.05F) {
                     this.setBlockState(☃, ☃, ☃xxxxxxx, ☃xxxxx, ☃xxxxxxxxx, ☃);
                  }
               }
            }
         }
      }
   }

   protected void clearCurrentPositionBlocksUpwards(World var1, int var2, int var3, int var4, StructureBoundingBox var5) {
      BlockPos ☃ = new BlockPos(this.getXWithOffset(☃, ☃), this.getYWithOffset(☃), this.getZWithOffset(☃, ☃));
      if (☃.isVecInside(☃)) {
         while (!☃.isAirBlock(☃) && ☃.getY() < 255) {
            ☃.setBlockState(☃, Blocks.AIR.getDefaultState(), 2);
            ☃ = ☃.up();
         }
      }
   }

   protected void replaceAirAndLiquidDownwards(World var1, IBlockState var2, int var3, int var4, int var5, StructureBoundingBox var6) {
      int ☃ = this.getXWithOffset(☃, ☃);
      int ☃x = this.getYWithOffset(☃);
      int ☃xx = this.getZWithOffset(☃, ☃);
      if (☃.isVecInside(new BlockPos(☃, ☃x, ☃xx))) {
         while ((☃.isAirBlock(new BlockPos(☃, ☃x, ☃xx)) || ☃.getBlockState(new BlockPos(☃, ☃x, ☃xx)).getMaterial().isLiquid()) && ☃x > 1) {
            ☃.setBlockState(new BlockPos(☃, ☃x, ☃xx), ☃, 2);
            ☃x--;
         }
      }
   }

   protected boolean generateChest(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, ResourceLocation var7) {
      BlockPos ☃ = new BlockPos(this.getXWithOffset(☃, ☃), this.getYWithOffset(☃), this.getZWithOffset(☃, ☃));
      return this.generateChest(☃, ☃, ☃, ☃, ☃, null);
   }

   protected boolean generateChest(World var1, StructureBoundingBox var2, Random var3, BlockPos var4, ResourceLocation var5, @Nullable IBlockState var6) {
      if (☃.isVecInside(☃) && ☃.getBlockState(☃).getBlock() != Blocks.CHEST) {
         if (☃ == null) {
            ☃ = Blocks.CHEST.correctFacing(☃, ☃, Blocks.CHEST.getDefaultState());
         }

         ☃.setBlockState(☃, ☃, 2);
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityChest) {
            ((TileEntityChest)☃).setLootTable(☃, ☃.nextLong());
         }

         return true;
      } else {
         return false;
      }
   }

   protected boolean createDispenser(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, EnumFacing var7, ResourceLocation var8) {
      BlockPos ☃ = new BlockPos(this.getXWithOffset(☃, ☃), this.getYWithOffset(☃), this.getZWithOffset(☃, ☃));
      if (☃.isVecInside(☃) && ☃.getBlockState(☃).getBlock() != Blocks.DISPENSER) {
         this.setBlockState(☃, Blocks.DISPENSER.getDefaultState().withProperty(BlockDispenser.FACING, ☃), ☃, ☃, ☃, ☃);
         TileEntity ☃x = ☃.getTileEntity(☃);
         if (☃x instanceof TileEntityDispenser) {
            ((TileEntityDispenser)☃x).setLootTable(☃, ☃.nextLong());
         }

         return true;
      } else {
         return false;
      }
   }

   protected void generateDoor(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, EnumFacing var7, BlockDoor var8) {
      this.setBlockState(☃, ☃.getDefaultState().withProperty(BlockDoor.FACING, ☃), ☃, ☃, ☃, ☃);
      this.setBlockState(☃, ☃.getDefaultState().withProperty(BlockDoor.FACING, ☃).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), ☃, ☃ + 1, ☃, ☃);
   }

   public void offset(int var1, int var2, int var3) {
      this.boundingBox.offset(☃, ☃, ☃);
   }

   @Nullable
   public EnumFacing getCoordBaseMode() {
      return this.coordBaseMode;
   }

   public void setCoordBaseMode(@Nullable EnumFacing var1) {
      this.coordBaseMode = ☃;
      if (☃ == null) {
         this.rotation = Rotation.NONE;
         this.mirror = Mirror.NONE;
      } else {
         switch (☃) {
            case SOUTH:
               this.mirror = Mirror.LEFT_RIGHT;
               this.rotation = Rotation.NONE;
               break;
            case WEST:
               this.mirror = Mirror.LEFT_RIGHT;
               this.rotation = Rotation.CLOCKWISE_90;
               break;
            case EAST:
               this.mirror = Mirror.NONE;
               this.rotation = Rotation.CLOCKWISE_90;
               break;
            default:
               this.mirror = Mirror.NONE;
               this.rotation = Rotation.NONE;
         }
      }
   }

   public abstract static class BlockSelector {
      protected IBlockState blockstate = Blocks.AIR.getDefaultState();

      protected BlockSelector() {
      }

      public abstract void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5);

      public IBlockState getBlockState() {
         return this.blockstate;
      }
   }
}
