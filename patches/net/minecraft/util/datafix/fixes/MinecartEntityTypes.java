package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData {
   private static final List<String> MINECART_TYPE_LIST = Lists.newArrayList(
      new String[]{"MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock"}
   );

   @Override
   public int getFixVersion() {
      return 106;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("Minecart".equals(☃.getString("id"))) {
         String ☃ = "MinecartRideable";
         int ☃x = ☃.getInteger("Type");
         if (☃x > 0 && ☃x < MINECART_TYPE_LIST.size()) {
            ☃ = MINECART_TYPE_LIST.get(☃x);
         }

         ☃.setString("id", ☃);
         ☃.removeTag("Type");
      }

      return ☃;
   }
}
