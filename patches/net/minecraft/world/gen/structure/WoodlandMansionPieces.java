package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class WoodlandMansionPieces {
   public static void registerWoodlandMansionPieces() {
      MapGenStructureIO.registerStructureComponent(WoodlandMansionPieces.MansionTemplate.class, "WMP");
   }

   public static void generateMansion(TemplateManager var0, BlockPos var1, Rotation var2, List<WoodlandMansionPieces.MansionTemplate> var3, Random var4) {
      WoodlandMansionPieces.Grid ☃ = new WoodlandMansionPieces.Grid(☃);
      WoodlandMansionPieces.Placer ☃x = new WoodlandMansionPieces.Placer(☃, ☃);
      ☃x.createMansion(☃, ☃, ☃, ☃);
   }

   static class FirstFloor extends WoodlandMansionPieces.RoomCollection {
      private FirstFloor() {
      }

      @Override
      public String get1x1(Random var1) {
         return "1x1_a" + (☃.nextInt(5) + 1);
      }

      @Override
      public String get1x1Secret(Random var1) {
         return "1x1_as" + (☃.nextInt(4) + 1);
      }

      @Override
      public String get1x2SideEntrance(Random var1, boolean var2) {
         return "1x2_a" + (☃.nextInt(9) + 1);
      }

      @Override
      public String get1x2FrontEntrance(Random var1, boolean var2) {
         return "1x2_b" + (☃.nextInt(5) + 1);
      }

      @Override
      public String get1x2Secret(Random var1) {
         return "1x2_s" + (☃.nextInt(2) + 1);
      }

      @Override
      public String get2x2(Random var1) {
         return "2x2_a" + (☃.nextInt(4) + 1);
      }

      @Override
      public String get2x2Secret(Random var1) {
         return "2x2_s1";
      }
   }

   static class Grid {
      private final Random random;
      private final WoodlandMansionPieces.SimpleGrid baseGrid;
      private final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
      private final WoodlandMansionPieces.SimpleGrid[] floorRooms;
      private final int entranceX;
      private final int entranceY;

      public Grid(Random var1) {
         this.random = ☃;
         int ☃ = 11;
         this.entranceX = 7;
         this.entranceY = 4;
         this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
         this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
         this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
         this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
         this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
         this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
         this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
         this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
         this.baseGrid.set(0, 0, 11, 1, 5);
         this.baseGrid.set(0, 9, 11, 11, 5);
         this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, EnumFacing.WEST, 6);
         this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, EnumFacing.WEST, 6);
         this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, EnumFacing.WEST, 3);
         this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, EnumFacing.WEST, 3);

         while (this.cleanEdges(this.baseGrid)) {
         }

         this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
         this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
         this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
         this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
         this.identifyRooms(this.baseGrid, this.floorRooms[0]);
         this.identifyRooms(this.baseGrid, this.floorRooms[1]);
         this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
         this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
         this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
         this.setupThirdFloor();
         this.identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
      }

      public static boolean isHouse(WoodlandMansionPieces.SimpleGrid var0, int var1, int var2) {
         int ☃ = ☃.get(☃, ☃);
         return ☃ == 1 || ☃ == 2 || ☃ == 3 || ☃ == 4;
      }

      public boolean isRoomId(WoodlandMansionPieces.SimpleGrid var1, int var2, int var3, int var4, int var5) {
         return (this.floorRooms[☃].get(☃, ☃) & 65535) == ☃;
      }

      @Nullable
      public EnumFacing get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid var1, int var2, int var3, int var4, int var5) {
         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL.facings()) {
            if (this.isRoomId(☃, ☃ + ☃.getXOffset(), ☃ + ☃.getZOffset(), ☃, ☃)) {
               return ☃;
            }
         }

         return null;
      }

      private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid var1, int var2, int var3, EnumFacing var4, int var5) {
         if (☃ > 0) {
            ☃.set(☃, ☃, 1);
            ☃.setIf(☃ + ☃.getXOffset(), ☃ + ☃.getZOffset(), 0, 1);

            for (int ☃ = 0; ☃ < 8; ☃++) {
               EnumFacing ☃x = EnumFacing.byHorizontalIndex(this.random.nextInt(4));
               if (☃x != ☃.getOpposite() && (☃x != EnumFacing.EAST || !this.random.nextBoolean())) {
                  int ☃xx = ☃ + ☃.getXOffset();
                  int ☃xxx = ☃ + ☃.getZOffset();
                  if (☃.get(☃xx + ☃x.getXOffset(), ☃xxx + ☃x.getZOffset()) == 0 && ☃.get(☃xx + ☃x.getXOffset() * 2, ☃xxx + ☃x.getZOffset() * 2) == 0) {
                     this.recursiveCorridor(☃, ☃ + ☃.getXOffset() + ☃x.getXOffset(), ☃ + ☃.getZOffset() + ☃x.getZOffset(), ☃x, ☃ - 1);
                     break;
                  }
               }
            }

            EnumFacing ☃x = ☃.rotateY();
            EnumFacing ☃xx = ☃.rotateYCCW();
            ☃.setIf(☃ + ☃x.getXOffset(), ☃ + ☃x.getZOffset(), 0, 2);
            ☃.setIf(☃ + ☃xx.getXOffset(), ☃ + ☃xx.getZOffset(), 0, 2);
            ☃.setIf(☃ + ☃.getXOffset() + ☃x.getXOffset(), ☃ + ☃.getZOffset() + ☃x.getZOffset(), 0, 2);
            ☃.setIf(☃ + ☃.getXOffset() + ☃xx.getXOffset(), ☃ + ☃.getZOffset() + ☃xx.getZOffset(), 0, 2);
            ☃.setIf(☃ + ☃.getXOffset() * 2, ☃ + ☃.getZOffset() * 2, 0, 2);
            ☃.setIf(☃ + ☃x.getXOffset() * 2, ☃ + ☃x.getZOffset() * 2, 0, 2);
            ☃.setIf(☃ + ☃xx.getXOffset() * 2, ☃ + ☃xx.getZOffset() * 2, 0, 2);
         }
      }

      private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid var1) {
         boolean ☃ = false;

         for (int ☃x = 0; ☃x < ☃.height; ☃x++) {
            for (int ☃xx = 0; ☃xx < ☃.width; ☃xx++) {
               if (☃.get(☃xx, ☃x) == 0) {
                  int ☃xxx = 0;
                  ☃xxx += isHouse(☃, ☃xx + 1, ☃x) ? 1 : 0;
                  ☃xxx += isHouse(☃, ☃xx - 1, ☃x) ? 1 : 0;
                  ☃xxx += isHouse(☃, ☃xx, ☃x + 1) ? 1 : 0;
                  ☃xxx += isHouse(☃, ☃xx, ☃x - 1) ? 1 : 0;
                  if (☃xxx >= 3) {
                     ☃.set(☃xx, ☃x, 2);
                     ☃ = true;
                  } else if (☃xxx == 2) {
                     int ☃xxxx = 0;
                     ☃xxxx += isHouse(☃, ☃xx + 1, ☃x + 1) ? 1 : 0;
                     ☃xxxx += isHouse(☃, ☃xx - 1, ☃x + 1) ? 1 : 0;
                     ☃xxxx += isHouse(☃, ☃xx + 1, ☃x - 1) ? 1 : 0;
                     ☃xxxx += isHouse(☃, ☃xx - 1, ☃x - 1) ? 1 : 0;
                     if (☃xxxx <= 1) {
                        ☃.set(☃xx, ☃x, 2);
                        ☃ = true;
                     }
                  }
               }
            }
         }

         return ☃;
      }

      private void setupThirdFloor() {
         List<Tuple<Integer, Integer>> ☃ = Lists.newArrayList();
         WoodlandMansionPieces.SimpleGrid ☃x = this.floorRooms[1];

         for (int ☃xx = 0; ☃xx < this.thirdFloorGrid.height; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx < this.thirdFloorGrid.width; ☃xxx++) {
               int ☃xxxx = ☃x.get(☃xxx, ☃xx);
               int ☃xxxxx = ☃xxxx & 983040;
               if (☃xxxxx == 131072 && (☃xxxx & 2097152) == 2097152) {
                  ☃.add(new Tuple<>(☃xxx, ☃xx));
               }
            }
         }

         if (☃.isEmpty()) {
            this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
         } else {
            Tuple<Integer, Integer> ☃xx = ☃.get(this.random.nextInt(☃.size()));
            int ☃xxxx = ☃x.get(☃xx.getFirst(), ☃xx.getSecond());
            ☃x.set(☃xx.getFirst(), ☃xx.getSecond(), ☃xxxx | 4194304);
            EnumFacing ☃xxxxx = this.get1x2RoomDirection(this.baseGrid, ☃xx.getFirst(), ☃xx.getSecond(), 1, ☃xxxx & 65535);
            int ☃xxxxxx = ☃xx.getFirst() + ☃xxxxx.getXOffset();
            int ☃xxxxxxx = ☃xx.getSecond() + ☃xxxxx.getZOffset();

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < this.thirdFloorGrid.height; ☃xxxxxxxx++) {
               for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < this.thirdFloorGrid.width; ☃xxxxxxxxx++) {
                  if (!isHouse(this.baseGrid, ☃xxxxxxxxx, ☃xxxxxxxx)) {
                     this.thirdFloorGrid.set(☃xxxxxxxxx, ☃xxxxxxxx, 5);
                  } else if (☃xxxxxxxxx == ☃xx.getFirst() && ☃xxxxxxxx == ☃xx.getSecond()) {
                     this.thirdFloorGrid.set(☃xxxxxxxxx, ☃xxxxxxxx, 3);
                  } else if (☃xxxxxxxxx == ☃xxxxxx && ☃xxxxxxxx == ☃xxxxxxx) {
                     this.thirdFloorGrid.set(☃xxxxxxxxx, ☃xxxxxxxx, 3);
                     this.floorRooms[2].set(☃xxxxxxxxx, ☃xxxxxxxx, 8388608);
                  }
               }
            }

            List<EnumFacing> ☃xxxxxxxx = Lists.newArrayList();

            for (EnumFacing ☃xxxxxxxxxx : EnumFacing.Plane.HORIZONTAL.facings()) {
               if (this.thirdFloorGrid.get(☃xxxxxx + ☃xxxxxxxxxx.getXOffset(), ☃xxxxxxx + ☃xxxxxxxxxx.getZOffset()) == 0) {
                  ☃xxxxxxxx.add(☃xxxxxxxxxx);
               }
            }

            if (☃xxxxxxxx.isEmpty()) {
               this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
               ☃x.set(☃xx.getFirst(), ☃xx.getSecond(), ☃xxxx);
            } else {
               EnumFacing ☃xxxxxxxxxxx = ☃xxxxxxxx.get(this.random.nextInt(☃xxxxxxxx.size()));
               this.recursiveCorridor(this.thirdFloorGrid, ☃xxxxxx + ☃xxxxxxxxxxx.getXOffset(), ☃xxxxxxx + ☃xxxxxxxxxxx.getZOffset(), ☃xxxxxxxxxxx, 4);

               while (this.cleanEdges(this.thirdFloorGrid)) {
               }
            }
         }
      }

      private void identifyRooms(WoodlandMansionPieces.SimpleGrid var1, WoodlandMansionPieces.SimpleGrid var2) {
         List<Tuple<Integer, Integer>> ☃ = Lists.newArrayList();

         for (int ☃x = 0; ☃x < ☃.height; ☃x++) {
            for (int ☃xx = 0; ☃xx < ☃.width; ☃xx++) {
               if (☃.get(☃xx, ☃x) == 2) {
                  ☃.add(new Tuple<>(☃xx, ☃x));
               }
            }
         }

         Collections.shuffle(☃, this.random);
         int ☃x = 10;

         for (Tuple<Integer, Integer> ☃xxx : ☃) {
            int ☃xxxx = ☃xxx.getFirst();
            int ☃xxxxx = ☃xxx.getSecond();
            if (☃.get(☃xxxx, ☃xxxxx) == 0) {
               int ☃xxxxxx = ☃xxxx;
               int ☃xxxxxxx = ☃xxxx;
               int ☃xxxxxxxx = ☃xxxxx;
               int ☃xxxxxxxxx = ☃xxxxx;
               int ☃xxxxxxxxxx = 65536;
               if (☃.get(☃xxxx + 1, ☃xxxxx) == 0
                  && ☃.get(☃xxxx, ☃xxxxx + 1) == 0
                  && ☃.get(☃xxxx + 1, ☃xxxxx + 1) == 0
                  && ☃.get(☃xxxx + 1, ☃xxxxx) == 2
                  && ☃.get(☃xxxx, ☃xxxxx + 1) == 2
                  && ☃.get(☃xxxx + 1, ☃xxxxx + 1) == 2) {
                  ☃xxxxxxx = ☃xxxx + 1;
                  ☃xxxxxxxxx = ☃xxxxx + 1;
                  ☃xxxxxxxxxx = 262144;
               } else if (☃.get(☃xxxx - 1, ☃xxxxx) == 0
                  && ☃.get(☃xxxx, ☃xxxxx + 1) == 0
                  && ☃.get(☃xxxx - 1, ☃xxxxx + 1) == 0
                  && ☃.get(☃xxxx - 1, ☃xxxxx) == 2
                  && ☃.get(☃xxxx, ☃xxxxx + 1) == 2
                  && ☃.get(☃xxxx - 1, ☃xxxxx + 1) == 2) {
                  ☃xxxxxx = ☃xxxx - 1;
                  ☃xxxxxxxxx = ☃xxxxx + 1;
                  ☃xxxxxxxxxx = 262144;
               } else if (☃.get(☃xxxx - 1, ☃xxxxx) == 0
                  && ☃.get(☃xxxx, ☃xxxxx - 1) == 0
                  && ☃.get(☃xxxx - 1, ☃xxxxx - 1) == 0
                  && ☃.get(☃xxxx - 1, ☃xxxxx) == 2
                  && ☃.get(☃xxxx, ☃xxxxx - 1) == 2
                  && ☃.get(☃xxxx - 1, ☃xxxxx - 1) == 2) {
                  ☃xxxxxx = ☃xxxx - 1;
                  ☃xxxxxxxx = ☃xxxxx - 1;
                  ☃xxxxxxxxxx = 262144;
               } else if (☃.get(☃xxxx + 1, ☃xxxxx) == 0 && ☃.get(☃xxxx + 1, ☃xxxxx) == 2) {
                  ☃xxxxxxx = ☃xxxx + 1;
                  ☃xxxxxxxxxx = 131072;
               } else if (☃.get(☃xxxx, ☃xxxxx + 1) == 0 && ☃.get(☃xxxx, ☃xxxxx + 1) == 2) {
                  ☃xxxxxxxxx = ☃xxxxx + 1;
                  ☃xxxxxxxxxx = 131072;
               } else if (☃.get(☃xxxx - 1, ☃xxxxx) == 0 && ☃.get(☃xxxx - 1, ☃xxxxx) == 2) {
                  ☃xxxxxx = ☃xxxx - 1;
                  ☃xxxxxxxxxx = 131072;
               } else if (☃.get(☃xxxx, ☃xxxxx - 1) == 0 && ☃.get(☃xxxx, ☃xxxxx - 1) == 2) {
                  ☃xxxxxxxx = ☃xxxxx - 1;
                  ☃xxxxxxxxxx = 131072;
               }

               int ☃xxxxxxxxxxx = this.random.nextBoolean() ? ☃xxxxxx : ☃xxxxxxx;
               int ☃xxxxxxxxxxxx = this.random.nextBoolean() ? ☃xxxxxxxx : ☃xxxxxxxxx;
               int ☃xxxxxxxxxxxxx = 2097152;
               if (!☃.edgesTo(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, 1)) {
                  ☃xxxxxxxxxxx = ☃xxxxxxxxxxx == ☃xxxxxx ? ☃xxxxxxx : ☃xxxxxx;
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx == ☃xxxxxxxx ? ☃xxxxxxxxx : ☃xxxxxxxx;
                  if (!☃.edgesTo(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, 1)) {
                     ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx == ☃xxxxxxxx ? ☃xxxxxxxxx : ☃xxxxxxxx;
                     if (!☃.edgesTo(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, 1)) {
                        ☃xxxxxxxxxxx = ☃xxxxxxxxxxx == ☃xxxxxx ? ☃xxxxxxx : ☃xxxxxx;
                        ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx == ☃xxxxxxxx ? ☃xxxxxxxxx : ☃xxxxxxxx;
                        if (!☃.edgesTo(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, 1)) {
                           ☃xxxxxxxxxxxxx = 0;
                           ☃xxxxxxxxxxx = ☃xxxxxx;
                           ☃xxxxxxxxxxxx = ☃xxxxxxxx;
                        }
                     }
                  }
               }

               for (int ☃xxxxxxxxxxxxxx = ☃xxxxxxxx; ☃xxxxxxxxxxxxxx <= ☃xxxxxxxxx; ☃xxxxxxxxxxxxxx++) {
                  for (int ☃xxxxxxxxxxxxxxx = ☃xxxxxx; ☃xxxxxxxxxxxxxxx <= ☃xxxxxxx; ☃xxxxxxxxxxxxxxx++) {
                     if (☃xxxxxxxxxxxxxxx == ☃xxxxxxxxxxx && ☃xxxxxxxxxxxxxx == ☃xxxxxxxxxxxx) {
                        ☃.set(☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, 1048576 | ☃xxxxxxxxxxxxx | ☃xxxxxxxxxx | ☃x);
                     } else {
                        ☃.set(☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxx | ☃x);
                     }
                  }
               }

               ☃x++;
            }
         }
      }
   }

   public static class MansionTemplate extends StructureComponentTemplate {
      private String templateName;
      private Rotation rotation;
      private Mirror mirror;

      public MansionTemplate() {
      }

      public MansionTemplate(TemplateManager var1, String var2, BlockPos var3, Rotation var4) {
         this(☃, ☃, ☃, ☃, Mirror.NONE);
      }

      public MansionTemplate(TemplateManager var1, String var2, BlockPos var3, Rotation var4, Mirror var5) {
         super(0);
         this.templateName = ☃;
         this.templatePosition = ☃;
         this.rotation = ☃;
         this.mirror = ☃;
         this.loadTemplate(☃);
      }

      private void loadTemplate(TemplateManager var1) {
         Template ☃ = ☃.getTemplate(null, new ResourceLocation("mansion/" + this.templateName));
         PlacementSettings ☃x = new PlacementSettings().setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
         this.setup(☃, this.templatePosition, ☃x);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setString("Template", this.templateName);
         ☃.setString("Rot", this.placeSettings.getRotation().name());
         ☃.setString("Mi", this.placeSettings.getMirror().name());
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.templateName = ☃.getString("Template");
         this.rotation = Rotation.valueOf(☃.getString("Rot"));
         this.mirror = Mirror.valueOf(☃.getString("Mi"));
         this.loadTemplate(☃);
      }

      @Override
      protected void handleDataMarker(String var1, BlockPos var2, World var3, Random var4, StructureBoundingBox var5) {
         if (☃.startsWith("Chest")) {
            Rotation ☃ = this.placeSettings.getRotation();
            IBlockState ☃x = Blocks.CHEST.getDefaultState();
            if ("ChestWest".equals(☃)) {
               ☃x = ☃x.withProperty(BlockChest.FACING, ☃.rotate(EnumFacing.WEST));
            } else if ("ChestEast".equals(☃)) {
               ☃x = ☃x.withProperty(BlockChest.FACING, ☃.rotate(EnumFacing.EAST));
            } else if ("ChestSouth".equals(☃)) {
               ☃x = ☃x.withProperty(BlockChest.FACING, ☃.rotate(EnumFacing.SOUTH));
            } else if ("ChestNorth".equals(☃)) {
               ☃x = ☃x.withProperty(BlockChest.FACING, ☃.rotate(EnumFacing.NORTH));
            }

            this.generateChest(☃, ☃, ☃, ☃, LootTableList.CHESTS_WOODLAND_MANSION, ☃x);
         } else if ("Mage".equals(☃)) {
            EntityEvoker ☃ = new EntityEvoker(☃);
            ☃.enablePersistence();
            ☃.moveToBlockPosAndAngles(☃, 0.0F, 0.0F);
            ☃.spawnEntity(☃);
            ☃.setBlockState(☃, Blocks.AIR.getDefaultState(), 2);
         } else if ("Warrior".equals(☃)) {
            EntityVindicator ☃ = new EntityVindicator(☃);
            ☃.enablePersistence();
            ☃.moveToBlockPosAndAngles(☃, 0.0F, 0.0F);
            ☃.onInitialSpawn(☃.getDifficultyForLocation(new BlockPos(☃)), null);
            ☃.spawnEntity(☃);
            ☃.setBlockState(☃, Blocks.AIR.getDefaultState(), 2);
         }
      }
   }

   static class PlacementData {
      public Rotation rotation;
      public BlockPos position;
      public String wallType;

      private PlacementData() {
      }
   }

   static class Placer {
      private final TemplateManager templateManager;
      private final Random random;
      private int startX;
      private int startY;

      public Placer(TemplateManager var1, Random var2) {
         this.templateManager = ☃;
         this.random = ☃;
      }

      public void createMansion(BlockPos var1, Rotation var2, List<WoodlandMansionPieces.MansionTemplate> var3, WoodlandMansionPieces.Grid var4) {
         WoodlandMansionPieces.PlacementData ☃ = new WoodlandMansionPieces.PlacementData();
         ☃.position = ☃;
         ☃.rotation = ☃;
         ☃.wallType = "wall_flat";
         WoodlandMansionPieces.PlacementData ☃x = new WoodlandMansionPieces.PlacementData();
         this.entrance(☃, ☃);
         ☃x.position = ☃.position.up(8);
         ☃x.rotation = ☃.rotation;
         ☃x.wallType = "wall_window";
         if (!☃.isEmpty()) {
         }

         WoodlandMansionPieces.SimpleGrid ☃xx = ☃.baseGrid;
         WoodlandMansionPieces.SimpleGrid ☃xxx = ☃.thirdFloorGrid;
         this.startX = ☃.entranceX + 1;
         this.startY = ☃.entranceY + 1;
         int ☃xxxx = ☃.entranceX + 1;
         int ☃xxxxx = ☃.entranceY;
         this.traverseOuterWalls(☃, ☃, ☃xx, EnumFacing.SOUTH, this.startX, this.startY, ☃xxxx, ☃xxxxx);
         this.traverseOuterWalls(☃, ☃x, ☃xx, EnumFacing.SOUTH, this.startX, this.startY, ☃xxxx, ☃xxxxx);
         WoodlandMansionPieces.PlacementData ☃xxxxxx = new WoodlandMansionPieces.PlacementData();
         ☃xxxxxx.position = ☃.position.up(19);
         ☃xxxxxx.rotation = ☃.rotation;
         ☃xxxxxx.wallType = "wall_window";
         boolean ☃xxxxxxx = false;

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxx.height && !☃xxxxxxx; ☃xxxxxxxx++) {
            for (int ☃xxxxxxxxx = ☃xxx.width - 1; ☃xxxxxxxxx >= 0 && !☃xxxxxxx; ☃xxxxxxxxx--) {
               if (WoodlandMansionPieces.Grid.isHouse(☃xxx, ☃xxxxxxxxx, ☃xxxxxxxx)) {
                  ☃xxxxxx.position = ☃xxxxxx.position.offset(☃.rotate(EnumFacing.SOUTH), 8 + (☃xxxxxxxx - this.startY) * 8);
                  ☃xxxxxx.position = ☃xxxxxx.position.offset(☃.rotate(EnumFacing.EAST), (☃xxxxxxxxx - this.startX) * 8);
                  this.traverseWallPiece(☃, ☃xxxxxx);
                  this.traverseOuterWalls(☃, ☃xxxxxx, ☃xxx, EnumFacing.SOUTH, ☃xxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxx);
                  ☃xxxxxxx = true;
               }
            }
         }

         this.createRoof(☃, ☃.up(16), ☃, ☃xx, ☃xxx);
         this.createRoof(☃, ☃.up(27), ☃, ☃xxx, null);
         if (!☃.isEmpty()) {
         }

         WoodlandMansionPieces.RoomCollection[] ☃xxxxxxxx = new WoodlandMansionPieces.RoomCollection[]{
            new WoodlandMansionPieces.FirstFloor(), new WoodlandMansionPieces.SecondFloor(), new WoodlandMansionPieces.ThirdFloor()
         };

         for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < 3; ☃xxxxxxxxxx++) {
            BlockPos ☃xxxxxxxxxxx = ☃.up(8 * ☃xxxxxxxxxx + (☃xxxxxxxxxx == 2 ? 3 : 0));
            WoodlandMansionPieces.SimpleGrid ☃xxxxxxxxxxxx = ☃.floorRooms[☃xxxxxxxxxx];
            WoodlandMansionPieces.SimpleGrid ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx == 2 ? ☃xxx : ☃xx;
            String ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx == 0 ? "carpet_south" : "carpet_south_2";
            String ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxx == 0 ? "carpet_west" : "carpet_west_2";

            for (int ☃xxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxx.height; ☃xxxxxxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxx.width; ☃xxxxxxxxxxxxxxxxx++) {
                  if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx) == 1) {
                     BlockPos ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 8 + (☃xxxxxxxxxxxxxxxx - this.startY) * 8);
                     ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.EAST), (☃xxxxxxxxxxxxxxxxx - this.startX) * 8);
                     ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "corridor_floor", ☃xxxxxxxxxxxxxxxxxx, ☃));
                     if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx - 1) == 1
                        || (☃xxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx - 1) & 8388608) == 8388608) {
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager, "carpet_north", ☃xxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.EAST), 1).up(), ☃
                           )
                        );
                     }

                     if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx + 1, ☃xxxxxxxxxxxxxxxx) == 1
                        || (☃xxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx + 1, ☃xxxxxxxxxxxxxxxx) & 8388608) == 8388608) {
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager,
                              "carpet_east",
                              ☃xxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 1).offset(☃.rotate(EnumFacing.EAST), 5).up(),
                              ☃
                           )
                        );
                     }

                     if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx + 1) == 1
                        || (☃xxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx + 1) & 8388608) == 8388608) {
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager,
                              ☃xxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 5).offset(☃.rotate(EnumFacing.WEST), 1),
                              ☃
                           )
                        );
                     }

                     if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxxxx) == 1
                        || (☃xxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxxxx) & 8388608) == 8388608) {
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager,
                              ☃xxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.WEST), 1).offset(☃.rotate(EnumFacing.NORTH), 1),
                              ☃
                           )
                        );
                     }
                  }
               }
            }

            String ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxx == 0 ? "indoors_wall" : "indoors_wall_2";
            String ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxx == 0 ? "indoors_door" : "indoors_door_2";
            List<EnumFacing> ☃xxxxxxxxxxxxxxxxxxxx = Lists.newArrayList();

            for (int ☃xxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxx.height; ☃xxxxxxxxxxxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxx.width; ☃xxxxxxxxxxxxxxxxxxxxxx++) {
                  boolean ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxx == 2 && ☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx) == 3;
                  if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx) == 2 || ☃xxxxxxxxxxxxxxxxxxxxxxx) {
                     int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx);
                     int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx & 983040;
                     int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx & 65535;
                     ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxx && (☃xxxxxxxxxxxxxxxxxxxxxxxx & 8388608) == 8388608;
                     ☃xxxxxxxxxxxxxxxxxxxx.clear();
                     if ((☃xxxxxxxxxxxxxxxxxxxxxxxx & 2097152) == 2097152) {
                        for (EnumFacing ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx : EnumFacing.Plane.HORIZONTAL.facings()) {
                           if (☃xxxxxxxxxxxxx.get(
                                 ☃xxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.getXOffset(),
                                 ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.getZOffset()
                              )
                              == 1) {
                              ☃xxxxxxxxxxxxxxxxxxxx.add(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx);
                           }
                        }
                     }

                     EnumFacing ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = null;
                     if (!☃xxxxxxxxxxxxxxxxxxxx.isEmpty()) {
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx.get(this.random.nextInt(☃xxxxxxxxxxxxxxxxxxxx.size()));
                     } else if ((☃xxxxxxxxxxxxxxxxxxxxxxxx & 1048576) == 1048576) {
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = EnumFacing.UP;
                     }

                     BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 8 + (☃xxxxxxxxxxxxxxxxxxxxx - this.startY) * 8);
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.offset(
                        ☃.rotate(EnumFacing.EAST), -1 + (☃xxxxxxxxxxxxxxxxxxxxxx - this.startX) * 8
                     );
                     if (WoodlandMansionPieces.Grid.isHouse(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxxxxxxxxx)
                        && !☃.isRoomId(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx)) {
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.WEST ? ☃xxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃
                           )
                        );
                     }

                     if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxx + 1, ☃xxxxxxxxxxxxxxxxxxxxx) == 1 && !☃xxxxxxxxxxxxxxxxxxxxxxx) {
                        BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.EAST), 8);
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.EAST ? ☃xxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃
                           )
                        );
                     }

                     if (WoodlandMansionPieces.Grid.isHouse(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx + 1)
                        && !☃.isRoomId(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx + 1, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx)) {
                        BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 7);
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.EAST), 7);
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.SOUTH ? ☃xxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃.add(Rotation.CLOCKWISE_90)
                           )
                        );
                     }

                     if (☃xxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx - 1) == 1 && !☃xxxxxxxxxxxxxxxxxxxxxxx) {
                        BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.NORTH), 1);
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.offset(☃.rotate(EnumFacing.EAST), 7);
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(
                              this.templateManager,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.NORTH ? ☃xxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃.add(Rotation.CLOCKWISE_90)
                           )
                        );
                     }

                     if (☃xxxxxxxxxxxxxxxxxxxxxxxxx == 65536) {
                        this.addRoom1x1(☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxx[☃xxxxxxxxxx]);
                     } else if (☃xxxxxxxxxxxxxxxxxxxxxxxxx == 131072 && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx != null) {
                        EnumFacing ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.get1x2RoomDirection(
                           ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx
                        );
                        boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxx & 4194304) == 4194304;
                        this.addRoom1x2(
                           ☃,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                           ☃,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                           ☃xxxxxxxx[☃xxxxxxxxxx],
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        );
                     } else if (☃xxxxxxxxxxxxxxxxxxxxxxxxx == 262144 && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx != null && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx != EnumFacing.UP
                        )
                      {
                        EnumFacing ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx.rotateY();
                        if (!☃.isRoomId(
                           ☃xxxxxxxxxxxxx,
                           ☃xxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getXOffset(),
                           ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getZOffset(),
                           ☃xxxxxxxxxx,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxx
                        )) {
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getOpposite();
                        }

                        this.addRoom2x2(
                           ☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxx[☃xxxxxxxxxx]
                        );
                     } else if (☃xxxxxxxxxxxxxxxxxxxxxxxxx == 262144 && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.UP) {
                        this.addRoom2x2Secret(☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃, ☃xxxxxxxx[☃xxxxxxxxxx]);
                     }
                  }
               }
            }
         }
      }

      private void traverseOuterWalls(
         List<WoodlandMansionPieces.MansionTemplate> var1,
         WoodlandMansionPieces.PlacementData var2,
         WoodlandMansionPieces.SimpleGrid var3,
         EnumFacing var4,
         int var5,
         int var6,
         int var7,
         int var8
      ) {
         int ☃ = ☃;
         int ☃x = ☃;
         EnumFacing ☃xx = ☃;

         do {
            if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃ + ☃.getXOffset(), ☃x + ☃.getZOffset())) {
               this.traverseTurn(☃, ☃);
               ☃ = ☃.rotateY();
               if (☃ != ☃ || ☃x != ☃ || ☃xx != ☃) {
                  this.traverseWallPiece(☃, ☃);
               }
            } else if (WoodlandMansionPieces.Grid.isHouse(☃, ☃ + ☃.getXOffset(), ☃x + ☃.getZOffset())
               && WoodlandMansionPieces.Grid.isHouse(☃, ☃ + ☃.getXOffset() + ☃.rotateYCCW().getXOffset(), ☃x + ☃.getZOffset() + ☃.rotateYCCW().getZOffset())) {
               this.traverseInnerTurn(☃, ☃);
               ☃ += ☃.getXOffset();
               ☃x += ☃.getZOffset();
               ☃ = ☃.rotateYCCW();
            } else {
               ☃ += ☃.getXOffset();
               ☃x += ☃.getZOffset();
               if (☃ != ☃ || ☃x != ☃ || ☃xx != ☃) {
                  this.traverseWallPiece(☃, ☃);
               }
            }
         } while (☃ != ☃ || ☃x != ☃ || ☃xx != ☃);
      }

      private void createRoof(
         List<WoodlandMansionPieces.MansionTemplate> var1,
         BlockPos var2,
         Rotation var3,
         WoodlandMansionPieces.SimpleGrid var4,
         @Nullable WoodlandMansionPieces.SimpleGrid var5
      ) {
         for (int ☃ = 0; ☃ < ☃.height; ☃++) {
            for (int ☃x = 0; ☃x < ☃.width; ☃x++) {
               BlockPos var8 = ☃.offset(☃.rotate(EnumFacing.SOUTH), 8 + (☃ - this.startY) * 8);
               var8 = var8.offset(☃.rotate(EnumFacing.EAST), (☃x - this.startX) * 8);
               boolean ☃xx = ☃ != null && WoodlandMansionPieces.Grid.isHouse(☃, ☃x, ☃);
               if (WoodlandMansionPieces.Grid.isHouse(☃, ☃x, ☃) && !☃xx) {
                  ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof", var8.up(3), ☃));
                  if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃x + 1, ☃)) {
                     BlockPos ☃xxx = var8.offset(☃.rotate(EnumFacing.EAST), 6);
                     ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_front", ☃xxx, ☃));
                  }

                  if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃x - 1, ☃)) {
                     BlockPos ☃xxx = var8.offset(☃.rotate(EnumFacing.EAST), 0);
                     ☃xxx = ☃xxx.offset(☃.rotate(EnumFacing.SOUTH), 7);
                     ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_front", ☃xxx, ☃.add(Rotation.CLOCKWISE_180)));
                  }

                  if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃x, ☃ - 1)) {
                     BlockPos ☃xxx = var8.offset(☃.rotate(EnumFacing.WEST), 1);
                     ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_front", ☃xxx, ☃.add(Rotation.COUNTERCLOCKWISE_90)));
                  }

                  if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃x, ☃ + 1)) {
                     BlockPos ☃xxx = var8.offset(☃.rotate(EnumFacing.EAST), 6);
                     ☃xxx = ☃xxx.offset(☃.rotate(EnumFacing.SOUTH), 6);
                     ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_front", ☃xxx, ☃.add(Rotation.CLOCKWISE_90)));
                  }
               }
            }
         }

         if (☃ != null) {
            for (int ☃ = 0; ☃ < ☃.height; ☃++) {
               for (int ☃xx = 0; ☃xx < ☃.width; ☃xx++) {
                  BlockPos var17 = ☃.offset(☃.rotate(EnumFacing.SOUTH), 8 + (☃ - this.startY) * 8);
                  var17 = var17.offset(☃.rotate(EnumFacing.EAST), (☃xx - this.startX) * 8);
                  boolean ☃xxx = WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃);
                  if (WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃) && ☃xxx) {
                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx + 1, ☃)) {
                        BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.EAST), 7);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall", ☃xxxx, ☃));
                     }

                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx - 1, ☃)) {
                        BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.WEST), 1);
                        ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.SOUTH), 6);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall", ☃xxxx, ☃.add(Rotation.CLOCKWISE_180)));
                     }

                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃ - 1)) {
                        BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.WEST), 0);
                        ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.NORTH), 1);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall", ☃xxxx, ☃.add(Rotation.COUNTERCLOCKWISE_90)));
                     }

                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃ + 1)) {
                        BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.EAST), 6);
                        ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.SOUTH), 7);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall", ☃xxxx, ☃.add(Rotation.CLOCKWISE_90)));
                     }

                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx + 1, ☃)) {
                        if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃ - 1)) {
                           BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.EAST), 7);
                           ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.NORTH), 2);
                           ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall_corner", ☃xxxx, ☃));
                        }

                        if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃ + 1)) {
                           BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.EAST), 8);
                           ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.SOUTH), 7);
                           ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall_corner", ☃xxxx, ☃.add(Rotation.CLOCKWISE_90)));
                        }
                     }

                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx - 1, ☃)) {
                        if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃ - 1)) {
                           BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.WEST), 2);
                           ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.NORTH), 1);
                           ☃.add(
                              new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall_corner", ☃xxxx, ☃.add(Rotation.COUNTERCLOCKWISE_90))
                           );
                        }

                        if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xx, ☃ + 1)) {
                           BlockPos ☃xxxx = var17.offset(☃.rotate(EnumFacing.WEST), 1);
                           ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.SOUTH), 8);
                           ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "small_wall_corner", ☃xxxx, ☃.add(Rotation.CLOCKWISE_180)));
                        }
                     }
                  }
               }
            }
         }

         for (int ☃ = 0; ☃ < ☃.height; ☃++) {
            for (int ☃xxx = 0; ☃xxx < ☃.width; ☃xxx++) {
               BlockPos var19 = ☃.offset(☃.rotate(EnumFacing.SOUTH), 8 + (☃ - this.startY) * 8);
               var19 = var19.offset(☃.rotate(EnumFacing.EAST), (☃xxx - this.startX) * 8);
               boolean ☃xxxx = ☃ != null && WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx, ☃);
               if (WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx, ☃) && !☃xxxx) {
                  if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx + 1, ☃)) {
                     BlockPos ☃xxxxx = var19.offset(☃.rotate(EnumFacing.EAST), 6);
                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx, ☃ + 1)) {
                        BlockPos ☃xxxxxx = ☃xxxxx.offset(☃.rotate(EnumFacing.SOUTH), 6);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_corner", ☃xxxxxx, ☃));
                     } else if (WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx + 1, ☃ + 1)) {
                        BlockPos ☃xxxxxx = ☃xxxxx.offset(☃.rotate(EnumFacing.SOUTH), 5);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_inner_corner", ☃xxxxxx, ☃));
                     }

                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx, ☃ - 1)) {
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_corner", ☃xxxxx, ☃.add(Rotation.COUNTERCLOCKWISE_90)));
                     } else if (WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx + 1, ☃ - 1)) {
                        BlockPos ☃xxxxxx = var19.offset(☃.rotate(EnumFacing.EAST), 9);
                        ☃xxxxxx = ☃xxxxxx.offset(☃.rotate(EnumFacing.NORTH), 2);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_inner_corner", ☃xxxxxx, ☃.add(Rotation.CLOCKWISE_90)));
                     }
                  }

                  if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx - 1, ☃)) {
                     BlockPos ☃xxxxxx = var19.offset(☃.rotate(EnumFacing.EAST), 0);
                     ☃xxxxxx = ☃xxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 0);
                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx, ☃ + 1)) {
                        BlockPos ☃xxxxxxx = ☃xxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 6);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_corner", ☃xxxxxxx, ☃.add(Rotation.CLOCKWISE_90)));
                     } else if (WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx - 1, ☃ + 1)) {
                        BlockPos ☃xxxxxxx = ☃xxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 8);
                        ☃xxxxxxx = ☃xxxxxxx.offset(☃.rotate(EnumFacing.WEST), 3);
                        ☃.add(
                           new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_inner_corner", ☃xxxxxxx, ☃.add(Rotation.COUNTERCLOCKWISE_90))
                        );
                     }

                     if (!WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx, ☃ - 1)) {
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_corner", ☃xxxxxx, ☃.add(Rotation.CLOCKWISE_180)));
                     } else if (WoodlandMansionPieces.Grid.isHouse(☃, ☃xxx - 1, ☃ - 1)) {
                        BlockPos ☃xxxxxxx = ☃xxxxxx.offset(☃.rotate(EnumFacing.SOUTH), 1);
                        ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "roof_inner_corner", ☃xxxxxxx, ☃.add(Rotation.CLOCKWISE_180)));
                     }
                  }
               }
            }
         }
      }

      private void entrance(List<WoodlandMansionPieces.MansionTemplate> var1, WoodlandMansionPieces.PlacementData var2) {
         EnumFacing ☃ = ☃.rotation.rotate(EnumFacing.WEST);
         ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "entrance", ☃.position.offset(☃, 9), ☃.rotation));
         ☃.position = ☃.position.offset(☃.rotation.rotate(EnumFacing.SOUTH), 16);
      }

      private void traverseWallPiece(List<WoodlandMansionPieces.MansionTemplate> var1, WoodlandMansionPieces.PlacementData var2) {
         ☃.add(
            new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.wallType, ☃.position.offset(☃.rotation.rotate(EnumFacing.EAST), 7), ☃.rotation)
         );
         ☃.position = ☃.position.offset(☃.rotation.rotate(EnumFacing.SOUTH), 8);
      }

      private void traverseTurn(List<WoodlandMansionPieces.MansionTemplate> var1, WoodlandMansionPieces.PlacementData var2) {
         ☃.position = ☃.position.offset(☃.rotation.rotate(EnumFacing.SOUTH), -1);
         ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, "wall_corner", ☃.position, ☃.rotation));
         ☃.position = ☃.position.offset(☃.rotation.rotate(EnumFacing.SOUTH), -7);
         ☃.position = ☃.position.offset(☃.rotation.rotate(EnumFacing.WEST), -6);
         ☃.rotation = ☃.rotation.add(Rotation.CLOCKWISE_90);
      }

      private void traverseInnerTurn(List<WoodlandMansionPieces.MansionTemplate> var1, WoodlandMansionPieces.PlacementData var2) {
         ☃.position = ☃.position.offset(☃.rotation.rotate(EnumFacing.SOUTH), 6);
         ☃.position = ☃.position.offset(☃.rotation.rotate(EnumFacing.EAST), 8);
         ☃.rotation = ☃.rotation.add(Rotation.COUNTERCLOCKWISE_90);
      }

      private void addRoom1x1(
         List<WoodlandMansionPieces.MansionTemplate> var1, BlockPos var2, Rotation var3, EnumFacing var4, WoodlandMansionPieces.RoomCollection var5
      ) {
         Rotation ☃ = Rotation.NONE;
         String ☃x = ☃.get1x1(this.random);
         if (☃ != EnumFacing.EAST) {
            if (☃ == EnumFacing.NORTH) {
               ☃ = ☃.add(Rotation.COUNTERCLOCKWISE_90);
            } else if (☃ == EnumFacing.WEST) {
               ☃ = ☃.add(Rotation.CLOCKWISE_180);
            } else if (☃ == EnumFacing.SOUTH) {
               ☃ = ☃.add(Rotation.CLOCKWISE_90);
            } else {
               ☃x = ☃.get1x1Secret(this.random);
            }
         }

         BlockPos ☃xx = Template.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, ☃, 7, 7);
         ☃ = ☃.add(☃);
         ☃xx = ☃xx.rotate(☃);
         BlockPos ☃xxx = ☃.add(☃xx.getX(), 0, ☃xx.getZ());
         ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃x, ☃xxx, ☃));
      }

      private void addRoom1x2(
         List<WoodlandMansionPieces.MansionTemplate> var1,
         BlockPos var2,
         Rotation var3,
         EnumFacing var4,
         EnumFacing var5,
         WoodlandMansionPieces.RoomCollection var6,
         boolean var7
      ) {
         if (☃ == EnumFacing.EAST && ☃ == EnumFacing.SOUTH) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 1);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃));
         } else if (☃ == EnumFacing.EAST && ☃ == EnumFacing.NORTH) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 1);
            ☃ = ☃.offset(☃.rotate(EnumFacing.SOUTH), 6);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃, Mirror.LEFT_RIGHT));
         } else if (☃ == EnumFacing.WEST && ☃ == EnumFacing.NORTH) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 7);
            ☃ = ☃.offset(☃.rotate(EnumFacing.SOUTH), 6);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃.add(Rotation.CLOCKWISE_180)));
         } else if (☃ == EnumFacing.WEST && ☃ == EnumFacing.SOUTH) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 7);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃, Mirror.FRONT_BACK));
         } else if (☃ == EnumFacing.SOUTH && ☃ == EnumFacing.EAST) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 1);
            ☃.add(
               new WoodlandMansionPieces.MansionTemplate(
                  this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃.add(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT
               )
            );
         } else if (☃ == EnumFacing.SOUTH && ☃ == EnumFacing.WEST) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 7);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃.add(Rotation.CLOCKWISE_90)));
         } else if (☃ == EnumFacing.NORTH && ☃ == EnumFacing.WEST) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 7);
            ☃ = ☃.offset(☃.rotate(EnumFacing.SOUTH), 6);
            ☃.add(
               new WoodlandMansionPieces.MansionTemplate(
                  this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃.add(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK
               )
            );
         } else if (☃ == EnumFacing.NORTH && ☃ == EnumFacing.EAST) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 1);
            ☃ = ☃.offset(☃.rotate(EnumFacing.SOUTH), 6);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2SideEntrance(this.random, ☃), ☃, ☃.add(Rotation.COUNTERCLOCKWISE_90)));
         } else if (☃ == EnumFacing.SOUTH && ☃ == EnumFacing.NORTH) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 1);
            ☃ = ☃.offset(☃.rotate(EnumFacing.NORTH), 8);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2FrontEntrance(this.random, ☃), ☃, ☃));
         } else if (☃ == EnumFacing.NORTH && ☃ == EnumFacing.SOUTH) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 7);
            ☃ = ☃.offset(☃.rotate(EnumFacing.SOUTH), 14);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2FrontEntrance(this.random, ☃), ☃, ☃.add(Rotation.CLOCKWISE_180)));
         } else if (☃ == EnumFacing.WEST && ☃ == EnumFacing.EAST) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 15);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2FrontEntrance(this.random, ☃), ☃, ☃.add(Rotation.CLOCKWISE_90)));
         } else if (☃ == EnumFacing.EAST && ☃ == EnumFacing.WEST) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.WEST), 7);
            ☃ = ☃.offset(☃.rotate(EnumFacing.SOUTH), 6);
            ☃.add(
               new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2FrontEntrance(this.random, ☃), ☃, ☃.add(Rotation.COUNTERCLOCKWISE_90))
            );
         } else if (☃ == EnumFacing.UP && ☃ == EnumFacing.EAST) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 15);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2Secret(this.random), ☃, ☃.add(Rotation.CLOCKWISE_90)));
         } else if (☃ == EnumFacing.UP && ☃ == EnumFacing.SOUTH) {
            BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 1);
            ☃ = ☃.offset(☃.rotate(EnumFacing.NORTH), 0);
            ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get1x2Secret(this.random), ☃, ☃));
         }
      }

      private void addRoom2x2(
         List<WoodlandMansionPieces.MansionTemplate> var1,
         BlockPos var2,
         Rotation var3,
         EnumFacing var4,
         EnumFacing var5,
         WoodlandMansionPieces.RoomCollection var6
      ) {
         int ☃ = 0;
         int ☃x = 0;
         Rotation ☃xx = ☃;
         Mirror ☃xxx = Mirror.NONE;
         if (☃ == EnumFacing.EAST && ☃ == EnumFacing.SOUTH) {
            ☃ = -7;
         } else if (☃ == EnumFacing.EAST && ☃ == EnumFacing.NORTH) {
            ☃ = -7;
            ☃x = 6;
            ☃xxx = Mirror.LEFT_RIGHT;
         } else if (☃ == EnumFacing.NORTH && ☃ == EnumFacing.EAST) {
            ☃ = 1;
            ☃x = 14;
            ☃xx = ☃.add(Rotation.COUNTERCLOCKWISE_90);
         } else if (☃ == EnumFacing.NORTH && ☃ == EnumFacing.WEST) {
            ☃ = 7;
            ☃x = 14;
            ☃xx = ☃.add(Rotation.COUNTERCLOCKWISE_90);
            ☃xxx = Mirror.LEFT_RIGHT;
         } else if (☃ == EnumFacing.SOUTH && ☃ == EnumFacing.WEST) {
            ☃ = 7;
            ☃x = -8;
            ☃xx = ☃.add(Rotation.CLOCKWISE_90);
         } else if (☃ == EnumFacing.SOUTH && ☃ == EnumFacing.EAST) {
            ☃ = 1;
            ☃x = -8;
            ☃xx = ☃.add(Rotation.CLOCKWISE_90);
            ☃xxx = Mirror.LEFT_RIGHT;
         } else if (☃ == EnumFacing.WEST && ☃ == EnumFacing.NORTH) {
            ☃ = 15;
            ☃x = 6;
            ☃xx = ☃.add(Rotation.CLOCKWISE_180);
         } else if (☃ == EnumFacing.WEST && ☃ == EnumFacing.SOUTH) {
            ☃ = 15;
            ☃xxx = Mirror.FRONT_BACK;
         }

         BlockPos ☃xxxx = ☃.offset(☃.rotate(EnumFacing.EAST), ☃);
         ☃xxxx = ☃xxxx.offset(☃.rotate(EnumFacing.SOUTH), ☃x);
         ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get2x2(this.random), ☃xxxx, ☃xx, ☃xxx));
      }

      private void addRoom2x2Secret(List<WoodlandMansionPieces.MansionTemplate> var1, BlockPos var2, Rotation var3, WoodlandMansionPieces.RoomCollection var4) {
         BlockPos ☃ = ☃.offset(☃.rotate(EnumFacing.EAST), 1);
         ☃.add(new WoodlandMansionPieces.MansionTemplate(this.templateManager, ☃.get2x2Secret(this.random), ☃, ☃, Mirror.NONE));
      }
   }

   abstract static class RoomCollection {
      private RoomCollection() {
      }

      public abstract String get1x1(Random var1);

      public abstract String get1x1Secret(Random var1);

      public abstract String get1x2SideEntrance(Random var1, boolean var2);

      public abstract String get1x2FrontEntrance(Random var1, boolean var2);

      public abstract String get1x2Secret(Random var1);

      public abstract String get2x2(Random var1);

      public abstract String get2x2Secret(Random var1);
   }

   static class SecondFloor extends WoodlandMansionPieces.RoomCollection {
      private SecondFloor() {
      }

      @Override
      public String get1x1(Random var1) {
         return "1x1_b" + (☃.nextInt(4) + 1);
      }

      @Override
      public String get1x1Secret(Random var1) {
         return "1x1_as" + (☃.nextInt(4) + 1);
      }

      @Override
      public String get1x2SideEntrance(Random var1, boolean var2) {
         return ☃ ? "1x2_c_stairs" : "1x2_c" + (☃.nextInt(4) + 1);
      }

      @Override
      public String get1x2FrontEntrance(Random var1, boolean var2) {
         return ☃ ? "1x2_d_stairs" : "1x2_d" + (☃.nextInt(5) + 1);
      }

      @Override
      public String get1x2Secret(Random var1) {
         return "1x2_se" + (☃.nextInt(1) + 1);
      }

      @Override
      public String get2x2(Random var1) {
         return "2x2_b" + (☃.nextInt(5) + 1);
      }

      @Override
      public String get2x2Secret(Random var1) {
         return "2x2_s1";
      }
   }

   static class SimpleGrid {
      private final int[][] grid;
      private final int width;
      private final int height;
      private final int valueIfOutside;

      public SimpleGrid(int var1, int var2, int var3) {
         this.width = ☃;
         this.height = ☃;
         this.valueIfOutside = ☃;
         this.grid = new int[☃][☃];
      }

      public void set(int var1, int var2, int var3) {
         if (☃ >= 0 && ☃ < this.width && ☃ >= 0 && ☃ < this.height) {
            this.grid[☃][☃] = ☃;
         }
      }

      public void set(int var1, int var2, int var3, int var4, int var5) {
         for (int ☃ = ☃; ☃ <= ☃; ☃++) {
            for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
               this.set(☃x, ☃, ☃);
            }
         }
      }

      public int get(int var1, int var2) {
         return ☃ >= 0 && ☃ < this.width && ☃ >= 0 && ☃ < this.height ? this.grid[☃][☃] : this.valueIfOutside;
      }

      public void setIf(int var1, int var2, int var3, int var4) {
         if (this.get(☃, ☃) == ☃) {
            this.set(☃, ☃, ☃);
         }
      }

      public boolean edgesTo(int var1, int var2, int var3) {
         return this.get(☃ - 1, ☃) == ☃ || this.get(☃ + 1, ☃) == ☃ || this.get(☃, ☃ + 1) == ☃ || this.get(☃, ☃ - 1) == ☃;
      }
   }

   static class ThirdFloor extends WoodlandMansionPieces.SecondFloor {
      private ThirdFloor() {
      }
   }
}
