package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponentTemplate extends StructureComponent {
   private static final PlacementSettings DEFAULT_PLACE_SETTINGS = new PlacementSettings();
   protected Template template;
   protected PlacementSettings placeSettings = DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
   protected BlockPos templatePosition;

   public StructureComponentTemplate() {
   }

   public StructureComponentTemplate(int var1) {
      super(☃);
   }

   protected void setup(Template var1, BlockPos var2, PlacementSettings var3) {
      this.template = ☃;
      this.setCoordBaseMode(EnumFacing.NORTH);
      this.templatePosition = ☃;
      this.placeSettings = ☃;
      this.setBoundingBoxFromTemplate();
   }

   @Override
   protected void writeStructureToNBT(NBTTagCompound var1) {
      ☃.setInteger("TPX", this.templatePosition.getX());
      ☃.setInteger("TPY", this.templatePosition.getY());
      ☃.setInteger("TPZ", this.templatePosition.getZ());
   }

   @Override
   protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
      this.templatePosition = new BlockPos(☃.getInteger("TPX"), ☃.getInteger("TPY"), ☃.getInteger("TPZ"));
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      this.placeSettings.setBoundingBox(☃);
      this.template.addBlocksToWorld(☃, this.templatePosition, this.placeSettings, 18);
      Map<BlockPos, String> ☃ = this.template.getDataBlocks(this.templatePosition, this.placeSettings);

      for (Entry<BlockPos, String> ☃x : ☃.entrySet()) {
         String ☃xx = ☃x.getValue();
         this.handleDataMarker(☃xx, ☃x.getKey(), ☃, ☃, ☃);
      }

      return true;
   }

   protected abstract void handleDataMarker(String var1, BlockPos var2, World var3, Random var4, StructureBoundingBox var5);

   private void setBoundingBoxFromTemplate() {
      Rotation ☃ = this.placeSettings.getRotation();
      BlockPos ☃x = this.template.transformedSize(☃);
      Mirror ☃xx = this.placeSettings.getMirror();
      this.boundingBox = new StructureBoundingBox(0, 0, 0, ☃x.getX(), ☃x.getY() - 1, ☃x.getZ());
      switch (☃) {
         case NONE:
         default:
            break;
         case CLOCKWISE_90:
            this.boundingBox.offset(-☃x.getX(), 0, 0);
            break;
         case COUNTERCLOCKWISE_90:
            this.boundingBox.offset(0, 0, -☃x.getZ());
            break;
         case CLOCKWISE_180:
            this.boundingBox.offset(-☃x.getX(), 0, -☃x.getZ());
      }

      switch (☃xx) {
         case NONE:
         default:
            break;
         case FRONT_BACK:
            BlockPos ☃ = BlockPos.ORIGIN;
            if (☃ == Rotation.CLOCKWISE_90 || ☃ == Rotation.COUNTERCLOCKWISE_90) {
               ☃ = ☃.offset(☃.rotate(EnumFacing.WEST), ☃x.getZ());
            } else if (☃ == Rotation.CLOCKWISE_180) {
               ☃ = ☃.offset(EnumFacing.EAST, ☃x.getX());
            } else {
               ☃ = ☃.offset(EnumFacing.WEST, ☃x.getX());
            }

            this.boundingBox.offset(☃.getX(), 0, ☃.getZ());
            break;
         case LEFT_RIGHT:
            BlockPos ☃ = BlockPos.ORIGIN;
            if (☃ == Rotation.CLOCKWISE_90 || ☃ == Rotation.COUNTERCLOCKWISE_90) {
               ☃ = ☃.offset(☃.rotate(EnumFacing.NORTH), ☃x.getX());
            } else if (☃ == Rotation.CLOCKWISE_180) {
               ☃ = ☃.offset(EnumFacing.SOUTH, ☃x.getZ());
            } else {
               ☃ = ☃.offset(EnumFacing.NORTH, ☃x.getZ());
            }

            this.boundingBox.offset(☃.getX(), 0, ☃.getZ());
      }

      this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());
   }

   @Override
   public void offset(int var1, int var2, int var3) {
      super.offset(☃, ☃, ☃);
      this.templatePosition = this.templatePosition.add(☃, ☃, ☃);
   }
}
