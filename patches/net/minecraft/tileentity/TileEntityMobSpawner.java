package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity implements ITickable {
   private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic() {
      @Override
      public void broadcastEvent(int var1) {
         TileEntityMobSpawner.this.world.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.MOB_SPAWNER, ☃, 0);
      }

      @Override
      public World getSpawnerWorld() {
         return TileEntityMobSpawner.this.world;
      }

      @Override
      public BlockPos getSpawnerPosition() {
         return TileEntityMobSpawner.this.pos;
      }

      @Override
      public void setNextSpawnData(WeightedSpawnerEntity var1) {
         super.setNextSpawnData(☃);
         if (this.getSpawnerWorld() != null) {
            IBlockState ☃ = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
            this.getSpawnerWorld().notifyBlockUpdate(TileEntityMobSpawner.this.pos, ☃, ☃, 4);
         }
      }
   };

   public static void registerFixesMobSpawner(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (TileEntity.getKey(TileEntityMobSpawner.class).equals(new ResourceLocation(☃.getString("id")))) {
               if (☃.hasKey("SpawnPotentials", 9)) {
                  NBTTagList ☃ = ☃.getTagList("SpawnPotentials", 10);

                  for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
                     NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
                     ☃xx.setTag("Entity", ☃.process(FixTypes.ENTITY, ☃xx.getCompoundTag("Entity"), ☃));
                  }
               }

               ☃.setTag("SpawnData", ☃.process(FixTypes.ENTITY, ☃.getCompoundTag("SpawnData"), ☃));
            }

            return ☃;
         }
      });
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.spawnerLogic.readFromNBT(☃);
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      this.spawnerLogic.writeToNBT(☃);
      return ☃;
   }

   @Override
   public void update() {
      this.spawnerLogic.updateSpawner();
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      NBTTagCompound ☃ = this.writeToNBT(new NBTTagCompound());
      ☃.removeTag("SpawnPotentials");
      return ☃;
   }

   @Override
   public boolean receiveClientEvent(int var1, int var2) {
      return this.spawnerLogic.setDelayToMin(☃) ? true : super.receiveClientEvent(☃, ☃);
   }

   @Override
   public boolean onlyOpsCanSetNbt() {
      return true;
   }

   public MobSpawnerBaseLogic getSpawnerBaseLogic() {
      return this.spawnerLogic;
   }
}
