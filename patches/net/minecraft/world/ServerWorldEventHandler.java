package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ServerWorldEventHandler implements IWorldEventListener {
   private final MinecraftServer server;
   private final WorldServer world;

   public ServerWorldEventHandler(MinecraftServer var1, WorldServer var2) {
      this.server = ☃;
      this.world = ☃;
   }

   @Override
   public void spawnParticle(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
   }

   @Override
   public void spawnParticle(
      int var1, boolean var2, boolean var3, double var4, double var6, double var8, double var10, double var12, double var14, int... var16
   ) {
   }

   @Override
   public void onEntityAdded(Entity var1) {
      this.world.getEntityTracker().track(☃);
      if (☃ instanceof EntityPlayerMP) {
         this.world.provider.onPlayerAdded((EntityPlayerMP)☃);
      }
   }

   @Override
   public void onEntityRemoved(Entity var1) {
      this.world.getEntityTracker().untrack(☃);
      this.world.getScoreboard().removeEntity(☃);
      if (☃ instanceof EntityPlayerMP) {
         this.world.provider.onPlayerRemoved((EntityPlayerMP)☃);
      }
   }

   @Override
   public void playSoundToAllNearExcept(
      @Nullable EntityPlayer var1, SoundEvent var2, SoundCategory var3, double var4, double var6, double var8, float var10, float var11
   ) {
      this.server
         .getPlayerList()
         .sendToAllNearExcept(
            ☃, ☃, ☃, ☃, ☃ > 1.0F ? 16.0F * ☃ : 16.0, this.world.provider.getDimensionType().getId(), new SPacketSoundEffect(☃, ☃, ☃, ☃, ☃, ☃, ☃)
         );
   }

   @Override
   public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   @Override
   public void notifyBlockUpdate(World var1, BlockPos var2, IBlockState var3, IBlockState var4, int var5) {
      this.world.getPlayerChunkMap().markBlockForUpdate(☃);
   }

   @Override
   public void notifyLightSet(BlockPos var1) {
   }

   @Override
   public void playRecord(SoundEvent var1, BlockPos var2) {
   }

   @Override
   public void playEvent(EntityPlayer var1, int var2, BlockPos var3, int var4) {
      this.server
         .getPlayerList()
         .sendToAllNearExcept(☃, ☃.getX(), ☃.getY(), ☃.getZ(), 64.0, this.world.provider.getDimensionType().getId(), new SPacketEffect(☃, ☃, ☃, false));
   }

   @Override
   public void broadcastSound(int var1, BlockPos var2, int var3) {
      this.server.getPlayerList().sendPacketToAllPlayers(new SPacketEffect(☃, ☃, ☃, true));
   }

   @Override
   public void sendBlockBreakProgress(int var1, BlockPos var2, int var3) {
      for (EntityPlayerMP ☃ : this.server.getPlayerList().getPlayers()) {
         if (☃ != null && ☃.world == this.world && ☃.getEntityId() != ☃) {
            double ☃x = ☃.getX() - ☃.posX;
            double ☃xx = ☃.getY() - ☃.posY;
            double ☃xxx = ☃.getZ() - ☃.posZ;
            if (☃x * ☃x + ☃xx * ☃xx + ☃xxx * ☃xxx < 1024.0) {
               ☃.connection.sendPacket(new SPacketBlockBreakAnim(☃, ☃, ☃));
            }
         }
      }
   }
}
