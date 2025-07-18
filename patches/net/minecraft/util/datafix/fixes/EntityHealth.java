package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class EntityHealth implements IFixableData {
   private static final Set<String> ENTITY_LIST = Sets.newHashSet(
      new String[]{
         "ArmorStand",
         "Bat",
         "Blaze",
         "CaveSpider",
         "Chicken",
         "Cow",
         "Creeper",
         "EnderDragon",
         "Enderman",
         "Endermite",
         "EntityHorse",
         "Ghast",
         "Giant",
         "Guardian",
         "LavaSlime",
         "MushroomCow",
         "Ozelot",
         "Pig",
         "PigZombie",
         "Rabbit",
         "Sheep",
         "Shulker",
         "Silverfish",
         "Skeleton",
         "Slime",
         "SnowMan",
         "Spider",
         "Squid",
         "Villager",
         "VillagerGolem",
         "Witch",
         "WitherBoss",
         "Wolf",
         "Zombie"
      }
   );

   @Override
   public int getFixVersion() {
      return 109;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if (ENTITY_LIST.contains(☃.getString("id"))) {
         float ☃;
         if (☃.hasKey("HealF", 99)) {
            ☃ = ☃.getFloat("HealF");
            ☃.removeTag("HealF");
         } else {
            if (!☃.hasKey("Health", 99)) {
               return ☃;
            }

            ☃ = ☃.getFloat("Health");
         }

         ☃.setFloat("Health", ☃);
      }

      return ☃;
   }
}
