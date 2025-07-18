package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapGenStructureIO {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Map<String, Class<? extends StructureStart>> startNameToClassMap = Maps.newHashMap();
   private static final Map<Class<? extends StructureStart>, String> startClassToNameMap = Maps.newHashMap();
   private static final Map<String, Class<? extends StructureComponent>> componentNameToClassMap = Maps.newHashMap();
   private static final Map<Class<? extends StructureComponent>, String> componentClassToNameMap = Maps.newHashMap();

   private static void registerStructure(Class<? extends StructureStart> var0, String var1) {
      startNameToClassMap.put(☃, ☃);
      startClassToNameMap.put(☃, ☃);
   }

   static void registerStructureComponent(Class<? extends StructureComponent> var0, String var1) {
      componentNameToClassMap.put(☃, ☃);
      componentClassToNameMap.put(☃, ☃);
   }

   public static String getStructureStartName(StructureStart var0) {
      return startClassToNameMap.get(☃.getClass());
   }

   public static String getStructureComponentName(StructureComponent var0) {
      return componentClassToNameMap.get(☃.getClass());
   }

   @Nullable
   public static StructureStart getStructureStart(NBTTagCompound var0, World var1) {
      StructureStart ☃ = null;

      try {
         Class<? extends StructureStart> ☃x = startNameToClassMap.get(☃.getString("id"));
         if (☃x != null) {
            ☃ = ☃x.newInstance();
         }
      } catch (Exception var4) {
         LOGGER.warn("Failed Start with id {}", ☃.getString("id"));
         var4.printStackTrace();
      }

      if (☃ != null) {
         ☃.readStructureComponentsFromNBT(☃, ☃);
      } else {
         LOGGER.warn("Skipping Structure with id {}", ☃.getString("id"));
      }

      return ☃;
   }

   public static StructureComponent getStructureComponent(NBTTagCompound var0, World var1) {
      StructureComponent ☃ = null;

      try {
         Class<? extends StructureComponent> ☃x = componentNameToClassMap.get(☃.getString("id"));
         if (☃x != null) {
            ☃ = ☃x.newInstance();
         }
      } catch (Exception var4) {
         LOGGER.warn("Failed Piece with id {}", ☃.getString("id"));
         var4.printStackTrace();
      }

      if (☃ != null) {
         ☃.readStructureBaseNBT(☃, ☃);
      } else {
         LOGGER.warn("Skipping Piece with id {}", ☃.getString("id"));
      }

      return ☃;
   }

   static {
      registerStructure(StructureMineshaftStart.class, "Mineshaft");
      registerStructure(MapGenVillage.Start.class, "Village");
      registerStructure(MapGenNetherBridge.Start.class, "Fortress");
      registerStructure(MapGenStronghold.Start.class, "Stronghold");
      registerStructure(MapGenScatteredFeature.Start.class, "Temple");
      registerStructure(StructureOceanMonument.StartMonument.class, "Monument");
      registerStructure(MapGenEndCity.Start.class, "EndCity");
      registerStructure(WoodlandMansion.Start.class, "Mansion");
      StructureMineshaftPieces.registerStructurePieces();
      StructureVillagePieces.registerVillagePieces();
      StructureNetherBridgePieces.registerNetherFortressPieces();
      StructureStrongholdPieces.registerStrongholdPieces();
      ComponentScatteredFeaturePieces.registerScatteredFeaturePieces();
      StructureOceanMonumentPieces.registerOceanMonumentPieces();
      StructureEndCityPieces.registerPieces();
      WoodlandMansionPieces.registerWoodlandMansionPieces();
   }
}
