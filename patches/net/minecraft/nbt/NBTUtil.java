package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class NBTUtil {
   private static final Logger LOGGER = LogManager.getLogger();

   @Nullable
   public static GameProfile readGameProfileFromNBT(NBTTagCompound var0) {
      String ☃ = null;
      String ☃x = null;
      if (☃.hasKey("Name", 8)) {
         ☃ = ☃.getString("Name");
      }

      if (☃.hasKey("Id", 8)) {
         ☃x = ☃.getString("Id");
      }

      try {
         UUID ☃xx;
         try {
            ☃xx = UUID.fromString(☃x);
         } catch (Throwable var12) {
            ☃xx = null;
         }

         GameProfile ☃xxx = new GameProfile(☃xx, ☃);
         if (☃.hasKey("Properties", 10)) {
            NBTTagCompound ☃xxxx = ☃.getCompoundTag("Properties");

            for (String ☃xxxxx : ☃xxxx.getKeySet()) {
               NBTTagList ☃xxxxxx = ☃xxxx.getTagList(☃xxxxx, 10);

               for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxxxxx.tagCount(); ☃xxxxxxx++) {
                  NBTTagCompound ☃xxxxxxxx = ☃xxxxxx.getCompoundTagAt(☃xxxxxxx);
                  String ☃xxxxxxxxx = ☃xxxxxxxx.getString("Value");
                  if (☃xxxxxxxx.hasKey("Signature", 8)) {
                     ☃xxx.getProperties().put(☃xxxxx, new Property(☃xxxxx, ☃xxxxxxxxx, ☃xxxxxxxx.getString("Signature")));
                  } else {
                     ☃xxx.getProperties().put(☃xxxxx, new Property(☃xxxxx, ☃xxxxxxxxx));
                  }
               }
            }
         }

         return ☃xxx;
      } catch (Throwable var13) {
         return null;
      }
   }

   public static NBTTagCompound writeGameProfile(NBTTagCompound var0, GameProfile var1) {
      if (!StringUtils.isNullOrEmpty(☃.getName())) {
         ☃.setString("Name", ☃.getName());
      }

      if (☃.getId() != null) {
         ☃.setString("Id", ☃.getId().toString());
      }

      if (!☃.getProperties().isEmpty()) {
         NBTTagCompound ☃ = new NBTTagCompound();

         for (String ☃x : ☃.getProperties().keySet()) {
            NBTTagList ☃xx = new NBTTagList();

            for (Property ☃xxx : ☃.getProperties().get(☃x)) {
               NBTTagCompound ☃xxxx = new NBTTagCompound();
               ☃xxxx.setString("Value", ☃xxx.getValue());
               if (☃xxx.hasSignature()) {
                  ☃xxxx.setString("Signature", ☃xxx.getSignature());
               }

               ☃xx.appendTag(☃xxxx);
            }

            ☃.setTag(☃x, ☃xx);
         }

         ☃.setTag("Properties", ☃);
      }

      return ☃;
   }

   @VisibleForTesting
   public static boolean areNBTEquals(NBTBase var0, NBTBase var1, boolean var2) {
      if (☃ == ☃) {
         return true;
      } else if (☃ == null) {
         return true;
      } else if (☃ == null) {
         return false;
      } else if (!☃.getClass().equals(☃.getClass())) {
         return false;
      } else if (☃ instanceof NBTTagCompound) {
         NBTTagCompound ☃ = (NBTTagCompound)☃;
         NBTTagCompound ☃x = (NBTTagCompound)☃;

         for (String ☃xx : ☃.getKeySet()) {
            NBTBase ☃xxx = ☃.getTag(☃xx);
            if (!areNBTEquals(☃xxx, ☃x.getTag(☃xx), ☃)) {
               return false;
            }
         }

         return true;
      } else if (☃ instanceof NBTTagList && ☃) {
         NBTTagList ☃ = (NBTTagList)☃;
         NBTTagList ☃x = (NBTTagList)☃;
         if (☃.isEmpty()) {
            return ☃x.isEmpty();
         } else {
            for (int ☃xxx = 0; ☃xxx < ☃.tagCount(); ☃xxx++) {
               NBTBase ☃xxxx = ☃.get(☃xxx);
               boolean ☃xxxxx = false;

               for (int ☃xxxxxx = 0; ☃xxxxxx < ☃x.tagCount(); ☃xxxxxx++) {
                  if (areNBTEquals(☃xxxx, ☃x.get(☃xxxxxx), ☃)) {
                     ☃xxxxx = true;
                     break;
                  }
               }

               if (!☃xxxxx) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return ☃.equals(☃);
      }
   }

   public static NBTTagCompound createUUIDTag(UUID var0) {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setLong("M", ☃.getMostSignificantBits());
      ☃.setLong("L", ☃.getLeastSignificantBits());
      return ☃;
   }

   public static UUID getUUIDFromTag(NBTTagCompound var0) {
      return new UUID(☃.getLong("M"), ☃.getLong("L"));
   }

   public static BlockPos getPosFromTag(NBTTagCompound var0) {
      return new BlockPos(☃.getInteger("X"), ☃.getInteger("Y"), ☃.getInteger("Z"));
   }

   public static NBTTagCompound createPosTag(BlockPos var0) {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setInteger("X", ☃.getX());
      ☃.setInteger("Y", ☃.getY());
      ☃.setInteger("Z", ☃.getZ());
      return ☃;
   }

   public static IBlockState readBlockState(NBTTagCompound var0) {
      if (!☃.hasKey("Name", 8)) {
         return Blocks.AIR.getDefaultState();
      } else {
         Block ☃ = Block.REGISTRY.getObject(new ResourceLocation(☃.getString("Name")));
         IBlockState ☃x = ☃.getDefaultState();
         if (☃.hasKey("Properties", 10)) {
            NBTTagCompound ☃xx = ☃.getCompoundTag("Properties");
            BlockStateContainer ☃xxx = ☃.getBlockState();

            for (String ☃xxxx : ☃xx.getKeySet()) {
               IProperty<?> ☃xxxxx = ☃xxx.getProperty(☃xxxx);
               if (☃xxxxx != null) {
                  ☃x = setValueHelper(☃x, ☃xxxxx, ☃xxxx, ☃xx, ☃);
               }
            }
         }

         return ☃x;
      }
   }

   private static <T extends Comparable<T>> IBlockState setValueHelper(
      IBlockState var0, IProperty<T> var1, String var2, NBTTagCompound var3, NBTTagCompound var4
   ) {
      Optional<T> ☃ = ☃.parseValue(☃.getString(☃));
      if (☃.isPresent()) {
         return ☃.withProperty(☃, (Comparable)☃.get());
      } else {
         LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", ☃, ☃.getString(☃), ☃.toString());
         return ☃;
      }
   }

   public static NBTTagCompound writeBlockState(NBTTagCompound var0, IBlockState var1) {
      ☃.setString("Name", Block.REGISTRY.getNameForObject(☃.getBlock()).toString());
      if (!☃.getProperties().isEmpty()) {
         NBTTagCompound ☃ = new NBTTagCompound();
         UnmodifiableIterator var3 = ☃.getProperties().entrySet().iterator();

         while (var3.hasNext()) {
            Entry<IProperty<?>, Comparable<?>> ☃x = (Entry<IProperty<?>, Comparable<?>>)var3.next();
            IProperty<?> ☃xx = ☃x.getKey();
            ☃.setString(☃xx.getName(), getName(☃xx, ☃x.getValue()));
         }

         ☃.setTag("Properties", ☃);
      }

      return ☃;
   }

   private static <T extends Comparable<T>> String getName(IProperty<T> var0, Comparable<?> var1) {
      return ☃.getName((T)☃);
   }
}
