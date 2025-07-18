package net.minecraft.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

public class ItemMap extends ItemMapBase {
   protected ItemMap() {
      this.setHasSubtypes(true);
   }

   public static ItemStack setupNewMap(World var0, double var1, double var3, byte var5, boolean var6, boolean var7) {
      ItemStack ☃ = new ItemStack(Items.FILLED_MAP, 1, ☃.getUniqueDataId("map"));
      String ☃x = "map_" + ☃.getMetadata();
      MapData ☃xx = new MapData(☃x);
      ☃.setData(☃x, ☃xx);
      ☃xx.scale = ☃;
      ☃xx.calculateMapCenter(☃, ☃, ☃xx.scale);
      ☃xx.dimension = (byte)☃.provider.getDimensionType().getId();
      ☃xx.trackingPosition = ☃;
      ☃xx.unlimitedTracking = ☃;
      ☃xx.markDirty();
      return ☃;
   }

   @Nullable
   public static MapData loadMapData(int var0, World var1) {
      String ☃ = "map_" + ☃;
      return (MapData)☃.loadData(MapData.class, ☃);
   }

   @Nullable
   public MapData getMapData(ItemStack var1, World var2) {
      String ☃ = "map_" + ☃.getMetadata();
      MapData ☃x = (MapData)☃.loadData(MapData.class, ☃);
      if (☃x == null && !☃.isRemote) {
         ☃.setItemDamage(☃.getUniqueDataId("map"));
         ☃ = "map_" + ☃.getMetadata();
         ☃x = new MapData(☃);
         ☃x.scale = 3;
         ☃x.calculateMapCenter(☃.getWorldInfo().getSpawnX(), ☃.getWorldInfo().getSpawnZ(), ☃x.scale);
         ☃x.dimension = (byte)☃.provider.getDimensionType().getId();
         ☃x.markDirty();
         ☃.setData(☃, ☃x);
      }

      return ☃x;
   }

