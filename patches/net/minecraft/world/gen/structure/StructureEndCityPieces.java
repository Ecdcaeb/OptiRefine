package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureEndCityPieces {
   private static final PlacementSettings OVERWRITE = new PlacementSettings().setIgnoreEntities(true);
   private static final PlacementSettings INSERT = new PlacementSettings().setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
   private static final StructureEndCityPieces.IGenerator HOUSE_TOWER_GENERATOR = new StructureEndCityPieces.IGenerator() {
      @Override
      public void init() {
      }

      @Override
      public boolean generate(
         TemplateManager var1, int var2, StructureEndCityPieces.CityTemplate var3, BlockPos var4, List<StructureComponent> var5, Random var6
      ) {
         if (☃ > 8) {
            return false;
         } else {
            Rotation ☃ = ☃.placeSettings.getRotation();
            StructureEndCityPieces.CityTemplate ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃, ☃, "base_floor", ☃, true));
            int ☃xx = ☃.nextInt(3);
            if (☃xx == 0) {
               ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(-1, 4, -1), "base_roof", ☃, true));
            } else if (☃xx == 1) {
               ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(-1, 0, -1), "second_floor_2", ☃, false));
               ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(-1, 8, -1), "second_roof", ☃, false));
               StructureEndCityPieces.recursiveChildren(☃, StructureEndCityPieces.TOWER_GENERATOR, ☃ + 1, ☃x, null, ☃, ☃);
            } else if (☃xx == 2) {
               ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(-1, 0, -1), "second_floor_2", ☃, false));
               ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(-1, 4, -1), "third_floor_c", ☃, false));
               ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(-1, 8, -1), "third_roof", ☃, true));
               StructureEndCityPieces.recursiveChildren(☃, StructureEndCityPieces.TOWER_GENERATOR, ☃ + 1, ☃x, null, ☃, ☃);
            }

            return true;
         }
      }
   };
   private static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES = Lists.newArrayList(
      new Tuple[]{
         new Tuple<>(Rotation.NONE, new BlockPos(1, -1, 0)),
         new Tuple<>(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)),
         new Tuple<>(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)),
         new Tuple<>(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6))
      }
   );
   private static final StructureEndCityPieces.IGenerator TOWER_GENERATOR = new StructureEndCityPieces.IGenerator() {
      @Override
      public void init() {
      }

      @Override
      public boolean generate(
         TemplateManager var1, int var2, StructureEndCityPieces.CityTemplate var3, BlockPos var4, List<StructureComponent> var5, Random var6
      ) {
         Rotation ☃ = ☃.placeSettings.getRotation();
         StructureEndCityPieces.CityTemplate var8 = StructureEndCityPieces.addHelper(
            ☃, StructureEndCityPieces.addPiece(☃, ☃, new BlockPos(3 + ☃.nextInt(2), -3, 3 + ☃.nextInt(2)), "tower_base", ☃, true)
         );
         var8 = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, var8, new BlockPos(0, 7, 0), "tower_piece", ☃, true));
         StructureEndCityPieces.CityTemplate ☃x = ☃.nextInt(3) == 0 ? var8 : null;
         int ☃xx = 1 + ☃.nextInt(3);

         for (int ☃xxx = 0; ☃xxx < ☃xx; ☃xxx++) {
            var8 = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, var8, new BlockPos(0, 4, 0), "tower_piece", ☃, true));
            if (☃xxx < ☃xx - 1 && ☃.nextBoolean()) {
               ☃x = var8;
            }
         }

         if (☃x != null) {
            for (Tuple<Rotation, BlockPos> ☃xxxx : StructureEndCityPieces.TOWER_BRIDGES) {
               if (☃.nextBoolean()) {
                  StructureEndCityPieces.CityTemplate ☃xxxxx = StructureEndCityPieces.addHelper(
                     ☃, StructureEndCityPieces.addPiece(☃, ☃x, ☃xxxx.getSecond(), "bridge_end", ☃.add(☃xxxx.getFirst()), true)
                  );
                  StructureEndCityPieces.recursiveChildren(☃, StructureEndCityPieces.TOWER_BRIDGE_GENERATOR, ☃ + 1, ☃xxxxx, null, ☃, ☃);
               }
            }

            var8 = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, var8, new BlockPos(-1, 4, -1), "tower_top", ☃, true));
         } else {
            if (☃ != 7) {
               return StructureEndCityPieces.recursiveChildren(☃, StructureEndCityPieces.FAT_TOWER_GENERATOR, ☃ + 1, var8, null, ☃, ☃);
            }

            var8 = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, var8, new BlockPos(-1, 4, -1), "tower_top", ☃, true));
         }

         return true;
      }
   };
   private static final StructureEndCityPieces.IGenerator TOWER_BRIDGE_GENERATOR = new StructureEndCityPieces.IGenerator() {
      public boolean shipCreated;

      @Override
      public void init() {
         this.shipCreated = false;
      }

      @Override
      public boolean generate(
         TemplateManager var1, int var2, StructureEndCityPieces.CityTemplate var3, BlockPos var4, List<StructureComponent> var5, Random var6
      ) {
         Rotation ☃ = ☃.placeSettings.getRotation();
         int ☃x = ☃.nextInt(4) + 1;
         StructureEndCityPieces.CityTemplate ☃xx = StructureEndCityPieces.addHelper(
            ☃, StructureEndCityPieces.addPiece(☃, ☃, new BlockPos(0, 0, -4), "bridge_piece", ☃, true)
         );
         ☃xx.componentType = -1;
         int ☃xxx = 0;

         for (int ☃xxxx = 0; ☃xxxx < ☃x; ☃xxxx++) {
            if (☃.nextBoolean()) {
               ☃xx = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃xx, new BlockPos(0, ☃xxx, -4), "bridge_piece", ☃, true));
               ☃xxx = 0;
            } else {
               if (☃.nextBoolean()) {
                  ☃xx = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃xx, new BlockPos(0, ☃xxx, -4), "bridge_steep_stairs", ☃, true));
               } else {
                  ☃xx = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃xx, new BlockPos(0, ☃xxx, -8), "bridge_gentle_stairs", ☃, true));
               }

               ☃xxx = 4;
            }
         }

         if (!this.shipCreated && ☃.nextInt(10 - ☃) == 0) {
            StructureEndCityPieces.addHelper(
               ☃, StructureEndCityPieces.addPiece(☃, ☃xx, new BlockPos(-8 + ☃.nextInt(8), ☃xxx, -70 + ☃.nextInt(10)), "ship", ☃, true)
            );
            this.shipCreated = true;
         } else if (!StructureEndCityPieces.recursiveChildren(
            ☃, StructureEndCityPieces.HOUSE_TOWER_GENERATOR, ☃ + 1, ☃xx, new BlockPos(-3, ☃xxx + 1, -11), ☃, ☃
         )) {
            return false;
         }

         ☃xx = StructureEndCityPieces.addHelper(
            ☃, StructureEndCityPieces.addPiece(☃, ☃xx, new BlockPos(4, ☃xxx, 0), "bridge_end", ☃.add(Rotation.CLOCKWISE_180), true)
         );
         ☃xx.componentType = -1;
         return true;
      }
   };
   private static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES = Lists.newArrayList(
      new Tuple[]{
         new Tuple<>(Rotation.NONE, new BlockPos(4, -1, 0)),
         new Tuple<>(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)),
         new Tuple<>(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)),
         new Tuple<>(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12))
      }
   );
   private static final StructureEndCityPieces.IGenerator FAT_TOWER_GENERATOR = new StructureEndCityPieces.IGenerator() {
      @Override
      public void init() {
      }

      @Override
      public boolean generate(
         TemplateManager var1, int var2, StructureEndCityPieces.CityTemplate var3, BlockPos var4, List<StructureComponent> var5, Random var6
      ) {
         Rotation ☃ = ☃.placeSettings.getRotation();
         StructureEndCityPieces.CityTemplate ☃x = StructureEndCityPieces.addHelper(
            ☃, StructureEndCityPieces.addPiece(☃, ☃, new BlockPos(-3, 4, -3), "fat_tower_base", ☃, true)
         );
         ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(0, 4, 0), "fat_tower_middle", ☃, true));

         for (int ☃xx = 0; ☃xx < 2 && ☃.nextInt(3) != 0; ☃xx++) {
            ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(0, 8, 0), "fat_tower_middle", ☃, true));

            for (Tuple<Rotation, BlockPos> ☃xxx : StructureEndCityPieces.FAT_TOWER_BRIDGES) {
               if (☃.nextBoolean()) {
                  StructureEndCityPieces.CityTemplate ☃xxxx = StructureEndCityPieces.addHelper(
                     ☃, StructureEndCityPieces.addPiece(☃, ☃x, ☃xxx.getSecond(), "bridge_end", ☃.add(☃xxx.getFirst()), true)
                  );
                  StructureEndCityPieces.recursiveChildren(☃, StructureEndCityPieces.TOWER_BRIDGE_GENERATOR, ☃ + 1, ☃xxxx, null, ☃, ☃);
               }
            }
         }

         ☃x = StructureEndCityPieces.addHelper(☃, StructureEndCityPieces.addPiece(☃, ☃x, new BlockPos(-2, 8, -2), "fat_tower_top", ☃, true));
         return true;
      }
   };

   public static void registerPieces() {
      MapGenStructureIO.registerStructureComponent(StructureEndCityPieces.CityTemplate.class, "ECP");
   }

   private static StructureEndCityPieces.CityTemplate addPiece(
      TemplateManager var0, StructureEndCityPieces.CityTemplate var1, BlockPos var2, String var3, Rotation var4, boolean var5
   ) {
      StructureEndCityPieces.CityTemplate ☃ = new StructureEndCityPieces.CityTemplate(☃, ☃, ☃.templatePosition, ☃, ☃);
      BlockPos ☃x = ☃.template.calculateConnectedPos(☃.placeSettings, ☃, ☃.placeSettings, BlockPos.ORIGIN);
      ☃.offset(☃x.getX(), ☃x.getY(), ☃x.getZ());
      return ☃;
   }

   public static void startHouseTower(TemplateManager var0, BlockPos var1, Rotation var2, List<StructureComponent> var3, Random var4) {
      FAT_TOWER_GENERATOR.init();
      HOUSE_TOWER_GENERATOR.init();
      TOWER_BRIDGE_GENERATOR.init();
      TOWER_GENERATOR.init();
      StructureEndCityPieces.CityTemplate ☃ = addHelper(☃, new StructureEndCityPieces.CityTemplate(☃, "base_floor", ☃, ☃, true));
      ☃ = addHelper(☃, addPiece(☃, ☃, new BlockPos(-1, 0, -1), "second_floor", ☃, false));
      ☃ = addHelper(☃, addPiece(☃, ☃, new BlockPos(-1, 4, -1), "third_floor", ☃, false));
      ☃ = addHelper(☃, addPiece(☃, ☃, new BlockPos(-1, 8, -1), "third_roof", ☃, true));
      recursiveChildren(☃, TOWER_GENERATOR, 1, ☃, null, ☃, ☃);
   }

   private static StructureEndCityPieces.CityTemplate addHelper(List<StructureComponent> var0, StructureEndCityPieces.CityTemplate var1) {
      ☃.add(☃);
      return ☃;
   }

   private static boolean recursiveChildren(
      TemplateManager var0,
      StructureEndCityPieces.IGenerator var1,
      int var2,
      StructureEndCityPieces.CityTemplate var3,
      BlockPos var4,
      List<StructureComponent> var5,
      Random var6
   ) {
      if (☃ > 8) {
         return false;
      } else {
         List<StructureComponent> ☃ = Lists.newArrayList();
         if (☃.generate(☃, ☃, ☃, ☃, ☃, ☃)) {
            boolean ☃x = false;
            int ☃xx = ☃.nextInt();

            for (StructureComponent ☃xxx : ☃) {
               ☃xxx.componentType = ☃xx;
               StructureComponent ☃xxxx = StructureComponent.findIntersecting(☃, ☃xxx.getBoundingBox());
               if (☃xxxx != null && ☃xxxx.componentType != ☃.componentType) {
                  ☃x = true;
                  break;
               }
            }

            if (!☃x) {
               ☃.addAll(☃);
               return true;
            }
         }

         return false;
      }
   }

   public static class CityTemplate extends StructureComponentTemplate {
      private String pieceName;
      private Rotation rotation;
      private boolean overwrite;

      public CityTemplate() {
      }

      public CityTemplate(TemplateManager var1, String var2, BlockPos var3, Rotation var4, boolean var5) {
         super(0);
         this.pieceName = ☃;
         this.templatePosition = ☃;
         this.rotation = ☃;
         this.overwrite = ☃;
         this.loadTemplate(☃);
      }

      private void loadTemplate(TemplateManager var1) {
         Template ☃ = ☃.getTemplate(null, new ResourceLocation("endcity/" + this.pieceName));
         PlacementSettings ☃x = (this.overwrite ? StructureEndCityPieces.OVERWRITE : StructureEndCityPieces.INSERT).copy().setRotation(this.rotation);
         this.setup(☃, this.templatePosition, ☃x);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setString("Template", this.pieceName);
         ☃.setString("Rot", this.rotation.name());
         ☃.setBoolean("OW", this.overwrite);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.pieceName = ☃.getString("Template");
         this.rotation = Rotation.valueOf(☃.getString("Rot"));
         this.overwrite = ☃.getBoolean("OW");
         this.loadTemplate(☃);
      }

      @Override
      protected void handleDataMarker(String var1, BlockPos var2, World var3, Random var4, StructureBoundingBox var5) {
         if (☃.startsWith("Chest")) {
            BlockPos ☃ = ☃.down();
            if (☃.isVecInside(☃)) {
               TileEntity ☃x = ☃.getTileEntity(☃);
               if (☃x instanceof TileEntityChest) {
                  ((TileEntityChest)☃x).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, ☃.nextLong());
               }
            }
         } else if (☃.startsWith("Sentry")) {
            EntityShulker ☃ = new EntityShulker(☃);
            ☃.setPosition(☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5);
            ☃.setAttachmentPos(☃);
            ☃.spawnEntity(☃);
         } else if (☃.startsWith("Elytra")) {
            EntityItemFrame ☃ = new EntityItemFrame(☃, ☃, this.rotation.rotate(EnumFacing.SOUTH));
            ☃.setDisplayedItem(new ItemStack(Items.ELYTRA));
            ☃.spawnEntity(☃);
         }
      }
   }

   interface IGenerator {
      void init();

      boolean generate(TemplateManager var1, int var2, StructureEndCityPieces.CityTemplate var3, BlockPos var4, List<StructureComponent> var5, Random var6);
   }
}
