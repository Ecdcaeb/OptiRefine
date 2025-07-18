package net.minecraft.util.datafix.walkers;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockEntityTag implements IDataWalker {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Map<String, String> NEW_TO_OLD_ID_MAP = Maps.newHashMap();
   private static final Map<String, String> ITEM_ID_TO_BLOCK_ENTITY_ID = Maps.newHashMap();

   @Nullable
   private static String getBlockEntityID(int var0, String var1) {
      return ☃ < 515 ? NEW_TO_OLD_ID_MAP.get(new ResourceLocation(☃).toString()) : ITEM_ID_TO_BLOCK_ENTITY_ID.get(new ResourceLocation(☃).toString());
   }

   @Override
   public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
      if (!☃.hasKey("tag", 10)) {
         return ☃;
      } else {
         NBTTagCompound ☃ = ☃.getCompoundTag("tag");
         if (☃.hasKey("BlockEntityTag", 10)) {
            NBTTagCompound ☃x = ☃.getCompoundTag("BlockEntityTag");
            String ☃xx = ☃.getString("id");
            String ☃xxx = getBlockEntityID(☃, ☃xx);
            boolean ☃xxxx;
            if (☃xxx == null) {
               LOGGER.warn("Unable to resolve BlockEntity for ItemInstance: {}", ☃xx);
               ☃xxxx = false;
            } else {
               ☃xxxx = !☃x.hasKey("id");
               ☃x.setString("id", ☃xxx);
            }

            ☃.process(FixTypes.BLOCK_ENTITY, ☃x, ☃);
            if (☃xxxx) {
               ☃x.removeTag("id");
            }
         }

         return ☃;
      }
   }

   static {
      Map<String, String> ☃ = NEW_TO_OLD_ID_MAP;
      ☃.put("minecraft:furnace", "Furnace");
      ☃.put("minecraft:lit_furnace", "Furnace");
      ☃.put("minecraft:chest", "Chest");
      ☃.put("minecraft:trapped_chest", "Chest");
      ☃.put("minecraft:ender_chest", "EnderChest");
      ☃.put("minecraft:jukebox", "RecordPlayer");
      ☃.put("minecraft:dispenser", "Trap");
      ☃.put("minecraft:dropper", "Dropper");
      ☃.put("minecraft:sign", "Sign");
      ☃.put("minecraft:mob_spawner", "MobSpawner");
      ☃.put("minecraft:noteblock", "Music");
      ☃.put("minecraft:brewing_stand", "Cauldron");
      ☃.put("minecraft:enhanting_table", "EnchantTable");
      ☃.put("minecraft:command_block", "CommandBlock");
      ☃.put("minecraft:beacon", "Beacon");
      ☃.put("minecraft:skull", "Skull");
      ☃.put("minecraft:daylight_detector", "DLDetector");
      ☃.put("minecraft:hopper", "Hopper");
      ☃.put("minecraft:banner", "Banner");
      ☃.put("minecraft:flower_pot", "FlowerPot");
      ☃.put("minecraft:repeating_command_block", "CommandBlock");
      ☃.put("minecraft:chain_command_block", "CommandBlock");
      ☃.put("minecraft:standing_sign", "Sign");
      ☃.put("minecraft:wall_sign", "Sign");
      ☃.put("minecraft:piston_head", "Piston");
      ☃.put("minecraft:daylight_detector_inverted", "DLDetector");
      ☃.put("minecraft:unpowered_comparator", "Comparator");
      ☃.put("minecraft:powered_comparator", "Comparator");
      ☃.put("minecraft:wall_banner", "Banner");
      ☃.put("minecraft:standing_banner", "Banner");
      ☃.put("minecraft:structure_block", "Structure");
      ☃.put("minecraft:end_portal", "Airportal");
      ☃.put("minecraft:end_gateway", "EndGateway");
      ☃.put("minecraft:shield", "Shield");
      ☃ = ITEM_ID_TO_BLOCK_ENTITY_ID;
      ☃.put("minecraft:furnace", "minecraft:furnace");
      ☃.put("minecraft:lit_furnace", "minecraft:furnace");
      ☃.put("minecraft:chest", "minecraft:chest");
      ☃.put("minecraft:trapped_chest", "minecraft:chest");
      ☃.put("minecraft:ender_chest", "minecraft:enderchest");
      ☃.put("minecraft:jukebox", "minecraft:jukebox");
      ☃.put("minecraft:dispenser", "minecraft:dispenser");
      ☃.put("minecraft:dropper", "minecraft:dropper");
      ☃.put("minecraft:sign", "minecraft:sign");
      ☃.put("minecraft:mob_spawner", "minecraft:mob_spawner");
      ☃.put("minecraft:noteblock", "minecraft:noteblock");
      ☃.put("minecraft:brewing_stand", "minecraft:brewing_stand");
      ☃.put("minecraft:enhanting_table", "minecraft:enchanting_table");
      ☃.put("minecraft:command_block", "minecraft:command_block");
      ☃.put("minecraft:beacon", "minecraft:beacon");
      ☃.put("minecraft:skull", "minecraft:skull");
      ☃.put("minecraft:daylight_detector", "minecraft:daylight_detector");
      ☃.put("minecraft:hopper", "minecraft:hopper");
      ☃.put("minecraft:banner", "minecraft:banner");
      ☃.put("minecraft:flower_pot", "minecraft:flower_pot");
      ☃.put("minecraft:repeating_command_block", "minecraft:command_block");
      ☃.put("minecraft:chain_command_block", "minecraft:command_block");
      ☃.put("minecraft:shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:white_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:orange_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:magenta_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:light_blue_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:yellow_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:lime_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:pink_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:gray_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:silver_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:cyan_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:purple_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:blue_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:brown_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:green_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:red_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:black_shulker_box", "minecraft:shulker_box");
      ☃.put("minecraft:bed", "minecraft:bed");
      ☃.put("minecraft:standing_sign", "minecraft:sign");
      ☃.put("minecraft:wall_sign", "minecraft:sign");
      ☃.put("minecraft:piston_head", "minecraft:piston");
      ☃.put("minecraft:daylight_detector_inverted", "minecraft:daylight_detector");
      ☃.put("minecraft:unpowered_comparator", "minecraft:comparator");
      ☃.put("minecraft:powered_comparator", "minecraft:comparator");
      ☃.put("minecraft:wall_banner", "minecraft:banner");
      ☃.put("minecraft:standing_banner", "minecraft:banner");
      ☃.put("minecraft:structure_block", "minecraft:structure_block");
      ☃.put("minecraft:end_portal", "minecraft:end_portal");
      ☃.put("minecraft:end_gateway", "minecraft:end_gateway");
      ☃.put("minecraft:shield", "minecraft:shield");
   }
}