   public void updateMapData(World var1, Entity var2, MapData var3) {
      if (☃.provider.getDimensionType().getId() == ☃.dimension && ☃ instanceof EntityPlayer) {
         int ☃ = 1 << ☃.scale;
         int ☃x = ☃.xCenter;
         int ☃xx = ☃.zCenter;
         int ☃xxx = MathHelper.floor(☃.posX - ☃x) / ☃ + 64;
         int ☃xxxx = MathHelper.floor(☃.posZ - ☃xx) / ☃ + 64;
         int ☃xxxxx = 128 / ☃;
         if (☃.provider.isNether()) {
            ☃xxxxx /= 2;
         }

         MapData.MapInfo ☃xxxxxx = ☃.getMapInfo((EntityPlayer)☃);
         ☃xxxxxx.step++;
         boolean ☃xxxxxxx = false;

         for (int ☃xxxxxxxx = ☃xxx - ☃xxxxx + 1; ☃xxxxxxxx < ☃xxx + ☃xxxxx; ☃xxxxxxxx++) {
            if ((☃xxxxxxxx & 15) == (☃xxxxxx.step & 15) || ☃xxxxxxx) {
               ☃xxxxxxx = false;
               double ☃xxxxxxxxx = 0.0;

               for (int ☃xxxxxxxxxx = ☃xxxx - ☃xxxxx - 1; ☃xxxxxxxxxx < ☃xxxx + ☃xxxxx; ☃xxxxxxxxxx++) {
                  if (☃xxxxxxxx >= 0 && ☃xxxxxxxxxx >= -1 && ☃xxxxxxxx < 128 && ☃xxxxxxxxxx < 128) {
                     int ☃xxxxxxxxxxx = ☃xxxxxxxx - ☃xxx;
                     int ☃xxxxxxxxxxxx = ☃xxxxxxxxxx - ☃xxxx;
                     boolean ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx > (☃xxxxx - 2) * (☃xxxxx - 2);
                     int ☃xxxxxxxxxxxxxx = (☃x / ☃ + ☃xxxxxxxx - 64) * ☃;
                     int ☃xxxxxxxxxxxxxxx = (☃xx / ☃ + ☃xxxxxxxxxx - 64) * ☃;
                     Multiset<MapColor> ☃xxxxxxxxxxxxxxxx = HashMultiset.create();
                     Chunk ☃xxxxxxxxxxxxxxxxx = ☃.getChunk(new BlockPos(☃xxxxxxxxxxxxxx, 0, ☃xxxxxxxxxxxxxxx));
                     if (!☃xxxxxxxxxxxxxxxxx.isEmpty()) {
                        int ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx & 15;
                        int ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx & 15;
                        int ☃xxxxxxxxxxxxxxxxxxxx = 0;
                        double ☃xxxxxxxxxxxxxxxxxxxxx = 0.0;
                        if (☃.provider.isNether()) {
                           int ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxx * 231871;
                           ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx * 31287121 + ☃xxxxxxxxxxxxxxxxxxxxxx * 11;
                           if ((☃xxxxxxxxxxxxxxxxxxxxxx >> 20 & 1) == 0) {
                              ☃xxxxxxxxxxxxxxxx.add(
                                 Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMapColor(☃, BlockPos.ORIGIN), 10
                              );
                           } else {
                              ☃xxxxxxxxxxxxxxxx.add(
                                 Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMapColor(☃, BlockPos.ORIGIN),
                                 100
                              );
                           }

                           ☃xxxxxxxxxxxxxxxxxxxxx = 100.0;
                        } else {
                           BlockPos.MutableBlockPos ☃xxxxxxxxxxxxxxxxxxxxxx = new BlockPos.MutableBlockPos();

                           for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < ☃; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                              for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxx < ☃; ☃xxxxxxxxxxxxxxxxxxxxxxxx++) {
                                 int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.getHeightValue(
                                       ☃xxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx
                                    )
                                    + 1;
                                 IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = Blocks.AIR.getDefaultState();
                                 if (☃xxxxxxxxxxxxxxxxxxxxxxxxx <= 1) {
                                    ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = Blocks.BEDROCK.getDefaultState();
                                 } else {
                                    do {
                                       ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.getBlockState(
                                          ☃xxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx,
                                          --☃xxxxxxxxxxxxxxxxxxxxxxxxx,
                                          ☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx
                                       );
                                       ☃xxxxxxxxxxxxxxxxxxxxxx.setPos(
                                          (☃xxxxxxxxxxxxxxxxx.x << 4) + ☃xxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx,
                                          ☃xxxxxxxxxxxxxxxxxxxxxxxxx,
                                          (☃xxxxxxxxxxxxxxxxx.z << 4) + ☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx
                                       );
                                    } while (
                                       ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.getMapColor(☃, ☃xxxxxxxxxxxxxxxxxxxxxx) == MapColor.AIR && ☃xxxxxxxxxxxxxxxxxxxxxxxxx > 0
                                    );

                                    if (☃xxxxxxxxxxxxxxxxxxxxxxxxx > 0 && ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.getMaterial().isLiquid()) {
                                       int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx - 1;

                                       IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx;
                                       do {
                                          ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.getBlockState(
                                             ☃xxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx,
                                             ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx--,
                                             ☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx
                                          );
                                          ☃xxxxxxxxxxxxxxxxxxxx++;
                                       } while (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx > 0 && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx.getMaterial().isLiquid());
                                    }
                                 }

                                 ☃xxxxxxxxxxxxxxxxxxxxx += (double)☃xxxxxxxxxxxxxxxxxxxxxxxxx / (☃ * ☃);
                                 ☃xxxxxxxxxxxxxxxx.add(☃xxxxxxxxxxxxxxxxxxxxxxxxxx.getMapColor(☃, ☃xxxxxxxxxxxxxxxxxxxxxx));
                              }
                           }
                        }

                        ☃xxxxxxxxxxxxxxxxxxxx /= ☃ * ☃;
                        double ☃xxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxx) * 4.0 / (☃ + 4) + ((☃xxxxxxxx + ☃xxxxxxxxxx & 1) - 0.5) * 0.4;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxx = 1;
                        if (☃xxxxxxxxxxxxxxxxxxxxxx > 0.6) {
                           ☃xxxxxxxxxxxxxxxxxxxxxxx = 2;
                        }

                        if (☃xxxxxxxxxxxxxxxxxxxxxx < -0.6) {
                           ☃xxxxxxxxxxxxxxxxxxxxxxx = 0;
                        }

                        MapColor ☃xxxxxxxxxxxxxxxxxxxxxxxx = (MapColor)Iterables.getFirst(Multisets.copyHighestCountFirst(☃xxxxxxxxxxxxxxxx), MapColor.AIR);
                        if (☃xxxxxxxxxxxxxxxxxxxxxxxx == MapColor.WATER) {
                           ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx * 0.1 + (☃xxxxxxxx + ☃xxxxxxxxxx & 1) * 0.2;
                           ☃xxxxxxxxxxxxxxxxxxxxxxx = 1;
                           if (☃xxxxxxxxxxxxxxxxxxxxxx < 0.5) {
                              ☃xxxxxxxxxxxxxxxxxxxxxxx = 2;
                           }

                           if (☃xxxxxxxxxxxxxxxxxxxxxx > 0.9) {
                              ☃xxxxxxxxxxxxxxxxxxxxxxx = 0;
                           }
                        }

                        ☃xxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx;
                        if (☃xxxxxxxxxx >= 0
                           && ☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx < ☃xxxxx * ☃xxxxx
                           && (!☃xxxxxxxxxxxxx || (☃xxxxxxxx + ☃xxxxxxxxxx & 1) != 0)) {
                           byte ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃.colors[☃xxxxxxxx + ☃xxxxxxxxxx * 128];
                           byte ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (byte)(☃xxxxxxxxxxxxxxxxxxxxxxxx.colorIndex * 4 + ☃xxxxxxxxxxxxxxxxxxxxxxx);
                           if (☃xxxxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxxxxxxxxxxx) {
                              ☃.colors[☃xxxxxxxx + ☃xxxxxxxxxx * 128] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
                              ☃.updateMapData(☃xxxxxxxx, ☃xxxxxxxxxx);
                              ☃xxxxxxx = true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void renderBiomePreviewMap(World var0, ItemStack var1) {
      if (☃.getItem() == Items.FILLED_MAP) {
         MapData ☃ = Items.FILLED_MAP.getMapData(☃, ☃);
         if (☃ != null) {
            if (☃.provider.getDimensionType().getId() == ☃.dimension) {
               int ☃x = 1 << ☃.scale;
               int ☃xx = ☃.xCenter;
               int ☃xxx = ☃.zCenter;
               Biome[] ☃xxxx = ☃.getBiomeProvider().getBiomes(null, (☃xx / ☃x - 64) * ☃x, (☃xxx / ☃x - 64) * ☃x, 128 * ☃x, 128 * ☃x, false);

               for (int ☃xxxxx = 0; ☃xxxxx < 128; ☃xxxxx++) {
                  for (int ☃xxxxxx = 0; ☃xxxxxx < 128; ☃xxxxxx++) {
                     int ☃xxxxxxx = ☃xxxxx * ☃x;
                     int ☃xxxxxxxx = ☃xxxxxx * ☃x;
                     Biome ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + ☃xxxxxxxx * 128 * ☃x];
                     MapColor ☃xxxxxxxxxx = MapColor.AIR;
                     int ☃xxxxxxxxxxx = 3;
                     int ☃xxxxxxxxxxxx = 8;
                     if (☃xxxxx > 0 && ☃xxxxxx > 0 && ☃xxxxx < 127 && ☃xxxxxx < 127) {
                        if (☃xxxx[(☃xxxxx - 1) * ☃x + (☃xxxxxx - 1) * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxx[(☃xxxxx - 1) * ☃x + (☃xxxxxx + 1) * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxx[(☃xxxxx - 1) * ☃x + ☃xxxxxx * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxx[(☃xxxxx + 1) * ☃x + (☃xxxxxx - 1) * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxx[(☃xxxxx + 1) * ☃x + (☃xxxxxx + 1) * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxx[(☃xxxxx + 1) * ☃x + ☃xxxxxx * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxx[☃xxxxx * ☃x + (☃xxxxxx - 1) * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxx[☃xxxxx * ☃x + (☃xxxxxx + 1) * ☃x * 128 * ☃x].getBaseHeight() >= 0.0F) {
                           ☃xxxxxxxxxxxx--;
                        }

                        if (☃xxxxxxxxx.getBaseHeight() < 0.0F) {
                           ☃xxxxxxxxxx = MapColor.ADOBE;
                           if (☃xxxxxxxxxxxx > 7 && ☃xxxxxx % 2 == 0) {
                              ☃xxxxxxxxxxx = (☃xxxxx + (int)(MathHelper.sin(☃xxxxxx + 0.0F) * 7.0F)) / 8 % 5;
                              if (☃xxxxxxxxxxx == 3) {
                                 ☃xxxxxxxxxxx = 1;
                              } else if (☃xxxxxxxxxxx == 4) {
                                 ☃xxxxxxxxxxx = 0;
                              }
                           } else if (☃xxxxxxxxxxxx > 7) {
                              ☃xxxxxxxxxx = MapColor.AIR;
                           } else if (☃xxxxxxxxxxxx > 5) {
                              ☃xxxxxxxxxxx = 1;
                           } else if (☃xxxxxxxxxxxx > 3) {
                              ☃xxxxxxxxxxx = 0;
                           } else if (☃xxxxxxxxxxxx > 1) {
                              ☃xxxxxxxxxxx = 0;
                           }
                        } else if (☃xxxxxxxxxxxx > 0) {
                           ☃xxxxxxxxxx = MapColor.BROWN;
                           if (☃xxxxxxxxxxxx > 3) {
                              ☃xxxxxxxxxxx = 1;
                           } else {
                              ☃xxxxxxxxxxx = 3;
                           }
                        }
                     }

                     if (☃xxxxxxxxxx != MapColor.AIR) {
                        ☃.colors[☃xxxxx + ☃xxxxxx * 128] = (byte)(☃xxxxxxxxxx.colorIndex * 4 + ☃xxxxxxxxxxx);
                        ☃.updateMapData(☃xxxxx, ☃xxxxxx);
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onUpdate(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
      if (!☃.isRemote) {
         MapData ☃ = this.getMapData(☃, ☃);
         if (☃ instanceof EntityPlayer) {
            EntityPlayer ☃x = (EntityPlayer)☃;
            ☃.updateVisiblePlayers(☃x, ☃);
         }

         if (☃ || ☃ instanceof EntityPlayer && ((EntityPlayer)☃).getHeldItemOffhand() == ☃) {
            this.updateMapData(☃, ☃, ☃);
         }
      }
   }

   @Nullable
   @Override
   public Packet<?> createMapDataPacket(ItemStack var1, World var2, EntityPlayer var3) {
      return this.getMapData(☃, ☃).getMapPacket(☃, ☃, ☃);
   }

   @Override
   public void onCreated(ItemStack var1, World var2, EntityPlayer var3) {
      NBTTagCompound ☃ = ☃.getTagCompound();
      if (☃ != null) {
         if (☃.hasKey("map_scale_direction", 99)) {
            scaleMap(☃, ☃, ☃.getInteger("map_scale_direction"));
            ☃.removeTag("map_scale_direction");
         } else if (☃.getBoolean("map_tracking_position")) {
            enableMapTracking(☃, ☃);
            ☃.removeTag("map_tracking_position");
         }
      }
   }

   protected static void scaleMap(ItemStack var0, World var1, int var2) {
      MapData ☃ = Items.FILLED_MAP.getMapData(☃, ☃);
      ☃.setItemDamage(☃.getUniqueDataId("map"));
      MapData ☃x = new MapData("map_" + ☃.getMetadata());
      if (☃ != null) {
         ☃x.scale = (byte)MathHelper.clamp(☃.scale + ☃, 0, 4);
         ☃x.trackingPosition = ☃.trackingPosition;
         ☃x.calculateMapCenter(☃.xCenter, ☃.zCenter, ☃x.scale);
         ☃x.dimension = ☃.dimension;
         ☃x.markDirty();
         ☃.setData("map_" + ☃.getMetadata(), ☃x);
      }
   }

   protected static void enableMapTracking(ItemStack var0, World var1) {
      MapData ☃ = Items.FILLED_MAP.getMapData(☃, ☃);
      ☃.setItemDamage(☃.getUniqueDataId("map"));
      MapData ☃x = new MapData("map_" + ☃.getMetadata());
      ☃x.trackingPosition = true;
      if (☃ != null) {
         ☃x.xCenter = ☃.xCenter;
         ☃x.zCenter = ☃.zCenter;
         ☃x.scale = ☃.scale;
         ☃x.dimension = ☃.dimension;
         ☃x.markDirty();
         ☃.setData("map_" + ☃.getMetadata(), ☃x);
      }
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      if (☃.isAdvanced()) {
         MapData ☃ = ☃ == null ? null : this.getMapData(☃, ☃);
         if (☃ != null) {
            ☃.add(I18n.translateToLocalFormatted("filled_map.scale", 1 << ☃.scale));
            ☃.add(I18n.translateToLocalFormatted("filled_map.level", ☃.scale, 4));
         } else {
            ☃.add(I18n.translateToLocal("filled_map.unknown"));
         }
      }
   }

   public static int getColor(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getSubCompound("display");
      if (☃ != null && ☃.hasKey("MapColor", 99)) {
         int ☃x = ☃.getInteger("MapColor");
         return 0xFF000000 | ☃x & 16777215;
      } else {
         return -12173266;
      }
   }
}
