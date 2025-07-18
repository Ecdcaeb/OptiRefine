package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class FlatGeneratorInfo {
   private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
   private final Map<String, Map<String, String>> worldFeatures = Maps.newHashMap();
   private int biomeToUse;

   public int getBiome() {
      return this.biomeToUse;
   }

   public void setBiome(int var1) {
      this.biomeToUse = ☃;
   }

   public Map<String, Map<String, String>> getWorldFeatures() {
      return this.worldFeatures;
   }

   public List<FlatLayerInfo> getFlatLayers() {
      return this.flatLayers;
   }

   public void updateLayers() {
      int ☃ = 0;

      for (FlatLayerInfo ☃x : this.flatLayers) {
         ☃x.setMinY(☃);
         ☃ += ☃x.getLayerCount();
      }
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder();
      ☃.append(3);
      ☃.append(";");

      for (int ☃x = 0; ☃x < this.flatLayers.size(); ☃x++) {
         if (☃x > 0) {
            ☃.append(",");
         }

         ☃.append(this.flatLayers.get(☃x));
      }

      ☃.append(";");
      ☃.append(this.biomeToUse);
      if (this.worldFeatures.isEmpty()) {
         ☃.append(";");
      } else {
         ☃.append(";");
         int ☃x = 0;

         for (Entry<String, Map<String, String>> ☃xx : this.worldFeatures.entrySet()) {
            if (☃x++ > 0) {
               ☃.append(",");
            }

            ☃.append(☃xx.getKey().toLowerCase(Locale.ROOT));
            Map<String, String> ☃xxx = ☃xx.getValue();
            if (!☃xxx.isEmpty()) {
               ☃.append("(");
               int ☃xxxx = 0;

               for (Entry<String, String> ☃xxxxx : ☃xxx.entrySet()) {
                  if (☃xxxx++ > 0) {
                     ☃.append(" ");
                  }

                  ☃.append(☃xxxxx.getKey());
                  ☃.append("=");
                  ☃.append(☃xxxxx.getValue());
               }

               ☃.append(")");
            }
         }
      }

      return ☃.toString();
   }

   private static FlatLayerInfo getLayerFromString(int var0, String var1, int var2) {
      String[] ☃ = ☃ >= 3 ? ☃.split("\\*", 2) : ☃.split("x", 2);
      int ☃x = 1;
      int ☃xx = 0;
      if (☃.length == 2) {
         try {
            ☃x = Integer.parseInt(☃[0]);
            if (☃ + ☃x >= 256) {
               ☃x = 256 - ☃;
            }

            if (☃x < 0) {
               ☃x = 0;
            }
         } catch (Throwable var8) {
            return null;
         }
      }

      Block ☃xxx;
      try {
         String ☃xxxx = ☃[☃.length - 1];
         if (☃ < 3) {
            ☃ = ☃xxxx.split(":", 2);
            if (☃.length > 1) {
               ☃xx = Integer.parseInt(☃[1]);
            }

            ☃xxx = Block.getBlockById(Integer.parseInt(☃[0]));
         } else {
            ☃ = ☃xxxx.split(":", 3);
            ☃xxx = ☃.length > 1 ? Block.getBlockFromName(☃[0] + ":" + ☃[1]) : null;
            if (☃xxx != null) {
               ☃xx = ☃.length > 2 ? Integer.parseInt(☃[2]) : 0;
            } else {
               ☃xxx = Block.getBlockFromName(☃[0]);
               if (☃xxx != null) {
                  ☃xx = ☃.length > 1 ? Integer.parseInt(☃[1]) : 0;
               }
            }

            if (☃xxx == null) {
               return null;
            }
         }

         if (☃xxx == Blocks.AIR) {
            ☃xx = 0;
         }

         if (☃xx < 0 || ☃xx > 15) {
            ☃xx = 0;
         }
      } catch (Throwable var9) {
         return null;
      }

      FlatLayerInfo ☃xxxxx = new FlatLayerInfo(☃, ☃x, ☃xxx, ☃xx);
      ☃xxxxx.setMinY(☃);
      return ☃xxxxx;
   }

   private static List<FlatLayerInfo> getLayersFromString(int var0, String var1) {
      if (☃ != null && ☃.length() >= 1) {
         List<FlatLayerInfo> ☃ = Lists.newArrayList();
         String[] ☃x = ☃.split(",");
         int ☃xx = 0;

         for (String ☃xxx : ☃x) {
            FlatLayerInfo ☃xxxx = getLayerFromString(☃, ☃xxx, ☃xx);
            if (☃xxxx == null) {
               return null;
            }

            ☃.add(☃xxxx);
            ☃xx += ☃xxxx.getLayerCount();
         }

         return ☃;
      } else {
         return null;
      }
   }

   public static FlatGeneratorInfo createFlatGeneratorFromString(String var0) {
      if (☃ == null) {
         return getDefaultFlatGenerator();
      } else {
         String[] ☃ = ☃.split(";", -1);
         int ☃x = ☃.length == 1 ? 0 : MathHelper.getInt(☃[0], 0);
         if (☃x >= 0 && ☃x <= 3) {
            FlatGeneratorInfo ☃xx = new FlatGeneratorInfo();
            int ☃xxx = ☃.length == 1 ? 0 : 1;
            List<FlatLayerInfo> ☃xxxx = getLayersFromString(☃x, ☃[☃xxx++]);
            if (☃xxxx != null && !☃xxxx.isEmpty()) {
               ☃xx.getFlatLayers().addAll(☃xxxx);
               ☃xx.updateLayers();
               int ☃xxxxx = Biome.getIdForBiome(Biomes.PLAINS);
               if (☃x > 0 && ☃.length > ☃xxx) {
                  ☃xxxxx = MathHelper.getInt(☃[☃xxx++], ☃xxxxx);
               }

               ☃xx.setBiome(☃xxxxx);
               if (☃x > 0 && ☃.length > ☃xxx) {
                  String[] ☃xxxxxx = ☃[☃xxx++].toLowerCase(Locale.ROOT).split(",");

                  for (String ☃xxxxxxx : ☃xxxxxx) {
                     String[] ☃xxxxxxxx = ☃xxxxxxx.split("\\(", 2);
                     Map<String, String> ☃xxxxxxxxx = Maps.newHashMap();
                     if (!☃xxxxxxxx[0].isEmpty()) {
                        ☃xx.getWorldFeatures().put(☃xxxxxxxx[0], ☃xxxxxxxxx);
                        if (☃xxxxxxxx.length > 1 && ☃xxxxxxxx[1].endsWith(")") && ☃xxxxxxxx[1].length() > 1) {
                           String[] ☃xxxxxxxxxx = ☃xxxxxxxx[1].substring(0, ☃xxxxxxxx[1].length() - 1).split(" ");

                           for (String ☃xxxxxxxxxxx : ☃xxxxxxxxxx) {
                              String[] ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx.split("=", 2);
                              if (☃xxxxxxxxxxxx.length == 2) {
                                 ☃xxxxxxxxx.put(☃xxxxxxxxxxxx[0], ☃xxxxxxxxxxxx[1]);
                              }
                           }
                        }
                     }
                  }
               } else {
                  ☃xx.getWorldFeatures().put("village", Maps.newHashMap());
               }

               return ☃xx;
            } else {
               return getDefaultFlatGenerator();
            }
         } else {
            return getDefaultFlatGenerator();
         }
      }
   }

   public static FlatGeneratorInfo getDefaultFlatGenerator() {
      FlatGeneratorInfo ☃ = new FlatGeneratorInfo();
      ☃.setBiome(Biome.getIdForBiome(Biomes.PLAINS));
      ☃.getFlatLayers().add(new FlatLayerInfo(1, Blocks.BEDROCK));
      ☃.getFlatLayers().add(new FlatLayerInfo(2, Blocks.DIRT));
      ☃.getFlatLayers().add(new FlatLayerInfo(1, Blocks.GRASS));
      ☃.updateLayers();
      ☃.getWorldFeatures().put("village", Maps.newHashMap());
      return ☃;
   }
}
