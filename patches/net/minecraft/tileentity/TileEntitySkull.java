package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import javax.annotation.Nullable;
import net.minecraft.block.BlockSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity implements ITickable {
   private int skullType;
   private int skullRotation;
   private GameProfile playerProfile;
   private int dragonAnimatedTicks;
   private boolean dragonAnimated;
   private static PlayerProfileCache profileCache;
   private static MinecraftSessionService sessionService;

   public static void setProfileCache(PlayerProfileCache var0) {
      profileCache = ☃;
   }

   public static void setSessionService(MinecraftSessionService var0) {
      sessionService = ☃;
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setByte("SkullType", (byte)(this.skullType & 0xFF));
      ☃.setByte("Rot", (byte)(this.skullRotation & 0xFF));
      if (this.playerProfile != null) {
         NBTTagCompound ☃ = new NBTTagCompound();
         NBTUtil.writeGameProfile(☃, this.playerProfile);
         ☃.setTag("Owner", ☃);
      }

      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.skullType = ☃.getByte("SkullType");
      this.skullRotation = ☃.getByte("Rot");
      if (this.skullType == 3) {
         if (☃.hasKey("Owner", 10)) {
            this.playerProfile = NBTUtil.readGameProfileFromNBT(☃.getCompoundTag("Owner"));
         } else if (☃.hasKey("ExtraType", 8)) {
            String ☃ = ☃.getString("ExtraType");
            if (!StringUtils.isNullOrEmpty(☃)) {
               this.playerProfile = new GameProfile(null, ☃);
               this.updatePlayerProfile();
            }
         }
      }
   }

   @Override
   public void update() {
      if (this.skullType == 5) {
         if (this.world.isBlockPowered(this.pos)) {
            this.dragonAnimated = true;
            this.dragonAnimatedTicks++;
         } else {
            this.dragonAnimated = false;
         }
      }
   }

   public float getAnimationProgress(float var1) {
      return this.dragonAnimated ? this.dragonAnimatedTicks + ☃ : this.dragonAnimatedTicks;
   }

   @Nullable
   public GameProfile getPlayerProfile() {
      return this.playerProfile;
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 4, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   public void setType(int var1) {
      this.skullType = ☃;
      this.playerProfile = null;
   }

   public void setPlayerProfile(@Nullable GameProfile var1) {
      this.skullType = 3;
      this.playerProfile = ☃;
      this.updatePlayerProfile();
   }

   private void updatePlayerProfile() {
      this.playerProfile = updateGameProfile(this.playerProfile);
      this.markDirty();
   }

   public static GameProfile updateGameProfile(GameProfile var0) {
      if (☃ != null && !StringUtils.isNullOrEmpty(☃.getName())) {
         if (☃.isComplete() && ☃.getProperties().containsKey("textures")) {
            return ☃;
         } else if (profileCache != null && sessionService != null) {
            GameProfile ☃ = profileCache.getGameProfileForUsername(☃.getName());
            if (☃ == null) {
               return ☃;
            } else {
               Property ☃x = (Property)Iterables.getFirst(☃.getProperties().get("textures"), null);
               if (☃x == null) {
                  ☃ = sessionService.fillProfileProperties(☃, true);
               }

               return ☃;
            }
         } else {
            return ☃;
         }
      } else {
         return ☃;
      }
   }

   public int getSkullType() {
      return this.skullType;
   }

   public int getSkullRotation() {
      return this.skullRotation;
   }

   public void setSkullRotation(int var1) {
      this.skullRotation = ☃;
   }

   @Override
   public void mirror(Mirror var1) {
      if (this.world != null && this.world.getBlockState(this.getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP) {
         this.skullRotation = ☃.mirrorRotation(this.skullRotation, 16);
      }
   }

   @Override
   public void rotate(Rotation var1) {
      if (this.world != null && this.world.getBlockState(this.getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP) {
         this.skullRotation = ☃.rotate(this.skullRotation, 16);
      }
   }
}
