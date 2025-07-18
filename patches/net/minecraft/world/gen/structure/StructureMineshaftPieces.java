package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureMineshaftPieces {
   public static void registerStructurePieces() {
      MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Corridor.class, "MSCorridor");
      MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Cross.class, "MSCrossing");
      MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Room.class, "MSRoom");
      MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Stairs.class, "MSStairs");
   }

   private static StructureMineshaftPieces.Peice createRandomShaftPiece(
      List<StructureComponent> var0, Random var1, int var2, int var3, int var4, @Nullable EnumFacing var5, int var6, MapGenMineshaft.Type var7
   ) {
      int ☃ = ☃.nextInt(100);
      if (☃ >= 80) {
         StructureBoundingBox ☃x = StructureMineshaftPieces.Cross.findCrossing(☃, ☃, ☃, ☃, ☃, ☃);
         if (☃x != null) {
            return new StructureMineshaftPieces.Cross(☃, ☃, ☃x, ☃, ☃);
         }
      } else if (☃ >= 70) {
         StructureBoundingBox ☃x = StructureMineshaftPieces.Stairs.findStairs(☃, ☃, ☃, ☃, ☃, ☃);
         if (☃x != null) {
            return new StructureMineshaftPieces.Stairs(☃, ☃, ☃x, ☃, ☃);
         }
      } else {
         StructureBoundingBox ☃x = StructureMineshaftPieces.Corridor.findCorridorSize(☃, ☃, ☃, ☃, ☃, ☃);
         if (☃x != null) {
            return new StructureMineshaftPieces.Corridor(☃, ☃, ☃x, ☃, ☃);
         }
      }

      return null;
   }

   private static StructureMineshaftPieces.Peice generateAndAddPiece(
      StructureComponent var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
   ) {
      if (☃ > 8) {
         return null;
      } else if (Math.abs(☃ - ☃.getBoundingBox().minX) <= 80 && Math.abs(☃ - ☃.getBoundingBox().minZ) <= 80) {
         MapGenMineshaft.Type ☃ = ((StructureMineshaftPieces.Peice)☃).mineShaftType;
         StructureMineshaftPieces.Peice ☃x = createRandomShaftPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃ + 1, ☃);
         if (☃x != null) {
            ☃.add(☃x);
            ☃x.buildComponent(☃, ☃, ☃);
         }

         return ☃x;
      } else {
         return null;
      }
   }

   public static class Corridor extends StructureMineshaftPieces.Peice {
      private boolean hasRails;
      private boolean hasSpiders;
      private boolean spawnerPlaced;
      private int sectionCount;

      public Corridor() {
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("hr", this.hasRails);
         ☃.setBoolean("sc", this.hasSpiders);
         ☃.setBoolean("hps", this.spawnerPlaced);
         ☃.setInteger("Num", this.sectionCount);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.hasRails = ☃.getBoolean("hr");
         this.hasSpiders = ☃.getBoolean("sc");
         this.spawnerPlaced = ☃.getBoolean("hps");
         this.sectionCount = ☃.getInteger("Num");
      }

      public Corridor(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4, MapGenMineshaft.Type var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.hasRails = ☃.nextInt(3) == 0;
         this.hasSpiders = !this.hasRails && ☃.nextInt(23) == 0;
         if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
            this.sectionCount = ☃.getZSize() / 5;
         } else {
            this.sectionCount = ☃.getXSize() / 5;
         }
      }

      public static StructureBoundingBox findCorridorSize(List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5) {
         StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃, ☃, ☃, ☃ + 2, ☃);

         int ☃x;
         for (☃x = ☃.nextInt(3) + 2; ☃x > 0; ☃x--) {
            int ☃xx = ☃x * 5;
            switch (☃) {
               case NORTH:
               default:
                  ☃.maxX = ☃ + 2;
                  ☃.minZ = ☃ - (☃xx - 1);
                  break;
               case SOUTH:
                  ☃.maxX = ☃ + 2;
                  ☃.maxZ = ☃ + (☃xx - 1);
                  break;
               case WEST:
                  ☃.minX = ☃ - (☃xx - 1);
                  ☃.maxZ = ☃ + 2;
                  break;
               case EAST:
                  ☃.maxX = ☃ + (☃xx - 1);
                  ☃.maxZ = ☃ + 2;
            }

            if (StructureComponent.findIntersecting(☃, ☃) == null) {
               break;
            }
         }

         return ☃x > 0 ? ☃ : null;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         int ☃ = this.getComponentType();
         int ☃x = ☃.nextInt(4);
         EnumFacing ☃xx = this.getCoordBaseMode();
         if (☃xx != null) {
            switch (☃xx) {
               case NORTH:
               default:
                  if (☃x <= 1) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.minX, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.minZ - 1, ☃xx, ☃
                     );
                  } else if (☃x == 2) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, ☃
                     );
                  } else {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, ☃
                     );
                  }
                  break;
               case SOUTH:
                  if (☃x <= 1) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.minX, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.maxZ + 1, ☃xx, ☃
                     );
                  } else if (☃x == 2) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, ☃
                     );
                  } else {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, ☃
                     );
                  }
                  break;
               case WEST:
                  if (☃x <= 1) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.minZ, ☃xx, ☃
                     );
                  } else if (☃x == 2) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.minX, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
                     );
                  } else {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.minX, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
                     );
                  }
                  break;
               case EAST:
                  if (☃x <= 1) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.minZ, ☃xx, ☃
                     );
                  } else if (☃x == 2) {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
                     );
                  } else {
                     StructureMineshaftPieces.generateAndAddPiece(
                        ☃, ☃, ☃, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + ☃.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
                     );
                  }
            }
         }

         if (☃ < 8) {
            if (☃xx != EnumFacing.NORTH && ☃xx != EnumFacing.SOUTH) {
               for (int ☃xxx = this.boundingBox.minX + 3; ☃xxx + 3 <= this.boundingBox.maxX; ☃xxx += 5) {
                  int ☃xxxx = ☃.nextInt(5);
                  if (☃xxxx == 0) {
                     StructureMineshaftPieces.generateAndAddPiece(☃, ☃, ☃, ☃xxx, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃ + 1);
                  } else if (☃xxxx == 1) {
                     StructureMineshaftPieces.generateAndAddPiece(☃, ☃, ☃, ☃xxx, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃ + 1);
                  }
               }
            } else {
               for (int ☃xxxx = this.boundingBox.minZ + 3; ☃xxxx + 3 <= this.boundingBox.maxZ; ☃xxxx += 5) {
                  int ☃xxxxx = ☃.nextInt(5);
                  if (☃xxxxx == 0) {
                     StructureMineshaftPieces.generateAndAddPiece(☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY, ☃xxxx, EnumFacing.WEST, ☃ + 1);
                  } else if (☃xxxxx == 1) {
                     StructureMineshaftPieces.generateAndAddPiece(☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY, ☃xxxx, EnumFacing.EAST, ☃ + 1);
                  }
               }
            }
         }
      }

      @Override
      protected boolean generateChest(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, ResourceLocation var7) {
         BlockPos ☃ = new BlockPos(this.getXWithOffset(☃, ☃), this.getYWithOffset(☃), this.getZWithOffset(☃, ☃));
         if (☃.isVecInside(☃) && ☃.getBlockState(☃).getMaterial() == Material.AIR && ☃.getBlockState(☃.down()).getMaterial() != Material.AIR) {
            IBlockState ☃x = Blocks.RAIL
               .getDefaultState()
               .withProperty(BlockRail.SHAPE, ☃.nextBoolean() ? BlockRailBase.EnumRailDirection.NORTH_SOUTH : BlockRailBase.EnumRailDirection.EAST_WEST);
            this.setBlockState(☃, ☃x, ☃, ☃, ☃, ☃);
            EntityMinecartChest ☃xx = new EntityMinecartChest(☃, ☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F);
            ☃xx.setLootTable(☃, ☃.nextLong());
            ☃.spawnEntity(☃xx);
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            int ☃ = 0;
            int ☃x = 2;
            int ☃xx = 0;
            int ☃xxx = 2;
            int ☃xxxx = this.sectionCount * 5 - 1;
            IBlockState ☃xxxxx = this.getPlanksBlock();
            this.fillWithBlocks(☃, ☃, 0, 0, 0, 2, 1, ☃xxxx, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.generateMaybeBox(☃, ☃, ☃, 0.8F, 0, 2, 0, 2, 2, ☃xxxx, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false, 0);
            if (this.hasSpiders) {
               this.generateMaybeBox(☃, ☃, ☃, 0.6F, 0, 0, 0, 2, 1, ☃xxxx, Blocks.WEB.getDefaultState(), Blocks.AIR.getDefaultState(), false, 8);
            }

            for (int ☃xxxxxx = 0; ☃xxxxxx < this.sectionCount; ☃xxxxxx++) {
               int ☃xxxxxxx = 2 + ☃xxxxxx * 5;
               this.placeSupport(☃, ☃, 0, 0, ☃xxxxxxx, 2, 2, ☃);
               this.placeCobWeb(☃, ☃, ☃, 0.1F, 0, 2, ☃xxxxxxx - 1);
               this.placeCobWeb(☃, ☃, ☃, 0.1F, 2, 2, ☃xxxxxxx - 1);
               this.placeCobWeb(☃, ☃, ☃, 0.1F, 0, 2, ☃xxxxxxx + 1);
               this.placeCobWeb(☃, ☃, ☃, 0.1F, 2, 2, ☃xxxxxxx + 1);
               this.placeCobWeb(☃, ☃, ☃, 0.05F, 0, 2, ☃xxxxxxx - 2);
               this.placeCobWeb(☃, ☃, ☃, 0.05F, 2, 2, ☃xxxxxxx - 2);
               this.placeCobWeb(☃, ☃, ☃, 0.05F, 0, 2, ☃xxxxxxx + 2);
               this.placeCobWeb(☃, ☃, ☃, 0.05F, 2, 2, ☃xxxxxxx + 2);
               if (☃.nextInt(100) == 0) {
                  this.generateChest(☃, ☃, ☃, 2, 0, ☃xxxxxxx - 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
               }

               if (☃.nextInt(100) == 0) {
                  this.generateChest(☃, ☃, ☃, 0, 0, ☃xxxxxxx + 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
               }

               if (this.hasSpiders && !this.spawnerPlaced) {
                  int ☃xxxxxxxx = this.getYWithOffset(0);
                  int ☃xxxxxxxxx = ☃xxxxxxx - 1 + ☃.nextInt(3);
                  int ☃xxxxxxxxxx = this.getXWithOffset(1, ☃xxxxxxxxx);
                  int ☃xxxxxxxxxxx = this.getZWithOffset(1, ☃xxxxxxxxx);
                  BlockPos ☃xxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxx);
                  if (☃.isVecInside(☃xxxxxxxxxxxx) && this.getSkyBrightness(☃, 1, 0, ☃xxxxxxxxx, ☃) < 8) {
                     this.spawnerPlaced = true;
                     ☃.setBlockState(☃xxxxxxxxxxxx, Blocks.MOB_SPAWNER.getDefaultState(), 2);
                     TileEntity ☃xxxxxxxxxxxxx = ☃.getTileEntity(☃xxxxxxxxxxxx);
                     if (☃xxxxxxxxxxxxx instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner)☃xxxxxxxxxxxxx).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityCaveSpider.class));
                     }
                  }
               }
            }

            for (int ☃xxxxxx = 0; ☃xxxxxx <= 2; ☃xxxxxx++) {
               for (int ☃xxxxxxxx = 0; ☃xxxxxxxx <= ☃xxxx; ☃xxxxxxxx++) {
                  int ☃xxxxxxxxx = -1;
                  IBlockState ☃xxxxxxxxxx = this.getBlockStateFromPos(☃, ☃xxxxxx, -1, ☃xxxxxxxx, ☃);
                  if (☃xxxxxxxxxx.getMaterial() == Material.AIR && this.getSkyBrightness(☃, ☃xxxxxx, -1, ☃xxxxxxxx, ☃) < 8) {
                     int ☃xxxxxxxxxxx = -1;
                     this.setBlockState(☃, ☃xxxxx, ☃xxxxxx, -1, ☃xxxxxxxx, ☃);
                  }
               }
            }

            if (this.hasRails) {
               IBlockState ☃xxxxxx = Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

               for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx <= ☃xxxx; ☃xxxxxxxxx++) {
                  IBlockState ☃xxxxxxxxxx = this.getBlockStateFromPos(☃, 1, -1, ☃xxxxxxxxx, ☃);
                  if (☃xxxxxxxxxx.getMaterial() != Material.AIR && ☃xxxxxxxxxx.isFullBlock()) {
                     float ☃xxxxxxxxxxx = this.getSkyBrightness(☃, 1, 0, ☃xxxxxxxxx, ☃) > 8 ? 0.9F : 0.7F;
                     this.randomlyPlaceBlock(☃, ☃, ☃, ☃xxxxxxxxxxx, 1, 0, ☃xxxxxxxxx, ☃xxxxxx);
                  }
               }
            }

            return true;
         }
      }

      private void placeSupport(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, Random var8) {
         if (this.isSupportingBox(☃, ☃, ☃, ☃, ☃, ☃)) {
            IBlockState ☃ = this.getPlanksBlock();
            IBlockState ☃x = this.getFenceBlock();
            IBlockState ☃xx = Blocks.AIR.getDefaultState();
            this.fillWithBlocks(☃, ☃, ☃, ☃, ☃, ☃, ☃ - 1, ☃, ☃x, ☃xx, false);
            this.fillWithBlocks(☃, ☃, ☃, ☃, ☃, ☃, ☃ - 1, ☃, ☃x, ☃xx, false);
            if (☃.nextInt(4) == 0) {
               this.fillWithBlocks(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃xx, false);
               this.fillWithBlocks(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃xx, false);
            } else {
               this.fillWithBlocks(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃xx, false);
               this.randomlyPlaceBlock(☃, ☃, ☃, 0.05F, ☃ + 1, ☃, ☃ - 1, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
               this.randomlyPlaceBlock(☃, ☃, ☃, 0.05F, ☃ + 1, ☃, ☃ + 1, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
            }
         }
      }

      private void placeCobWeb(World var1, StructureBoundingBox var2, Random var3, float var4, int var5, int var6, int var7) {
         if (this.getSkyBrightness(☃, ☃, ☃, ☃, ☃) < 8) {
            this.randomlyPlaceBlock(☃, ☃, ☃, ☃, ☃, ☃, ☃, Blocks.WEB.getDefaultState());
         }
      }
   }

   public static class Cross extends StructureMineshaftPieces.Peice {
      private EnumFacing corridorDirection;
      private boolean isMultipleFloors;

      public Cross() {
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("tf", this.isMultipleFloors);
         ☃.setInteger("D", this.corridorDirection.getHorizontalIndex());
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.isMultipleFloors = ☃.getBoolean("tf");
         this.corridorDirection = EnumFacing.byHorizontalIndex(☃.getInteger("D"));
      }

      public Cross(int var1, Random var2, StructureBoundingBox var3, @Nullable EnumFacing var4, MapGenMineshaft.Type var5) {
         super(☃, ☃);
         this.corridorDirection = ☃;
         this.boundingBox = ☃;
         this.isMultipleFloors = ☃.getYSize() > 3;
      }

      public static StructureBoundingBox findCrossing(List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5) {
         StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃, ☃, ☃, ☃ + 2, ☃);
         if (☃.nextInt(4) == 0) {
            ☃.maxY += 4;
         }

         switch (☃) {
            case NORTH:
            default:
               ☃.minX = ☃ - 1;
               ☃.maxX = ☃ + 3;
               ☃.minZ = ☃ - 4;
               break;
            case SOUTH:
               ☃.minX = ☃ - 1;
               ☃.maxX = ☃ + 3;
               ☃.maxZ = ☃ + 3 + 1;
               break;
            case WEST:
               ☃.minX = ☃ - 4;
               ☃.minZ = ☃ - 1;
               ☃.maxZ = ☃ + 3;
               break;
            case EAST:
               ☃.maxX = ☃ + 3 + 1;
               ☃.minZ = ☃ - 1;
               ☃.maxZ = ☃ + 3;
         }

         return StructureComponent.findIntersecting(☃, ☃) != null ? null : ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         int ☃ = this.getComponentType();
         switch (this.corridorDirection) {
            case NORTH:
            default:
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, ☃
               );
               break;
            case SOUTH:
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, ☃
               );
               break;
            case WEST:
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, ☃
               );
               break;
            case EAST:
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
               );
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, ☃
               );
         }

         if (this.isMultipleFloors) {
            if (☃.nextBoolean()) {
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
               );
            }

            if (☃.nextBoolean()) {
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, ☃
               );
            }

            if (☃.nextBoolean()) {
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, ☃
               );
            }

            if (☃.nextBoolean()) {
               StructureMineshaftPieces.generateAndAddPiece(
                  ☃, ☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
               );
            }
         }
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            IBlockState ☃ = this.getPlanksBlock();
            if (this.isMultipleFloors) {
               this.fillWithBlocks(
                  ☃,
                  ☃,
                  this.boundingBox.minX + 1,
                  this.boundingBox.minY,
                  this.boundingBox.minZ,
                  this.boundingBox.maxX - 1,
                  this.boundingBox.minY + 3 - 1,
                  this.boundingBox.maxZ,
                  Blocks.AIR.getDefaultState(),
                  Blocks.AIR.getDefaultState(),
                  false
               );
               this.fillWithBlocks(
                  ☃,
                  ☃,
                  this.boundingBox.minX,
                  this.boundingBox.minY,
                  this.boundingBox.minZ + 1,
                  this.boundingBox.maxX,
                  this.boundingBox.minY + 3 - 1,
                  this.boundingBox.maxZ - 1,
                  Blocks.AIR.getDefaultState(),
                  Blocks.AIR.getDefaultState(),
                  false
               );
               this.fillWithBlocks(
                  ☃,
                  ☃,
                  this.boundingBox.minX + 1,
                  this.boundingBox.maxY - 2,
                  this.boundingBox.minZ,
                  this.boundingBox.maxX - 1,
                  this.boundingBox.maxY,
                  this.boundingBox.maxZ,
                  Blocks.AIR.getDefaultState(),
                  Blocks.AIR.getDefaultState(),
                  false
               );
               this.fillWithBlocks(
                  ☃,
                  ☃,
                  this.boundingBox.minX,
                  this.boundingBox.maxY - 2,
                  this.boundingBox.minZ + 1,
                  this.boundingBox.maxX,
                  this.boundingBox.maxY,
                  this.boundingBox.maxZ - 1,
                  Blocks.AIR.getDefaultState(),
                  Blocks.AIR.getDefaultState(),
                  false
               );
               this.fillWithBlocks(
                  ☃,
                  ☃,
                  this.boundingBox.minX + 1,
                  this.boundingBox.minY + 3,
                  this.boundingBox.minZ + 1,
                  this.boundingBox.maxX - 1,
                  this.boundingBox.minY + 3,
                  this.boundingBox.maxZ - 1,
                  Blocks.AIR.getDefaultState(),
                  Blocks.AIR.getDefaultState(),
                  false
               );
            } else {
               this.fillWithBlocks(
                  ☃,
                  ☃,
                  this.boundingBox.minX + 1,
                  this.boundingBox.minY,
                  this.boundingBox.minZ,
                  this.boundingBox.maxX - 1,
                  this.boundingBox.maxY,
                  this.boundingBox.maxZ,
                  Blocks.AIR.getDefaultState(),
                  Blocks.AIR.getDefaultState(),
                  false
               );
               this.fillWithBlocks(
                  ☃,
                  ☃,
                  this.boundingBox.minX,
                  this.boundingBox.minY,
                  this.boundingBox.minZ + 1,
                  this.boundingBox.maxX,
                  this.boundingBox.maxY,
                  this.boundingBox.maxZ - 1,
                  Blocks.AIR.getDefaultState(),
                  Blocks.AIR.getDefaultState(),
                  false
               );
            }

            this.placeSupportPillar(☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
            this.placeSupportPillar(☃, ☃, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
            this.placeSupportPillar(☃, ☃, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
            this.placeSupportPillar(☃, ☃, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);

            for (int ☃x = this.boundingBox.minX; ☃x <= this.boundingBox.maxX; ☃x++) {
               for (int ☃xx = this.boundingBox.minZ; ☃xx <= this.boundingBox.maxZ; ☃xx++) {
                  if (this.getBlockStateFromPos(☃, ☃x, this.boundingBox.minY - 1, ☃xx, ☃).getMaterial() == Material.AIR
                     && this.getSkyBrightness(☃, ☃x, this.boundingBox.minY - 1, ☃xx, ☃) < 8) {
                     this.setBlockState(☃, ☃, ☃x, this.boundingBox.minY - 1, ☃xx, ☃);
                  }
               }
            }

            return true;
         }
      }

      private void placeSupportPillar(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6) {
         if (this.getBlockStateFromPos(☃, ☃, ☃ + 1, ☃, ☃).getMaterial() != Material.AIR) {
            this.fillWithBlocks(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, this.getPlanksBlock(), Blocks.AIR.getDefaultState(), false);
         }
      }
   }

   abstract static class Peice extends StructureComponent {
      protected MapGenMineshaft.Type mineShaftType;

      public Peice() {
      }

      public Peice(int var1, MapGenMineshaft.Type var2) {
         super(☃);
         this.mineShaftType = ☃;
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         ☃.setInteger("MST", this.mineShaftType.ordinal());
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         this.mineShaftType = MapGenMineshaft.Type.byId(☃.getInteger("MST"));
      }

      protected IBlockState getPlanksBlock() {
         switch (this.mineShaftType) {
            case NORMAL:
            default:
               return Blocks.PLANKS.getDefaultState();
            case MESA:
               return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
         }
      }

      protected IBlockState getFenceBlock() {
         switch (this.mineShaftType) {
            case NORMAL:
            default:
               return Blocks.OAK_FENCE.getDefaultState();
            case MESA:
               return Blocks.DARK_OAK_FENCE.getDefaultState();
         }
      }

      protected boolean isSupportingBox(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6) {
         for (int ☃ = ☃; ☃ <= ☃; ☃++) {
            if (this.getBlockStateFromPos(☃, ☃, ☃ + 1, ☃, ☃).getMaterial() == Material.AIR) {
               return false;
            }
         }

         return true;
      }
   }

   public static class Room extends StructureMineshaftPieces.Peice {
      private final List<StructureBoundingBox> connectedRooms = Lists.newLinkedList();

      public Room() {
      }

      public Room(int var1, Random var2, int var3, int var4, MapGenMineshaft.Type var5) {
         super(☃, ☃);
         this.mineShaftType = ☃;
         this.boundingBox = new StructureBoundingBox(☃, 50, ☃, ☃ + 7 + ☃.nextInt(6), 54 + ☃.nextInt(6), ☃ + 7 + ☃.nextInt(6));
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         int ☃ = this.getComponentType();
         int ☃x = this.boundingBox.getYSize() - 3 - 1;
         if (☃x <= 0) {
            ☃x = 1;
         }

         int ☃xx = 0;

         while (☃xx < this.boundingBox.getXSize()) {
            ☃xx += ☃.nextInt(this.boundingBox.getXSize());
            if (☃xx + 3 > this.boundingBox.getXSize()) {
               break;
            }

            StructureMineshaftPieces.Peice ☃xxx = StructureMineshaftPieces.generateAndAddPiece(
               ☃, ☃, ☃, this.boundingBox.minX + ☃xx, this.boundingBox.minY + ☃.nextInt(☃x) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
            );
            if (☃xxx != null) {
               StructureBoundingBox ☃xxxx = ☃xxx.getBoundingBox();
               this.connectedRooms
                  .add(new StructureBoundingBox(☃xxxx.minX, ☃xxxx.minY, this.boundingBox.minZ, ☃xxxx.maxX, ☃xxxx.maxY, this.boundingBox.minZ + 1));
            }

            ☃xx += 4;
         }

         ☃xx = 0;

         while (☃xx < this.boundingBox.getXSize()) {
            ☃xx += ☃.nextInt(this.boundingBox.getXSize());
            if (☃xx + 3 > this.boundingBox.getXSize()) {
               break;
            }

            StructureMineshaftPieces.Peice ☃xxx = StructureMineshaftPieces.generateAndAddPiece(
               ☃, ☃, ☃, this.boundingBox.minX + ☃xx, this.boundingBox.minY + ☃.nextInt(☃x) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
            );
            if (☃xxx != null) {
               StructureBoundingBox ☃xxxx = ☃xxx.getBoundingBox();
               this.connectedRooms
                  .add(new StructureBoundingBox(☃xxxx.minX, ☃xxxx.minY, this.boundingBox.maxZ - 1, ☃xxxx.maxX, ☃xxxx.maxY, this.boundingBox.maxZ));
            }

            ☃xx += 4;
         }

         ☃xx = 0;

         while (☃xx < this.boundingBox.getZSize()) {
            ☃xx += ☃.nextInt(this.boundingBox.getZSize());
            if (☃xx + 3 > this.boundingBox.getZSize()) {
               break;
            }

            StructureMineshaftPieces.Peice ☃xxx = StructureMineshaftPieces.generateAndAddPiece(
               ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃.nextInt(☃x) + 1, this.boundingBox.minZ + ☃xx, EnumFacing.WEST, ☃
            );
            if (☃xxx != null) {
               StructureBoundingBox ☃xxxx = ☃xxx.getBoundingBox();
               this.connectedRooms
                  .add(new StructureBoundingBox(this.boundingBox.minX, ☃xxxx.minY, ☃xxxx.minZ, this.boundingBox.minX + 1, ☃xxxx.maxY, ☃xxxx.maxZ));
            }

            ☃xx += 4;
         }

         ☃xx = 0;

         while (☃xx < this.boundingBox.getZSize()) {
            ☃xx += ☃.nextInt(this.boundingBox.getZSize());
            if (☃xx + 3 > this.boundingBox.getZSize()) {
               break;
            }

            StructureComponent ☃xxx = StructureMineshaftPieces.generateAndAddPiece(
               ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃.nextInt(☃x) + 1, this.boundingBox.minZ + ☃xx, EnumFacing.EAST, ☃
            );
            if (☃xxx != null) {
               StructureBoundingBox ☃xxxx = ☃xxx.getBoundingBox();
               this.connectedRooms
                  .add(new StructureBoundingBox(this.boundingBox.maxX - 1, ☃xxxx.minY, ☃xxxx.minZ, this.boundingBox.maxX, ☃xxxx.maxY, ☃xxxx.maxZ));
            }

            ☃xx += 4;
         }
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithBlocks(
               ☃,
               ☃,
               this.boundingBox.minX,
               this.boundingBox.minY,
               this.boundingBox.minZ,
               this.boundingBox.maxX,
               this.boundingBox.minY,
               this.boundingBox.maxZ,
               Blocks.DIRT.getDefaultState(),
               Blocks.AIR.getDefaultState(),
               true
            );
            this.fillWithBlocks(
               ☃,
               ☃,
               this.boundingBox.minX,
               this.boundingBox.minY + 1,
               this.boundingBox.minZ,
               this.boundingBox.maxX,
               Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY),
               this.boundingBox.maxZ,
               Blocks.AIR.getDefaultState(),
               Blocks.AIR.getDefaultState(),
               false
            );

            for (StructureBoundingBox ☃ : this.connectedRooms) {
               this.fillWithBlocks(☃, ☃, ☃.minX, ☃.maxY - 2, ☃.minZ, ☃.maxX, ☃.maxY, ☃.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            this.randomlyRareFillWithBlocks(
               ☃,
               ☃,
               this.boundingBox.minX,
               this.boundingBox.minY + 4,
               this.boundingBox.minZ,
               this.boundingBox.maxX,
               this.boundingBox.maxY,
               this.boundingBox.maxZ,
               Blocks.AIR.getDefaultState(),
               false
            );
            return true;
         }
      }

      @Override
      public void offset(int var1, int var2, int var3) {
         super.offset(☃, ☃, ☃);

         for (StructureBoundingBox ☃ : this.connectedRooms) {
            ☃.offset(☃, ☃, ☃);
         }
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         NBTTagList ☃ = new NBTTagList();

         for (StructureBoundingBox ☃x : this.connectedRooms) {
            ☃.appendTag(☃x.toNBTTagIntArray());
         }

         ☃.setTag("Entrances", ☃);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         NBTTagList ☃ = ☃.getTagList("Entrances", 11);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            this.connectedRooms.add(new StructureBoundingBox(☃.getIntArrayAt(☃x)));
         }
      }
   }

   public static class Stairs extends StructureMineshaftPieces.Peice {
      public Stairs() {
      }

      public Stairs(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4, MapGenMineshaft.Type var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      public static StructureBoundingBox findStairs(List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5) {
         StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃ - 5, ☃, ☃, ☃ + 2, ☃);
         switch (☃) {
            case NORTH:
            default:
               ☃.maxX = ☃ + 2;
               ☃.minZ = ☃ - 8;
               break;
            case SOUTH:
               ☃.maxX = ☃ + 2;
               ☃.maxZ = ☃ + 8;
               break;
            case WEST:
               ☃.minX = ☃ - 8;
               ☃.maxZ = ☃ + 2;
               break;
            case EAST:
               ☃.maxX = ☃ + 8;
               ☃.maxZ = ☃ + 2;
         }

         return StructureComponent.findIntersecting(☃, ☃) != null ? null : ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         int ☃ = this.getComponentType();
         EnumFacing ☃x = this.getCoordBaseMode();
         if (☃x != null) {
            switch (☃x) {
               case NORTH:
               default:
                  StructureMineshaftPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, ☃
                  );
                  break;
               case SOUTH:
                  StructureMineshaftPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, ☃
                  );
                  break;
               case WEST:
                  StructureMineshaftPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, ☃
                  );
                  break;
               case EAST:
                  StructureMineshaftPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, ☃
                  );
            }
         }
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithBlocks(☃, ☃, 0, 5, 0, 2, 7, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 0, 0, 7, 2, 2, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

            for (int ☃ = 0; ☃ < 5; ☃++) {
               this.fillWithBlocks(☃, ☃, 0, 5 - ☃ - (☃ < 4 ? 1 : 0), 2 + ☃, 2, 7 - ☃, 2 + ☃, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            return true;
         }
      }
   }
}
