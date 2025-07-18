package net.minecraft.util.datafix;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataFixer implements IDataFixer {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<IFixType, List<IDataWalker>> walkerMap = Maps.newHashMap();
   private final Map<IFixType, List<IFixableData>> fixMap = Maps.newHashMap();
   private final int version;

   public DataFixer(int var1) {
      this.version = ☃;
   }

   public NBTTagCompound process(IFixType var1, NBTTagCompound var2) {
      int ☃ = ☃.hasKey("DataVersion", 99) ? ☃.getInteger("DataVersion") : -1;
      return ☃ >= 1343 ? ☃ : this.process(☃, ☃, ☃);
   }

   @Override
   public NBTTagCompound process(IFixType var1, NBTTagCompound var2, int var3) {
      if (☃ < this.version) {
         ☃ = this.processFixes(☃, ☃, ☃);
         ☃ = this.processWalkers(☃, ☃, ☃);
      }

      return ☃;
   }

   private NBTTagCompound processFixes(IFixType var1, NBTTagCompound var2, int var3) {
      List<IFixableData> ☃ = this.fixMap.get(☃);
      if (☃ != null) {
         for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
            IFixableData ☃xx = ☃.get(☃x);
            if (☃xx.getFixVersion() > ☃) {
               ☃ = ☃xx.fixTagCompound(☃);
            }
         }
      }

      return ☃;
   }

   private NBTTagCompound processWalkers(IFixType var1, NBTTagCompound var2, int var3) {
      List<IDataWalker> ☃ = this.walkerMap.get(☃);
      if (☃ != null) {
         for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
            ☃ = ☃.get(☃x).process(this, ☃, ☃);
         }
      }

      return ☃;
   }

   public void registerWalker(FixTypes var1, IDataWalker var2) {
      this.registerVanillaWalker(☃, ☃);
   }

   public void registerVanillaWalker(IFixType var1, IDataWalker var2) {
      this.getTypeList(this.walkerMap, ☃).add(☃);
   }

   public void registerFix(IFixType var1, IFixableData var2) {
      List<IFixableData> ☃ = this.getTypeList(this.fixMap, ☃);
      int ☃x = ☃.getFixVersion();
      if (☃x > this.version) {
         LOGGER.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", ☃x, this.version);
      } else {
         if (!☃.isEmpty() && Util.getLastElement(☃).getFixVersion() > ☃x) {
            for (int ☃xx = 0; ☃xx < ☃.size(); ☃xx++) {
               if (☃.get(☃xx).getFixVersion() > ☃x) {
                  ☃.add(☃xx, ☃);
                  break;
               }
            }
         } else {
            ☃.add(☃);
         }
      }
   }

   private <V> List<V> getTypeList(Map<IFixType, List<V>> var1, IFixType var2) {
      List<V> ☃ = ☃.get(☃);
      if (☃ == null) {
         ☃ = Lists.newArrayList();
         ☃.put(☃, ☃);
      }

      return ☃;
   }
}
