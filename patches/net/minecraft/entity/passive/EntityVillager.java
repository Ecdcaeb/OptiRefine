package net.minecraft.entity.passive;

import java.util.Locale;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityVillager.class, DataSerializers.VARINT);
   private int randomTickDivider;
   private boolean isMating;
   private boolean isPlaying;
   Village village;
   @Nullable
   private EntityPlayer buyingPlayer;
   @Nullable
   private MerchantRecipeList buyingList;
   private int timeUntilReset;
   private boolean needsInitilization;
   private boolean isWillingToMate;
   private int wealth;
   private String lastBuyingPlayer;
   private int careerId;
   private int careerLevel;
   private boolean isLookingForHome;
   private boolean areAdditionalTasksSet;
   private final InventoryBasic villagerInventory = new InventoryBasic("Items", false, 8);
   private static final EntityVillager.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new EntityVillager.ITradeList[][][][]{
      {
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.WHEAT, new EntityVillager.PriceInfo(18, 22)),
                        new EntityVillager.EmeraldForItems(Items.POTATO, new EntityVillager.PriceInfo(15, 19)),
                        new EntityVillager.EmeraldForItems(Items.CARROT, new EntityVillager.PriceInfo(15, 19)),
                        new EntityVillager.ListItemForEmeralds(Items.BREAD, new EntityVillager.PriceInfo(-4, -2))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new EntityVillager.PriceInfo(8, 13)),
                        new EntityVillager.ListItemForEmeralds(Items.PUMPKIN_PIE, new EntityVillager.PriceInfo(-3, -2))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new EntityVillager.PriceInfo(7, 12)),
                        new EntityVillager.ListItemForEmeralds(Items.APPLE, new EntityVillager.PriceInfo(-7, -5))
                  },
                  {
                        new EntityVillager.ListItemForEmeralds(Items.COOKIE, new EntityVillager.PriceInfo(-10, -6)),
                        new EntityVillager.ListItemForEmeralds(Items.CAKE, new EntityVillager.PriceInfo(1, 1))
                  }
            },
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.STRING, new EntityVillager.PriceInfo(15, 20)),
                        new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
                        new EntityVillager.ItemAndEmeraldToItem(
                           Items.FISH, new EntityVillager.PriceInfo(6, 6), Items.COOKED_FISH, new EntityVillager.PriceInfo(6, 6)
                        )
                  },
                  {new EntityVillager.ListEnchantedItemForEmeralds(Items.FISHING_ROD, new EntityVillager.PriceInfo(7, 8))}
            },
            {
                  {
                        new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new EntityVillager.PriceInfo(16, 22)),
                        new EntityVillager.ListItemForEmeralds(Items.SHEARS, new EntityVillager.PriceInfo(3, 4))
                  },
                  {
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new EntityVillager.PriceInfo(1, 2)),
                        new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new EntityVillager.PriceInfo(1, 2))
                  }
            },
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.STRING, new EntityVillager.PriceInfo(15, 20)),
                        new EntityVillager.ListItemForEmeralds(Items.ARROW, new EntityVillager.PriceInfo(-12, -8))
                  },
                  {
                        new EntityVillager.ListItemForEmeralds(Items.BOW, new EntityVillager.PriceInfo(2, 3)),
                        new EntityVillager.ItemAndEmeraldToItem(
                           Item.getItemFromBlock(Blocks.GRAVEL), new EntityVillager.PriceInfo(10, 10), Items.FLINT, new EntityVillager.PriceInfo(6, 10)
                        )
                  }
            }
      },
      {
            {
                  {new EntityVillager.EmeraldForItems(Items.PAPER, new EntityVillager.PriceInfo(24, 36)), new EntityVillager.ListEnchantedBookForEmeralds()},
                  {
                        new EntityVillager.EmeraldForItems(Items.BOOK, new EntityVillager.PriceInfo(8, 10)),
                        new EntityVillager.ListItemForEmeralds(Items.COMPASS, new EntityVillager.PriceInfo(10, 12)),
                        new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new EntityVillager.PriceInfo(3, 4))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.WRITTEN_BOOK, new EntityVillager.PriceInfo(2, 2)),
                        new EntityVillager.ListItemForEmeralds(Items.CLOCK, new EntityVillager.PriceInfo(10, 12)),
                        new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new EntityVillager.PriceInfo(-5, -3))
                  },
                  {new EntityVillager.ListEnchantedBookForEmeralds()},
                  {new EntityVillager.ListEnchantedBookForEmeralds()},
                  {new EntityVillager.ListItemForEmeralds(Items.NAME_TAG, new EntityVillager.PriceInfo(20, 22))}
            },
            {
                  {new EntityVillager.EmeraldForItems(Items.PAPER, new EntityVillager.PriceInfo(24, 36))},
                  {new EntityVillager.EmeraldForItems(Items.COMPASS, new EntityVillager.PriceInfo(1, 1))},
                  {new EntityVillager.ListItemForEmeralds(Items.MAP, new EntityVillager.PriceInfo(7, 11))},
                  {
                        new EntityVillager.TreasureMapForEmeralds(new EntityVillager.PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT),
                        new EntityVillager.TreasureMapForEmeralds(new EntityVillager.PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION)
                  }
            }
      },
      {
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.ROTTEN_FLESH, new EntityVillager.PriceInfo(36, 40)),
                        new EntityVillager.EmeraldForItems(Items.GOLD_INGOT, new EntityVillager.PriceInfo(8, 10))
                  },
                  {
                        new EntityVillager.ListItemForEmeralds(Items.REDSTONE, new EntityVillager.PriceInfo(-4, -1)),
                        new EntityVillager.ListItemForEmeralds(
                           new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new EntityVillager.PriceInfo(-2, -1)
                        )
                  },
                  {
                        new EntityVillager.ListItemForEmeralds(Items.ENDER_PEARL, new EntityVillager.PriceInfo(4, 7)),
                        new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new EntityVillager.PriceInfo(-3, -1))
                  },
                  {new EntityVillager.ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new EntityVillager.PriceInfo(3, 11))}
            }
      },
      {
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
                        new EntityVillager.ListItemForEmeralds(Items.IRON_HELMET, new EntityVillager.PriceInfo(4, 6))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)),
                        new EntityVillager.ListItemForEmeralds(Items.IRON_CHESTPLATE, new EntityVillager.PriceInfo(10, 14))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)),
                        new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new EntityVillager.PriceInfo(16, 19))
                  },
                  {
                        new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new EntityVillager.PriceInfo(5, 7)),
                        new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new EntityVillager.PriceInfo(9, 11)),
                        new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_HELMET, new EntityVillager.PriceInfo(5, 7)),
                        new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new EntityVillager.PriceInfo(11, 15))
                  }
            },
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
                        new EntityVillager.ListItemForEmeralds(Items.IRON_AXE, new EntityVillager.PriceInfo(6, 8))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)),
                        new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_SWORD, new EntityVillager.PriceInfo(9, 10))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)),
                        new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new EntityVillager.PriceInfo(12, 15)),
                        new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new EntityVillager.PriceInfo(9, 12))
                  }
            },
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
                        new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new EntityVillager.PriceInfo(5, 7))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)),
                        new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new EntityVillager.PriceInfo(9, 11))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)),
                        new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new EntityVillager.PriceInfo(12, 15))
                  }
            }
      },
      {
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.PORKCHOP, new EntityVillager.PriceInfo(14, 18)),
                        new EntityVillager.EmeraldForItems(Items.CHICKEN, new EntityVillager.PriceInfo(14, 18))
                  },
                  {
                        new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
                        new EntityVillager.ListItemForEmeralds(Items.COOKED_PORKCHOP, new EntityVillager.PriceInfo(-7, -5)),
                        new EntityVillager.ListItemForEmeralds(Items.COOKED_CHICKEN, new EntityVillager.PriceInfo(-8, -6))
                  }
            },
            {
                  {
                        new EntityVillager.EmeraldForItems(Items.LEATHER, new EntityVillager.PriceInfo(9, 12)),
                        new EntityVillager.ListItemForEmeralds(Items.LEATHER_LEGGINGS, new EntityVillager.PriceInfo(2, 4))
                  },
                  {new EntityVillager.ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new EntityVillager.PriceInfo(7, 12))},
                  {new EntityVillager.ListItemForEmeralds(Items.SADDLE, new EntityVillager.PriceInfo(8, 10))}
            }
      },
      {new EntityVillager.ITradeList[0][]}
   };

   public EntityVillager(World var1) {
      this(☃, 0);
   }

   public EntityVillager(World var1, int var2) {
      super(☃);
      this.setProfession(☃);
      this.setSize(0.6F, 1.95F);
      ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
      this.setCanPickUpLoot(true);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityZombie.class, 8.0F, 0.6, 0.6));
      this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityEvoker.class, 12.0F, 0.8, 0.8));
      this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityVindicator.class, 8.0F, 0.8, 0.8));
      this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityVex.class, 8.0F, 0.6, 0.6));
      this.tasks.addTask(1, new EntityAITradePlayer(this));
      this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
      this.tasks.addTask(2, new EntityAIMoveIndoors(this));
      this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
      this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
      this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6));
      this.tasks.addTask(6, new EntityAIVillagerMate(this));
      this.tasks.addTask(7, new EntityAIFollowGolem(this));
      this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      this.tasks.addTask(9, new EntityAIVillagerInteract(this));
      this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
   }

   private void setAdditionalAItasks() {
      if (!this.areAdditionalTasksSet) {
         this.areAdditionalTasksSet = true;
         if (this.isChild()) {
            this.tasks.addTask(8, new EntityAIPlay(this, 0.32));
         } else if (this.getProfession() == 0) {
            this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6));
         }
      }
   }

   @Override
   protected void onGrowingAdult() {
      if (this.getProfession() == 0) {
         this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6));
      }

      super.onGrowingAdult();
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
   }

   @Override
   protected void updateAITasks() {
      if (--this.randomTickDivider <= 0) {
         BlockPos ☃ = new BlockPos(this);
         this.world.getVillageCollection().addToVillagerPositionList(☃);
         this.randomTickDivider = 70 + this.rand.nextInt(50);
         this.village = this.world.getVillageCollection().getNearestVillage(☃, 32);
         if (this.village == null) {
            this.detachHome();
         } else {
            BlockPos ☃x = this.village.getCenter();
            this.setHomePosAndDistance(☃x, this.village.getVillageRadius());
            if (this.isLookingForHome) {
               this.isLookingForHome = false;
               this.village.setDefaultPlayerReputation(5);
            }
         }
      }

      if (!this.isTrading() && this.timeUntilReset > 0) {
         this.timeUntilReset--;
         if (this.timeUntilReset <= 0) {
            if (this.needsInitilization) {
               for (MerchantRecipe ☃ : this.buyingList) {
                  if (☃.isRecipeDisabled()) {
                     ☃.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                  }
               }

               this.populateBuyingList();
               this.needsInitilization = false;
               if (this.village != null && this.lastBuyingPlayer != null) {
                  this.world.setEntityState(this, (byte)14);
                  this.village.modifyPlayerReputation(this.lastBuyingPlayer, 1);
               }
            }

            this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
         }
      }

      super.updateAITasks();
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      boolean ☃x = ☃.getItem() == Items.NAME_TAG;
      if (☃x) {
         ☃.interactWithEntity(☃, this, ☃);
         return true;
      } else if (!this.holdingSpawnEggOfClass(☃, (Class<? extends Entity>)this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
         if (this.buyingList == null) {
            this.populateBuyingList();
         }

         if (☃ == EnumHand.MAIN_HAND) {
            ☃.addStat(StatList.TALKED_TO_VILLAGER);
         }

         if (!this.world.isRemote && !this.buyingList.isEmpty()) {
            this.setCustomer(☃);
            ☃.displayVillagerTradeGui(this);
         } else if (this.buyingList.isEmpty()) {
            return super.processInteract(☃, ☃);
         }

         return true;
      } else {
         return super.processInteract(☃, ☃);
      }
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(PROFESSION, 0);
   }

   public static void registerFixesVillager(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityVillager.class);
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityVillager.class, "Inventory"));
      ☃.registerWalker(FixTypes.ENTITY, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (EntityList.getKey(EntityVillager.class).equals(new ResourceLocation(☃.getString("id"))) && ☃.hasKey("Offers", 10)) {
               NBTTagCompound ☃ = ☃.getCompoundTag("Offers");
               if (☃.hasKey("Recipes", 9)) {
                  NBTTagList ☃x = ☃.getTagList("Recipes", 10);

                  for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
                     NBTTagCompound ☃xxx = ☃x.getCompoundTagAt(☃xx);
                     DataFixesManager.processItemStack(☃, ☃xxx, ☃, "buy");
                     DataFixesManager.processItemStack(☃, ☃xxx, ☃, "buyB");
                     DataFixesManager.processItemStack(☃, ☃xxx, ☃, "sell");
                     ☃x.set(☃xx, ☃xxx);
                  }
               }
            }

            return ☃;
         }
      });
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Profession", this.getProfession());
      ☃.setInteger("Riches", this.wealth);
      ☃.setInteger("Career", this.careerId);
      ☃.setInteger("CareerLevel", this.careerLevel);
      ☃.setBoolean("Willing", this.isWillingToMate);
      if (this.buyingList != null) {
         ☃.setTag("Offers", this.buyingList.getRecipiesAsTags());
      }

      NBTTagList ☃ = new NBTTagList();

      for (int ☃x = 0; ☃x < this.villagerInventory.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = this.villagerInventory.getStackInSlot(☃x);
         if (!☃xx.isEmpty()) {
            ☃.appendTag(☃xx.writeToNBT(new NBTTagCompound()));
         }
      }

      ☃.setTag("Inventory", ☃);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setProfession(☃.getInteger("Profession"));
      this.wealth = ☃.getInteger("Riches");
      this.careerId = ☃.getInteger("Career");
      this.careerLevel = ☃.getInteger("CareerLevel");
      this.isWillingToMate = ☃.getBoolean("Willing");
      if (☃.hasKey("Offers", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("Offers");
         this.buyingList = new MerchantRecipeList(☃);
      }

      NBTTagList ☃ = ☃.getTagList("Inventory", 10);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         ItemStack ☃xx = new ItemStack(☃.getCompoundTagAt(☃x));
         if (!☃xx.isEmpty()) {
            this.villagerInventory.addItem(☃xx);
         }
      }

      this.setCanPickUpLoot(true);
      this.setAdditionalAItasks();
   }

   @Override
   protected boolean canDespawn() {
      return false;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return this.isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_VILLAGER_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_VILLAGER_DEATH;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_VILLAGER;
   }

   public void setProfession(int var1) {
      this.dataManager.set(PROFESSION, ☃);
   }

   public int getProfession() {
      return Math.max(this.dataManager.get(PROFESSION) % 6, 0);
   }

   public boolean isMating() {
      return this.isMating;
   }

   public void setMating(boolean var1) {
      this.isMating = ☃;
   }

   public void setPlaying(boolean var1) {
      this.isPlaying = ☃;
   }

   public boolean isPlaying() {
      return this.isPlaying;
   }

   @Override
   public void setRevengeTarget(@Nullable EntityLivingBase var1) {
      super.setRevengeTarget(☃);
      if (this.village != null && ☃ != null) {
         this.village.addOrRenewAgressor(☃);
         if (☃ instanceof EntityPlayer) {
            int ☃ = -1;
            if (this.isChild()) {
               ☃ = -3;
            }

            this.village.modifyPlayerReputation(☃.getName(), ☃);
            if (this.isEntityAlive()) {
               this.world.setEntityState(this, (byte)13);
            }
         }
      }
   }

   @Override
   public void onDeath(DamageSource var1) {
      if (this.village != null) {
         Entity ☃ = ☃.getTrueSource();
         if (☃ != null) {
            if (☃ instanceof EntityPlayer) {
               this.village.modifyPlayerReputation(☃.getName(), -2);
            } else if (☃ instanceof IMob) {
               this.village.endMatingSeason();
            }
         } else {
            EntityPlayer ☃x = this.world.getClosestPlayerToEntity(this, 16.0);
            if (☃x != null) {
               this.village.endMatingSeason();
            }
         }
      }

      super.onDeath(☃);
   }

   @Override
   public void setCustomer(@Nullable EntityPlayer var1) {
      this.buyingPlayer = ☃;
   }

   @Nullable
   @Override
   public EntityPlayer getCustomer() {
      return this.buyingPlayer;
   }

   public boolean isTrading() {
      return this.buyingPlayer != null;
   }

   public boolean getIsWillingToMate(boolean var1) {
      if (!this.isWillingToMate && ☃ && this.hasEnoughFoodToBreed()) {
         boolean ☃ = false;

         for (int ☃x = 0; ☃x < this.villagerInventory.getSizeInventory(); ☃x++) {
            ItemStack ☃xx = this.villagerInventory.getStackInSlot(☃x);
            if (!☃xx.isEmpty()) {
               if (☃xx.getItem() == Items.BREAD && ☃xx.getCount() >= 3) {
                  ☃ = true;
                  this.villagerInventory.decrStackSize(☃x, 3);
               } else if ((☃xx.getItem() == Items.POTATO || ☃xx.getItem() == Items.CARROT) && ☃xx.getCount() >= 12) {
                  ☃ = true;
                  this.villagerInventory.decrStackSize(☃x, 12);
               }
            }

            if (☃) {
               this.world.setEntityState(this, (byte)18);
               this.isWillingToMate = true;
               break;
            }
         }
      }

      return this.isWillingToMate;
   }

   public void setIsWillingToMate(boolean var1) {
      this.isWillingToMate = ☃;
   }

   @Override
   public void useRecipe(MerchantRecipe var1) {
      ☃.incrementToolUses();
      this.livingSoundTime = -this.getTalkInterval();
      this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
      int ☃ = 3 + this.rand.nextInt(4);
      if (☃.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
         this.timeUntilReset = 40;
         this.needsInitilization = true;
         this.isWillingToMate = true;
         if (this.buyingPlayer != null) {
            this.lastBuyingPlayer = this.buyingPlayer.getName();
         } else {
            this.lastBuyingPlayer = null;
         }

         ☃ += 5;
      }

      if (☃.getItemToBuy().getItem() == Items.EMERALD) {
         this.wealth = this.wealth + ☃.getItemToBuy().getCount();
      }

      if (☃.getRewardsExp()) {
         this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5, this.posZ, ☃));
      }

      if (this.buyingPlayer instanceof EntityPlayerMP) {
         CriteriaTriggers.VILLAGER_TRADE.trigger((EntityPlayerMP)this.buyingPlayer, this, ☃.getItemToSell());
      }
   }

   @Override
   public void verifySellingItem(ItemStack var1) {
      if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
         this.livingSoundTime = -this.getTalkInterval();
         this.playSound(☃.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
      }
   }

   @Nullable
   @Override
   public MerchantRecipeList getRecipes(EntityPlayer var1) {
      if (this.buyingList == null) {
         this.populateBuyingList();
      }

      return this.buyingList;
   }

   private void populateBuyingList() {
      EntityVillager.ITradeList[][][] ☃ = DEFAULT_TRADE_LIST_MAP[this.getProfession()];
      if (this.careerId != 0 && this.careerLevel != 0) {
         this.careerLevel++;
      } else {
         this.careerId = this.rand.nextInt(☃.length) + 1;
         this.careerLevel = 1;
      }

      if (this.buyingList == null) {
         this.buyingList = new MerchantRecipeList();
      }

      int ☃x = this.careerId - 1;
      int ☃xx = this.careerLevel - 1;
      if (☃x >= 0 && ☃x < ☃.length) {
         EntityVillager.ITradeList[][] ☃xxx = ☃[☃x];
         if (☃xx >= 0 && ☃xx < ☃xxx.length) {
            EntityVillager.ITradeList[] ☃xxxx = ☃xxx[☃xx];

            for (EntityVillager.ITradeList ☃xxxxx : ☃xxxx) {
               ☃xxxxx.addMerchantRecipe(this, this.buyingList, this.rand);
            }
         }
      }
   }

   @Override
   public void setRecipes(@Nullable MerchantRecipeList var1) {
   }

   @Override
   public World getWorld() {
      return this.world;
   }

   @Override
   public BlockPos getPos() {
      return new BlockPos(this);
   }

   @Override
   public ITextComponent getDisplayName() {
      Team ☃ = this.getTeam();
      String ☃x = this.getCustomNameTag();
      if (☃x != null && !☃x.isEmpty()) {
         TextComponentString ☃xx = new TextComponentString(ScorePlayerTeam.formatPlayerName(☃, ☃x));
         ☃xx.getStyle().setHoverEvent(this.getHoverEvent());
         ☃xx.getStyle().setInsertion(this.getCachedUniqueIdString());
         return ☃xx;
      } else {
         if (this.buyingList == null) {
            this.populateBuyingList();
         }

         String ☃xx = null;
         switch (this.getProfession()) {
            case 0:
               if (this.careerId == 1) {
                  ☃xx = "farmer";
               } else if (this.careerId == 2) {
                  ☃xx = "fisherman";
               } else if (this.careerId == 3) {
                  ☃xx = "shepherd";
               } else if (this.careerId == 4) {
                  ☃xx = "fletcher";
               }
               break;
            case 1:
               if (this.careerId == 1) {
                  ☃xx = "librarian";
               } else if (this.careerId == 2) {
                  ☃xx = "cartographer";
               }
               break;
            case 2:
               ☃xx = "cleric";
               break;
            case 3:
               if (this.careerId == 1) {
                  ☃xx = "armor";
               } else if (this.careerId == 2) {
                  ☃xx = "weapon";
               } else if (this.careerId == 3) {
                  ☃xx = "tool";
               }
               break;
            case 4:
               if (this.careerId == 1) {
                  ☃xx = "butcher";
               } else if (this.careerId == 2) {
                  ☃xx = "leather";
               }
               break;
            case 5:
               ☃xx = "nitwit";
         }

         if (☃xx != null) {
            ITextComponent ☃xx = new TextComponentTranslation("entity.Villager." + ☃xx);
            ☃xx.getStyle().setHoverEvent(this.getHoverEvent());
            ☃xx.getStyle().setInsertion(this.getCachedUniqueIdString());
            if (☃ != null) {
               ☃xx.getStyle().setColor(☃.getColor());
            }

            return ☃xx;
         } else {
            return super.getDisplayName();
         }
      }
   }

   @Override
   public float getEyeHeight() {
      return this.isChild() ? 0.81F : 1.62F;
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 12) {
         this.spawnParticles(EnumParticleTypes.HEART);
      } else if (☃ == 13) {
         this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
      } else if (☃ == 14) {
         this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   private void spawnParticles(EnumParticleTypes var1) {
      for (int ☃ = 0; ☃ < 5; ☃++) {
         double ☃x = this.rand.nextGaussian() * 0.02;
         double ☃xx = this.rand.nextGaussian() * 0.02;
         double ☃xxx = this.rand.nextGaussian() * 0.02;
         this.world
            .spawnParticle(
               ☃,
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 1.0 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               ☃x,
               ☃xx,
               ☃xxx
            );
      }
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      return this.finalizeMobSpawn(☃, ☃, true);
   }

   public IEntityLivingData finalizeMobSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2, boolean var3) {
      ☃ = super.onInitialSpawn(☃, ☃);
      if (☃) {
         this.setProfession(this.world.rand.nextInt(6));
      }

      this.setAdditionalAItasks();
      this.populateBuyingList();
      return ☃;
   }

   public void setLookingForHome() {
      this.isLookingForHome = true;
   }

   public EntityVillager createChild(EntityAgeable var1) {
      EntityVillager ☃ = new EntityVillager(this.world);
      ☃.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(☃)), null);
      return ☃;
   }

   @Override
   public boolean canBeLeashedTo(EntityPlayer var1) {
      return false;
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt var1) {
      if (!this.world.isRemote && !this.isDead) {
         EntityWitch ☃ = new EntityWitch(this.world);
         ☃.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         ☃.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(☃)), null);
         ☃.setNoAI(this.isAIDisabled());
         if (this.hasCustomName()) {
            ☃.setCustomNameTag(this.getCustomNameTag());
            ☃.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
         }

         this.world.spawnEntity(☃);
         this.setDead();
      }
   }

   public InventoryBasic getVillagerInventory() {
      return this.villagerInventory;
   }

   @Override
   protected void updateEquipmentIfNeeded(EntityItem var1) {
      ItemStack ☃ = ☃.getItem();
      Item ☃x = ☃.getItem();
      if (this.canVillagerPickupItem(☃x)) {
         ItemStack ☃xx = this.villagerInventory.addItem(☃);
         if (☃xx.isEmpty()) {
            ☃.setDead();
         } else {
            ☃.setCount(☃xx.getCount());
         }
      }
   }

   private boolean canVillagerPickupItem(Item var1) {
      return ☃ == Items.BREAD
         || ☃ == Items.POTATO
         || ☃ == Items.CARROT
         || ☃ == Items.WHEAT
         || ☃ == Items.WHEAT_SEEDS
         || ☃ == Items.BEETROOT
         || ☃ == Items.BEETROOT_SEEDS;
   }

   public boolean hasEnoughFoodToBreed() {
      return this.hasEnoughItems(1);
   }

   public boolean canAbondonItems() {
      return this.hasEnoughItems(2);
   }

   public boolean wantsMoreFood() {
      boolean ☃ = this.getProfession() == 0;
      return ☃ ? !this.hasEnoughItems(5) : !this.hasEnoughItems(1);
   }

   private boolean hasEnoughItems(int var1) {
      boolean ☃ = this.getProfession() == 0;

      for (int ☃x = 0; ☃x < this.villagerInventory.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = this.villagerInventory.getStackInSlot(☃x);
         if (!☃xx.isEmpty()) {
            if (☃xx.getItem() == Items.BREAD && ☃xx.getCount() >= 3 * ☃
               || ☃xx.getItem() == Items.POTATO && ☃xx.getCount() >= 12 * ☃
               || ☃xx.getItem() == Items.CARROT && ☃xx.getCount() >= 12 * ☃
               || ☃xx.getItem() == Items.BEETROOT && ☃xx.getCount() >= 12 * ☃) {
               return true;
            }

            if (☃ && ☃xx.getItem() == Items.WHEAT && ☃xx.getCount() >= 9 * ☃) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isFarmItemInInventory() {
      for (int ☃ = 0; ☃ < this.villagerInventory.getSizeInventory(); ☃++) {
         ItemStack ☃x = this.villagerInventory.getStackInSlot(☃);
         if (!☃x.isEmpty()
            && (☃x.getItem() == Items.WHEAT_SEEDS || ☃x.getItem() == Items.POTATO || ☃x.getItem() == Items.CARROT || ☃x.getItem() == Items.BEETROOT_SEEDS)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      if (super.replaceItemInInventory(☃, ☃)) {
         return true;
      } else {
         int ☃ = ☃ - 300;
         if (☃ >= 0 && ☃ < this.villagerInventory.getSizeInventory()) {
            this.villagerInventory.setInventorySlotContents(☃, ☃);
            return true;
         } else {
            return false;
         }
      }
   }

   static class EmeraldForItems implements EntityVillager.ITradeList {
      public Item buyingItem;
      public EntityVillager.PriceInfo price;

      public EmeraldForItems(Item var1, EntityVillager.PriceInfo var2) {
         this.buyingItem = ☃;
         this.price = ☃;
      }

      @Override
      public void addMerchantRecipe(IMerchant var1, MerchantRecipeList var2, Random var3) {
         int ☃ = 1;
         if (this.price != null) {
            ☃ = this.price.getPrice(☃);
         }

         ☃.add(new MerchantRecipe(new ItemStack(this.buyingItem, ☃, 0), Items.EMERALD));
      }
   }

   interface ITradeList {
      void addMerchantRecipe(IMerchant var1, MerchantRecipeList var2, Random var3);
   }

   static class ItemAndEmeraldToItem implements EntityVillager.ITradeList {
      public ItemStack buyingItemStack;
      public EntityVillager.PriceInfo buyingPriceInfo;
      public ItemStack sellingItemstack;
      public EntityVillager.PriceInfo sellingPriceInfo;

      public ItemAndEmeraldToItem(Item var1, EntityVillager.PriceInfo var2, Item var3, EntityVillager.PriceInfo var4) {
         this.buyingItemStack = new ItemStack(☃);
         this.buyingPriceInfo = ☃;
         this.sellingItemstack = new ItemStack(☃);
         this.sellingPriceInfo = ☃;
      }

      @Override
      public void addMerchantRecipe(IMerchant var1, MerchantRecipeList var2, Random var3) {
         int ☃ = this.buyingPriceInfo.getPrice(☃);
         int ☃x = this.sellingPriceInfo.getPrice(☃);
         ☃.add(
            new MerchantRecipe(
               new ItemStack(this.buyingItemStack.getItem(), ☃, this.buyingItemStack.getMetadata()),
               new ItemStack(Items.EMERALD),
               new ItemStack(this.sellingItemstack.getItem(), ☃x, this.sellingItemstack.getMetadata())
            )
         );
      }
   }

   static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList {
      public ListEnchantedBookForEmeralds() {
      }

      @Override
      public void addMerchantRecipe(IMerchant var1, MerchantRecipeList var2, Random var3) {
         Enchantment ☃ = Enchantment.REGISTRY.getRandomObject(☃);
         int ☃x = MathHelper.getInt(☃, ☃.getMinLevel(), ☃.getMaxLevel());
         ItemStack ☃xx = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(☃, ☃x));
         int ☃xxx = 2 + ☃.nextInt(5 + ☃x * 10) + 3 * ☃x;
         if (☃.isTreasureEnchantment()) {
            ☃xxx *= 2;
         }

         if (☃xxx > 64) {
            ☃xxx = 64;
         }

         ☃.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, ☃xxx), ☃xx));
      }
   }

   static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList {
      public ItemStack enchantedItemStack;
      public EntityVillager.PriceInfo priceInfo;

      public ListEnchantedItemForEmeralds(Item var1, EntityVillager.PriceInfo var2) {
         this.enchantedItemStack = new ItemStack(☃);
         this.priceInfo = ☃;
      }

      @Override
      public void addMerchantRecipe(IMerchant var1, MerchantRecipeList var2, Random var3) {
         int ☃ = 1;
         if (this.priceInfo != null) {
            ☃ = this.priceInfo.getPrice(☃);
         }

         ItemStack ☃x = new ItemStack(Items.EMERALD, ☃, 0);
         ItemStack ☃xx = EnchantmentHelper.addRandomEnchantment(
            ☃, new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata()), 5 + ☃.nextInt(15), false
         );
         ☃.add(new MerchantRecipe(☃x, ☃xx));
      }
   }

   static class ListItemForEmeralds implements EntityVillager.ITradeList {
      public ItemStack itemToBuy;
      public EntityVillager.PriceInfo priceInfo;

      public ListItemForEmeralds(Item var1, EntityVillager.PriceInfo var2) {
         this.itemToBuy = new ItemStack(☃);
         this.priceInfo = ☃;
      }

      public ListItemForEmeralds(ItemStack var1, EntityVillager.PriceInfo var2) {
         this.itemToBuy = ☃;
         this.priceInfo = ☃;
      }

      @Override
      public void addMerchantRecipe(IMerchant var1, MerchantRecipeList var2, Random var3) {
         int ☃ = 1;
         if (this.priceInfo != null) {
            ☃ = this.priceInfo.getPrice(☃);
         }

         ItemStack ☃x;
         ItemStack ☃xx;
         if (☃ < 0) {
            ☃x = new ItemStack(Items.EMERALD);
            ☃xx = new ItemStack(this.itemToBuy.getItem(), -☃, this.itemToBuy.getMetadata());
         } else {
            ☃x = new ItemStack(Items.EMERALD, ☃, 0);
            ☃xx = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
         }

         ☃.add(new MerchantRecipe(☃x, ☃xx));
      }
   }

   static class PriceInfo extends Tuple<Integer, Integer> {
      public PriceInfo(int var1, int var2) {
         super(☃, ☃);
         if (☃ < ☃) {
            EntityVillager.LOGGER.warn("PriceRange({}, {}) invalid, {} smaller than {}", ☃, ☃, ☃, ☃);
         }
      }

      public int getPrice(Random var1) {
         return this.getFirst() >= this.getSecond() ? this.getFirst() : this.getFirst() + ☃.nextInt(this.getSecond() - this.getFirst() + 1);
      }
   }

   static class TreasureMapForEmeralds implements EntityVillager.ITradeList {
      public EntityVillager.PriceInfo value;
      public String destination;
      public MapDecoration.Type destinationType;

      public TreasureMapForEmeralds(EntityVillager.PriceInfo var1, String var2, MapDecoration.Type var3) {
         this.value = ☃;
         this.destination = ☃;
         this.destinationType = ☃;
      }

      @Override
      public void addMerchantRecipe(IMerchant var1, MerchantRecipeList var2, Random var3) {
         int ☃ = this.value.getPrice(☃);
         World ☃x = ☃.getWorld();
         BlockPos ☃xx = ☃x.findNearestStructure(this.destination, ☃.getPos(), true);
         if (☃xx != null) {
            ItemStack ☃xxx = ItemMap.setupNewMap(☃x, ☃xx.getX(), ☃xx.getZ(), (byte)2, true, true);
            ItemMap.renderBiomePreviewMap(☃x, ☃xxx);
            MapData.addTargetDecoration(☃xxx, ☃xx, "+", this.destinationType);
            ☃xxx.setTranslatableName("filled_map." + this.destination.toLowerCase(Locale.ROOT));
            ☃.add(new MerchantRecipe(new ItemStack(Items.EMERALD, ☃), new ItemStack(Items.COMPASS), ☃xxx));
         }
      }
   }
}
