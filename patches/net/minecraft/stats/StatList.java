package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class StatList {
   protected static final Map<String, StatBase> ID_TO_STAT_MAP = Maps.newHashMap();
   public static final List<StatBase> ALL_STATS = Lists.newArrayList();
   public static final List<StatBase> BASIC_STATS = Lists.newArrayList();
   public static final List<StatCrafting> USE_ITEM_STATS = Lists.newArrayList();
   public static final List<StatCrafting> MINE_BLOCK_STATS = Lists.newArrayList();
   public static final StatBase LEAVE_GAME = new StatBasic("stat.leaveGame", new TextComponentTranslation("stat.leaveGame"))
      .initIndependentStat()
      .registerStat();
   public static final StatBase PLAY_ONE_MINUTE = new StatBasic("stat.playOneMinute", new TextComponentTranslation("stat.playOneMinute"), StatBase.timeStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase TIME_SINCE_DEATH = new StatBasic(
         "stat.timeSinceDeath", new TextComponentTranslation("stat.timeSinceDeath"), StatBase.timeStatType
      )
      .initIndependentStat()
      .registerStat();
   public static final StatBase SNEAK_TIME = new StatBasic("stat.sneakTime", new TextComponentTranslation("stat.sneakTime"), StatBase.timeStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase WALK_ONE_CM = new StatBasic("stat.walkOneCm", new TextComponentTranslation("stat.walkOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase CROUCH_ONE_CM = new StatBasic("stat.crouchOneCm", new TextComponentTranslation("stat.crouchOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase SPRINT_ONE_CM = new StatBasic("stat.sprintOneCm", new TextComponentTranslation("stat.sprintOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase SWIM_ONE_CM = new StatBasic("stat.swimOneCm", new TextComponentTranslation("stat.swimOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase FALL_ONE_CM = new StatBasic("stat.fallOneCm", new TextComponentTranslation("stat.fallOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase CLIMB_ONE_CM = new StatBasic("stat.climbOneCm", new TextComponentTranslation("stat.climbOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase FLY_ONE_CM = new StatBasic("stat.flyOneCm", new TextComponentTranslation("stat.flyOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase DIVE_ONE_CM = new StatBasic("stat.diveOneCm", new TextComponentTranslation("stat.diveOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase MINECART_ONE_CM = new StatBasic(
         "stat.minecartOneCm", new TextComponentTranslation("stat.minecartOneCm"), StatBase.distanceStatType
      )
      .initIndependentStat()
      .registerStat();
   public static final StatBase BOAT_ONE_CM = new StatBasic("stat.boatOneCm", new TextComponentTranslation("stat.boatOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase PIG_ONE_CM = new StatBasic("stat.pigOneCm", new TextComponentTranslation("stat.pigOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase HORSE_ONE_CM = new StatBasic("stat.horseOneCm", new TextComponentTranslation("stat.horseOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase AVIATE_ONE_CM = new StatBasic("stat.aviateOneCm", new TextComponentTranslation("stat.aviateOneCm"), StatBase.distanceStatType)
      .initIndependentStat()
      .registerStat();
   public static final StatBase JUMP = new StatBasic("stat.jump", new TextComponentTranslation("stat.jump")).initIndependentStat().registerStat();
   public static final StatBase DROP = new StatBasic("stat.drop", new TextComponentTranslation("stat.drop")).initIndependentStat().registerStat();
   public static final StatBase DAMAGE_DEALT = new StatBasic("stat.damageDealt", new TextComponentTranslation("stat.damageDealt"), StatBase.divideByTen)
      .registerStat();
   public static final StatBase DAMAGE_TAKEN = new StatBasic("stat.damageTaken", new TextComponentTranslation("stat.damageTaken"), StatBase.divideByTen)
      .registerStat();
   public static final StatBase DEATHS = new StatBasic("stat.deaths", new TextComponentTranslation("stat.deaths")).registerStat();
   public static final StatBase MOB_KILLS = new StatBasic("stat.mobKills", new TextComponentTranslation("stat.mobKills")).registerStat();
   public static final StatBase ANIMALS_BRED = new StatBasic("stat.animalsBred", new TextComponentTranslation("stat.animalsBred")).registerStat();
   public static final StatBase PLAYER_KILLS = new StatBasic("stat.playerKills", new TextComponentTranslation("stat.playerKills")).registerStat();
   public static final StatBase FISH_CAUGHT = new StatBasic("stat.fishCaught", new TextComponentTranslation("stat.fishCaught")).registerStat();
   public static final StatBase TALKED_TO_VILLAGER = new StatBasic("stat.talkedToVillager", new TextComponentTranslation("stat.talkedToVillager"))
      .registerStat();
   public static final StatBase TRADED_WITH_VILLAGER = new StatBasic("stat.tradedWithVillager", new TextComponentTranslation("stat.tradedWithVillager"))
      .registerStat();
   public static final StatBase CAKE_SLICES_EATEN = new StatBasic("stat.cakeSlicesEaten", new TextComponentTranslation("stat.cakeSlicesEaten")).registerStat();
   public static final StatBase CAULDRON_FILLED = new StatBasic("stat.cauldronFilled", new TextComponentTranslation("stat.cauldronFilled")).registerStat();
   public static final StatBase CAULDRON_USED = new StatBasic("stat.cauldronUsed", new TextComponentTranslation("stat.cauldronUsed")).registerStat();
   public static final StatBase ARMOR_CLEANED = new StatBasic("stat.armorCleaned", new TextComponentTranslation("stat.armorCleaned")).registerStat();
   public static final StatBase BANNER_CLEANED = new StatBasic("stat.bannerCleaned", new TextComponentTranslation("stat.bannerCleaned")).registerStat();
   public static final StatBase BREWINGSTAND_INTERACTION = new StatBasic(
         "stat.brewingstandInteraction", new TextComponentTranslation("stat.brewingstandInteraction")
      )
      .registerStat();
   public static final StatBase BEACON_INTERACTION = new StatBasic("stat.beaconInteraction", new TextComponentTranslation("stat.beaconInteraction"))
      .registerStat();
   public static final StatBase DROPPER_INSPECTED = new StatBasic("stat.dropperInspected", new TextComponentTranslation("stat.dropperInspected"))
      .registerStat();
   public static final StatBase HOPPER_INSPECTED = new StatBasic("stat.hopperInspected", new TextComponentTranslation("stat.hopperInspected")).registerStat();
   public static final StatBase DISPENSER_INSPECTED = new StatBasic("stat.dispenserInspected", new TextComponentTranslation("stat.dispenserInspected"))
      .registerStat();
   public static final StatBase NOTEBLOCK_PLAYED = new StatBasic("stat.noteblockPlayed", new TextComponentTranslation("stat.noteblockPlayed")).registerStat();
   public static final StatBase NOTEBLOCK_TUNED = new StatBasic("stat.noteblockTuned", new TextComponentTranslation("stat.noteblockTuned")).registerStat();
   public static final StatBase FLOWER_POTTED = new StatBasic("stat.flowerPotted", new TextComponentTranslation("stat.flowerPotted")).registerStat();
   public static final StatBase TRAPPED_CHEST_TRIGGERED = new StatBasic(
         "stat.trappedChestTriggered", new TextComponentTranslation("stat.trappedChestTriggered")
      )
      .registerStat();
   public static final StatBase ENDERCHEST_OPENED = new StatBasic("stat.enderchestOpened", new TextComponentTranslation("stat.enderchestOpened"))
      .registerStat();
   public static final StatBase ITEM_ENCHANTED = new StatBasic("stat.itemEnchanted", new TextComponentTranslation("stat.itemEnchanted")).registerStat();
   public static final StatBase RECORD_PLAYED = new StatBasic("stat.recordPlayed", new TextComponentTranslation("stat.recordPlayed")).registerStat();
   public static final StatBase FURNACE_INTERACTION = new StatBasic("stat.furnaceInteraction", new TextComponentTranslation("stat.furnaceInteraction"))
      .registerStat();
   public static final StatBase CRAFTING_TABLE_INTERACTION = new StatBasic(
         "stat.craftingTableInteraction", new TextComponentTranslation("stat.workbenchInteraction")
      )
      .registerStat();
   public static final StatBase CHEST_OPENED = new StatBasic("stat.chestOpened", new TextComponentTranslation("stat.chestOpened")).registerStat();
   public static final StatBase SLEEP_IN_BED = new StatBasic("stat.sleepInBed", new TextComponentTranslation("stat.sleepInBed")).registerStat();
   public static final StatBase OPEN_SHULKER_BOX = new StatBasic("stat.shulkerBoxOpened", new TextComponentTranslation("stat.shulkerBoxOpened")).registerStat();
   private static final StatBase[] BLOCKS_STATS = new StatBase[4096];
   private static final StatBase[] CRAFTS_STATS = new StatBase[32000];
   private static final StatBase[] OBJECT_USE_STATS = new StatBase[32000];
   private static final StatBase[] OBJECT_BREAK_STATS = new StatBase[32000];
   private static final StatBase[] OBJECTS_PICKED_UP_STATS = new StatBase[32000];
   private static final StatBase[] OBJECTS_DROPPED_STATS = new StatBase[32000];

   @Nullable
   public static StatBase getBlockStats(Block var0) {
      return BLOCKS_STATS[Block.getIdFromBlock(☃)];
   }

   @Nullable
   public static StatBase getCraftStats(Item var0) {
      return CRAFTS_STATS[Item.getIdFromItem(☃)];
   }

   @Nullable
   public static StatBase getObjectUseStats(Item var0) {
      return OBJECT_USE_STATS[Item.getIdFromItem(☃)];
   }

   @Nullable
   public static StatBase getObjectBreakStats(Item var0) {
      return OBJECT_BREAK_STATS[Item.getIdFromItem(☃)];
   }

   @Nullable
   public static StatBase getObjectsPickedUpStats(Item var0) {
      return OBJECTS_PICKED_UP_STATS[Item.getIdFromItem(☃)];
   }

   @Nullable
   public static StatBase getDroppedObjectStats(Item var0) {
      return OBJECTS_DROPPED_STATS[Item.getIdFromItem(☃)];
   }

   public static void init() {
      initMiningStats();
      initStats();
      initItemDepleteStats();
      initCraftableStats();
      initPickedUpAndDroppedStats();
   }

   private static void initCraftableStats() {
      Set<Item> ☃ = Sets.newHashSet();

      for (IRecipe ☃x : CraftingManager.REGISTRY) {
         ItemStack ☃xx = ☃x.getRecipeOutput();
         if (!☃xx.isEmpty()) {
            ☃.add(☃x.getRecipeOutput().getItem());
         }
      }

      for (ItemStack ☃xx : FurnaceRecipes.instance().getSmeltingList().values()) {
         ☃.add(☃xx.getItem());
      }

      for (Item ☃xx : ☃) {
         if (☃xx != null) {
            int ☃xxx = Item.getIdFromItem(☃xx);
            String ☃xxxx = getItemName(☃xx);
            if (☃xxxx != null) {
               CRAFTS_STATS[☃xxx] = new StatCrafting(
                     "stat.craftItem.", ☃xxxx, new TextComponentTranslation("stat.craftItem", new ItemStack(☃xx).getTextComponent()), ☃xx
                  )
                  .registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(CRAFTS_STATS);
   }

   private static void initMiningStats() {
      for (Block ☃ : Block.REGISTRY) {
         Item ☃x = Item.getItemFromBlock(☃);
         if (☃x != Items.AIR) {
            int ☃xx = Block.getIdFromBlock(☃);
            String ☃xxx = getItemName(☃x);
            if (☃xxx != null && ☃.getEnableStats()) {
               BLOCKS_STATS[☃xx] = new StatCrafting(
                     "stat.mineBlock.", ☃xxx, new TextComponentTranslation("stat.mineBlock", new ItemStack(☃).getTextComponent()), ☃x
                  )
                  .registerStat();
               MINE_BLOCK_STATS.add((StatCrafting)BLOCKS_STATS[☃xx]);
            }
         }
      }

      replaceAllSimilarBlocks(BLOCKS_STATS);
   }

   private static void initStats() {
      for (Item ☃ : Item.REGISTRY) {
         if (☃ != null) {
            int ☃x = Item.getIdFromItem(☃);
            String ☃xx = getItemName(☃);
            if (☃xx != null) {
               OBJECT_USE_STATS[☃x] = new StatCrafting(
                     "stat.useItem.", ☃xx, new TextComponentTranslation("stat.useItem", new ItemStack(☃).getTextComponent()), ☃
                  )
                  .registerStat();
               if (!(☃ instanceof ItemBlock)) {
                  USE_ITEM_STATS.add((StatCrafting)OBJECT_USE_STATS[☃x]);
               }
            }
         }
      }

      replaceAllSimilarBlocks(OBJECT_USE_STATS);
   }

   private static void initItemDepleteStats() {
      for (Item ☃ : Item.REGISTRY) {
         if (☃ != null) {
            int ☃x = Item.getIdFromItem(☃);
            String ☃xx = getItemName(☃);
            if (☃xx != null && ☃.isDamageable()) {
               OBJECT_BREAK_STATS[☃x] = new StatCrafting(
                     "stat.breakItem.", ☃xx, new TextComponentTranslation("stat.breakItem", new ItemStack(☃).getTextComponent()), ☃
                  )
                  .registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
   }

   private static void initPickedUpAndDroppedStats() {
      for (Item ☃ : Item.REGISTRY) {
         if (☃ != null) {
            int ☃x = Item.getIdFromItem(☃);
            String ☃xx = getItemName(☃);
            if (☃xx != null) {
               OBJECTS_PICKED_UP_STATS[☃x] = new StatCrafting(
                     "stat.pickup.", ☃xx, new TextComponentTranslation("stat.pickup", new ItemStack(☃).getTextComponent()), ☃
                  )
                  .registerStat();
               OBJECTS_DROPPED_STATS[☃x] = new StatCrafting(
                     "stat.drop.", ☃xx, new TextComponentTranslation("stat.drop", new ItemStack(☃).getTextComponent()), ☃
                  )
                  .registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
   }

   private static String getItemName(Item var0) {
      ResourceLocation ☃ = Item.REGISTRY.getNameForObject(☃);
      return ☃ != null ? ☃.toString().replace(':', '.') : null;
   }

   private static void replaceAllSimilarBlocks(StatBase[] var0) {
      mergeStatBases(☃, Blocks.WATER, Blocks.FLOWING_WATER);
      mergeStatBases(☃, Blocks.LAVA, Blocks.FLOWING_LAVA);
      mergeStatBases(☃, Blocks.LIT_PUMPKIN, Blocks.PUMPKIN);
      mergeStatBases(☃, Blocks.LIT_FURNACE, Blocks.FURNACE);
      mergeStatBases(☃, Blocks.LIT_REDSTONE_ORE, Blocks.REDSTONE_ORE);
      mergeStatBases(☃, Blocks.POWERED_REPEATER, Blocks.UNPOWERED_REPEATER);
      mergeStatBases(☃, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_COMPARATOR);
      mergeStatBases(☃, Blocks.REDSTONE_TORCH, Blocks.UNLIT_REDSTONE_TORCH);
      mergeStatBases(☃, Blocks.LIT_REDSTONE_LAMP, Blocks.REDSTONE_LAMP);
      mergeStatBases(☃, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB);
      mergeStatBases(☃, Blocks.DOUBLE_WOODEN_SLAB, Blocks.WOODEN_SLAB);
      mergeStatBases(☃, Blocks.DOUBLE_STONE_SLAB2, Blocks.STONE_SLAB2);
      mergeStatBases(☃, Blocks.GRASS, Blocks.DIRT);
      mergeStatBases(☃, Blocks.FARMLAND, Blocks.DIRT);
   }

   private static void mergeStatBases(StatBase[] var0, Block var1, Block var2) {
      int ☃ = Block.getIdFromBlock(☃);
      int ☃x = Block.getIdFromBlock(☃);
      if (☃[☃] != null && ☃[☃x] == null) {
         ☃[☃x] = ☃[☃];
      } else {
         ALL_STATS.remove(☃[☃]);
         MINE_BLOCK_STATS.remove(☃[☃]);
         BASIC_STATS.remove(☃[☃]);
         ☃[☃] = ☃[☃x];
      }
   }

   public static StatBase getStatKillEntity(EntityList.EntityEggInfo var0) {
      String ☃ = EntityList.getTranslationName(☃.spawnedID);
      return ☃ == null
         ? null
         : new StatBase("stat.killEntity." + ☃, new TextComponentTranslation("stat.entityKill", new TextComponentTranslation("entity." + ☃ + ".name")))
            .registerStat();
   }

   public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo var0) {
      String ☃ = EntityList.getTranslationName(☃.spawnedID);
      return ☃ == null
         ? null
         : new StatBase("stat.entityKilledBy." + ☃, new TextComponentTranslation("stat.entityKilledBy", new TextComponentTranslation("entity." + ☃ + ".name")))
            .registerStat();
   }

   @Nullable
   public static StatBase getOneShotStat(String var0) {
      return ID_TO_STAT_MAP.get(☃);
   }
}
