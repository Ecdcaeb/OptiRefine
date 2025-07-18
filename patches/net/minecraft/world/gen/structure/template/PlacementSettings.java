package net.minecraft.world.gen.structure.template;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class PlacementSettings {
   private Mirror mirror = Mirror.NONE;
   private Rotation rotation = Rotation.NONE;
   private boolean ignoreEntities;
   @Nullable
   private Block replacedBlock;
   @Nullable
   private ChunkPos chunk;
   @Nullable
   private StructureBoundingBox boundingBox;
   private boolean ignoreStructureBlock = true;
   private float integrity = 1.0F;
   @Nullable
   private Random random;
   @Nullable
   private Long setSeed;

   public PlacementSettings copy() {
      PlacementSettings ☃ = new PlacementSettings();
      ☃.mirror = this.mirror;
      ☃.rotation = this.rotation;
      ☃.ignoreEntities = this.ignoreEntities;
      ☃.replacedBlock = this.replacedBlock;
      ☃.chunk = this.chunk;
      ☃.boundingBox = this.boundingBox;
      ☃.ignoreStructureBlock = this.ignoreStructureBlock;
      ☃.integrity = this.integrity;
      ☃.random = this.random;
      ☃.setSeed = this.setSeed;
      return ☃;
   }

   public PlacementSettings setMirror(Mirror var1) {
      this.mirror = ☃;
      return this;
   }

   public PlacementSettings setRotation(Rotation var1) {
      this.rotation = ☃;
      return this;
   }

   public PlacementSettings setIgnoreEntities(boolean var1) {
      this.ignoreEntities = ☃;
      return this;
   }

   public PlacementSettings setReplacedBlock(Block var1) {
      this.replacedBlock = ☃;
      return this;
   }

   public PlacementSettings setChunk(ChunkPos var1) {
      this.chunk = ☃;
      return this;
   }

   public PlacementSettings setBoundingBox(StructureBoundingBox var1) {
      this.boundingBox = ☃;
      return this;
   }

   public PlacementSettings setSeed(@Nullable Long var1) {
      this.setSeed = ☃;
      return this;
   }

   public PlacementSettings setRandom(@Nullable Random var1) {
      this.random = ☃;
      return this;
   }

   public PlacementSettings setIntegrity(float var1) {
      this.integrity = ☃;
      return this;
   }

   public Mirror getMirror() {
      return this.mirror;
   }

   public PlacementSettings setIgnoreStructureBlock(boolean var1) {
      this.ignoreStructureBlock = ☃;
      return this;
   }

   public Rotation getRotation() {
      return this.rotation;
   }

   public Random getRandom(@Nullable BlockPos var1) {
      if (this.random != null) {
         return this.random;
      } else if (this.setSeed != null) {
         return this.setSeed == 0L ? new Random(System.currentTimeMillis()) : new Random(this.setSeed);
      } else if (☃ == null) {
         return new Random(System.currentTimeMillis());
      } else {
         int ☃ = ☃.getX();
         int ☃x = ☃.getZ();
         return new Random(☃ * ☃ * 4987142 + ☃ * 5947611 + ☃x * ☃x * 4392871L + ☃x * 389711 ^ 987234911L);
      }
   }

   public float getIntegrity() {
      return this.integrity;
   }

   public boolean getIgnoreEntities() {
      return this.ignoreEntities;
   }

   @Nullable
   public Block getReplacedBlock() {
      return this.replacedBlock;
   }

   @Nullable
   public StructureBoundingBox getBoundingBox() {
      if (this.boundingBox == null && this.chunk != null) {
         this.setBoundingBoxFromChunk();
      }

      return this.boundingBox;
   }

   public boolean getIgnoreStructureBlock() {
      return this.ignoreStructureBlock;
   }

   void setBoundingBoxFromChunk() {
      this.boundingBox = this.getBoundingBoxFromChunk(this.chunk);
   }

   @Nullable
   private StructureBoundingBox getBoundingBoxFromChunk(@Nullable ChunkPos var1) {
      if (☃ == null) {
         return null;
      } else {
         int ☃ = ☃.x * 16;
         int ☃x = ☃.z * 16;
         return new StructureBoundingBox(☃, 0, ☃x, ☃ + 16 - 1, 255, ☃x + 16 - 1);
      }
   }
}
