package net.minecraft.entity.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartMobSpawner extends EntityMinecart {
   private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic() {
      @Override
      public void broadcastEvent(int var1) {
         EntityMinecartMobSpawner.this.world.setEntityState(EntityMinecartMobSpawner.this, (byte)☃);
      }

      @Override
      public World getSpawnerWorld() {
         return EntityMinecartMobSpawner.this.world;
      }

      @Override
      public BlockPos getSpawnerPosition() {
         return new BlockPos(EntityMinecartMobSpawner.this);
      }
   };

   public EntityMinecartMobSpawner(World var1) {
      super(☃);
   }

   public EntityMinecartMobSpawner(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesMinecartMobSpawner(DataFixer var0) {
      registerFixesMinecart(☃, EntityMinecartMobSpawner.class);
      ☃.registerWalker(FixTypes.ENTITY, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            String ☃ = ☃.getString("id");
            if (EntityList.getKey(EntityMinecartMobSpawner.class).equals(new ResourceLocation(☃))) {
               ☃.setString("id", TileEntity.getKey(TileEntityMobSpawner.class).toString());
               ☃.process(FixTypes.BLOCK_ENTITY, ☃, ☃);
               ☃.setString("id", ☃);
            }

            return ☃;
         }
      });
   }

   @Override
   public EntityMinecart.Type getType() {
      return EntityMinecart.Type.SPAWNER;
   }

   @Override
   public IBlockState getDefaultDisplayTile() {
      return Blocks.MOB_SPAWNER.getDefaultState();
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.mobSpawnerLogic.readFromNBT(☃);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      this.mobSpawnerLogic.writeToNBT(☃);
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      this.mobSpawnerLogic.setDelayToMin(☃);
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      this.mobSpawnerLogic.updateSpawner();
   }
}
