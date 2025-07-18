package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MapData extends WorldSavedData {
   public int xCenter;
   public int zCenter;
   public byte dimension;
   public boolean trackingPosition;
   public boolean unlimitedTracking;
   public byte scale;
   public byte[] colors = new byte[16384];
   public List<MapData.MapInfo> playersArrayList = Lists.newArrayList();
   private final Map<EntityPlayer, MapData.MapInfo> playersHashMap = Maps.newHashMap();
   public Map<String, MapDecoration> mapDecorations = Maps.newLinkedHashMap();

   public MapData(String var1) {
      super(☃);
   }

   public void calculateMapCenter(double var1, double var3, int var5) {
      int ☃ = 128 * (1 << ☃);
      int ☃x = MathHelper.floor((☃ + 64.0) / ☃);
      int ☃xx = MathHelper.floor((☃ + 64.0) / ☃);
      this.xCenter = ☃x * ☃ + ☃ / 2 - 64;
      this.zCenter = ☃xx * ☃ + ☃ / 2 - 64;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      this.dimension = ☃.getByte("dimension");
      this.xCenter = ☃.getInteger("xCenter");
      this.zCenter = ☃.getInteger("zCenter");
      this.scale = ☃.getByte("scale");
      this.scale = (byte)MathHelper.clamp(this.scale, 0, 4);
      if (☃.hasKey("trackingPosition", 1)) {
         this.trackingPosition = ☃.getBoolean("trackingPosition");
      } else {
         this.trackingPosition = true;
      }

      this.unlimitedTracking = ☃.getBoolean("unlimitedTracking");
      int ☃ = ☃.getShort("width");
      int ☃x = ☃.getShort("height");
      if (☃ == 128 && ☃x == 128) {
         this.colors = ☃.getByteArray("colors");
      } else {
         byte[] ☃xx = ☃.getByteArray("colors");
         this.colors = new byte[16384];
         int ☃xxx = (128 - ☃) / 2;
         int ☃xxxx = (128 - ☃x) / 2;

         for (int ☃xxxxx = 0; ☃xxxxx < ☃x; ☃xxxxx++) {
            int ☃xxxxxx = ☃xxxxx + ☃xxxx;
            if (☃xxxxxx >= 0 || ☃xxxxxx < 128) {
               for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃; ☃xxxxxxx++) {
                  int ☃xxxxxxxx = ☃xxxxxxx + ☃xxx;
                  if (☃xxxxxxxx >= 0 || ☃xxxxxxxx < 128) {
                     this.colors[☃xxxxxxxx + ☃xxxxxx * 128] = ☃xx[☃xxxxxxx + ☃xxxxx * ☃];
                  }
               }
            }
         }
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      ☃.setByte("dimension", this.dimension);
      ☃.setInteger("xCenter", this.xCenter);
      ☃.setInteger("zCenter", this.zCenter);
      ☃.setByte("scale", this.scale);
      ☃.setShort("width", (short)128);
      ☃.setShort("height", (short)128);
      ☃.setByteArray("colors", this.colors);
      ☃.setBoolean("trackingPosition", this.trackingPosition);
      ☃.setBoolean("unlimitedTracking", this.unlimitedTracking);
      return ☃;
   }

   public void updateVisiblePlayers(EntityPlayer var1, ItemStack var2) {
      if (!this.playersHashMap.containsKey(☃)) {
         MapData.MapInfo ☃ = new MapData.MapInfo(☃);
         this.playersHashMap.put(☃, ☃);
         this.playersArrayList.add(☃);
      }

      if (!☃.inventory.hasItemStack(☃)) {
         this.mapDecorations.remove(☃.getName());
      }

      for (int ☃ = 0; ☃ < this.playersArrayList.size(); ☃++) {
         MapData.MapInfo ☃x = this.playersArrayList.get(☃);
         if (!☃x.player.isDead && (☃x.player.inventory.hasItemStack(☃) || ☃.isOnItemFrame())) {
            if (!☃.isOnItemFrame() && ☃x.player.dimension == this.dimension && this.trackingPosition) {
               this.updateDecorations(MapDecoration.Type.PLAYER, ☃x.player.world, ☃x.player.getName(), ☃x.player.posX, ☃x.player.posZ, ☃x.player.rotationYaw);
            }
         } else {
            this.playersHashMap.remove(☃x.player);
            this.playersArrayList.remove(☃x);
         }
      }

      if (☃.isOnItemFrame() && this.trackingPosition) {
         EntityItemFrame ☃x = ☃.getItemFrame();
         BlockPos ☃xx = ☃x.getHangingPosition();
         this.updateDecorations(
            MapDecoration.Type.FRAME, ☃.world, "frame-" + ☃x.getEntityId(), ☃xx.getX(), ☃xx.getZ(), ☃x.facingDirection.getHorizontalIndex() * 90
         );
      }

      if (☃.hasTagCompound() && ☃.getTagCompound().hasKey("Decorations", 9)) {
         NBTTagList ☃x = ☃.getTagCompound().getTagList("Decorations", 10);

         for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
            NBTTagCompound ☃xxx = ☃x.getCompoundTagAt(☃xx);
            if (!this.mapDecorations.containsKey(☃xxx.getString("id"))) {
               this.updateDecorations(
                  MapDecoration.Type.byIcon(☃xxx.getByte("type")),
                  ☃.world,
                  ☃xxx.getString("id"),
                  ☃xxx.getDouble("x"),
                  ☃xxx.getDouble("z"),
                  ☃xxx.getDouble("rot")
               );
            }
         }
      }
   }

   public static void addTargetDecoration(ItemStack var0, BlockPos var1, String var2, MapDecoration.Type var3) {
      NBTTagList ☃;
      if (☃.hasTagCompound() && ☃.getTagCompound().hasKey("Decorations", 9)) {
         ☃ = ☃.getTagCompound().getTagList("Decorations", 10);
      } else {
         ☃ = new NBTTagList();
         ☃.setTagInfo("Decorations", ☃);
      }

      NBTTagCompound ☃x = new NBTTagCompound();
      ☃x.setByte("type", ☃.getIcon());
      ☃x.setString("id", ☃);
      ☃x.setDouble("x", ☃.getX());
      ☃x.setDouble("z", ☃.getZ());
      ☃x.setDouble("rot", 180.0);
      ☃.appendTag(☃x);
      if (☃.hasMapColor()) {
         NBTTagCompound ☃xx = ☃.getOrCreateSubCompound("display");
         ☃xx.setInteger("MapColor", ☃.getMapColor());
      }
   }

   private void updateDecorations(MapDecoration.Type var1, World var2, String var3, double var4, double var6, double var8) {
      int ☃ = 1 << this.scale;
      float ☃x = (float)(☃ - this.xCenter) / ☃;
      float ☃xx = (float)(☃ - this.zCenter) / ☃;
      byte ☃xxx = (byte)(☃x * 2.0F + 0.5);
      byte ☃xxxx = (byte)(☃xx * 2.0F + 0.5);
      int ☃xxxxx = 63;
      byte ☃xxxxxx;
      if (☃x >= -63.0F && ☃xx >= -63.0F && ☃x <= 63.0F && ☃xx <= 63.0F) {
         ☃ += ☃ < 0.0 ? -8.0 : 8.0;
         ☃xxxxxx = (byte)(☃ * 16.0 / 360.0);
         if (this.dimension < 0) {
            int ☃xxxxxxx = (int)(☃.getWorldInfo().getWorldTime() / 10L);
            ☃xxxxxx = (byte)(☃xxxxxxx * ☃xxxxxxx * 34187121 + ☃xxxxxxx * 121 >> 15 & 15);
         }
      } else {
         if (☃ != MapDecoration.Type.PLAYER) {
            this.mapDecorations.remove(☃);
            return;
         }

         int ☃xxxxxxx = 320;
         if (Math.abs(☃x) < 320.0F && Math.abs(☃xx) < 320.0F) {
            ☃ = MapDecoration.Type.PLAYER_OFF_MAP;
         } else {
            if (!this.unlimitedTracking) {
               this.mapDecorations.remove(☃);
               return;
            }

            ☃ = MapDecoration.Type.PLAYER_OFF_LIMITS;
         }

         ☃xxxxxx = 0;
         if (☃x <= -63.0F) {
            ☃xxx = -128;
         }

         if (☃xx <= -63.0F) {
            ☃xxxx = -128;
         }

         if (☃x >= 63.0F) {
            ☃xxx = 127;
         }

         if (☃xx >= 63.0F) {
            ☃xxxx = 127;
         }
      }

      this.mapDecorations.put(☃, new MapDecoration(☃, ☃xxx, ☃xxxx, ☃xxxxxx));
   }

   @Nullable
   public Packet<?> getMapPacket(ItemStack var1, World var2, EntityPlayer var3) {
      MapData.MapInfo ☃ = this.playersHashMap.get(☃);
      return ☃ == null ? null : ☃.getPacket(☃);
   }

   public void updateMapData(int var1, int var2) {
      super.markDirty();

      for (MapData.MapInfo ☃ : this.playersArrayList) {
         ☃.update(☃, ☃);
      }
   }

   public MapData.MapInfo getMapInfo(EntityPlayer var1) {
      MapData.MapInfo ☃ = this.playersHashMap.get(☃);
      if (☃ == null) {
         ☃ = new MapData.MapInfo(☃);
         this.playersHashMap.put(☃, ☃);
         this.playersArrayList.add(☃);
      }

      return ☃;
   }

   public class MapInfo {
      public final EntityPlayer player;
      private boolean isDirty = true;
      private int minX;
      private int minY;
      private int maxX = 127;
      private int maxY = 127;
      private int tick;
      public int step;

      public MapInfo(EntityPlayer var2) {
         this.player = ☃;
      }

      @Nullable
      public Packet<?> getPacket(ItemStack var1) {
         if (this.isDirty) {
            this.isDirty = false;
            return new SPacketMaps(
               ☃.getMetadata(),
               MapData.this.scale,
               MapData.this.trackingPosition,
               MapData.this.mapDecorations.values(),
               MapData.this.colors,
               this.minX,
               this.minY,
               this.maxX + 1 - this.minX,
               this.maxY + 1 - this.minY
            );
         } else {
            return this.tick++ % 5 == 0
               ? new SPacketMaps(
                  ☃.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0
               )
               : null;
         }
      }

      public void update(int var1, int var2) {
         if (this.isDirty) {
            this.minX = Math.min(this.minX, ☃);
            this.minY = Math.min(this.minY, ☃);
            this.maxX = Math.max(this.maxX, ☃);
            this.maxY = Math.max(this.maxY, ☃);
         } else {
            this.isDirty = true;
            this.minX = ☃;
            this.minY = ☃;
            this.maxX = ☃;
            this.maxY = ☃;
         }
      }
   }
}
