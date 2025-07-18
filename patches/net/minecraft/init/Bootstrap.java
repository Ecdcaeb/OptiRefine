package net.minecraft.init;

import com.mojang.authlib.GameProfile;
import java.io.PrintStream;
import java.util.Random;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.server.DebugLoggingPrintStream;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bootstrap {
   public static final PrintStream SYSOUT = System.out;
   private static boolean alreadyRegistered;
   public static boolean hasErrored;
   private static final Logger LOGGER = LogManager.getLogger();

   public static boolean isRegistered() {
      return alreadyRegistered;
   }

   static void registerDispenserBehaviors() {
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ARROW, new BehaviorProjectileDispense() {
         @Override
         protected IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3) {
            EntityTippedArrow ☃ = new EntityTippedArrow(☃, ☃.getX(), ☃.getY(), ☃.getZ());
            ☃.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
            return ☃;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.TIPPED_ARROW, new BehaviorProjectileDispense() {
         @Override
         protected IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3) {
            EntityTippedArrow ☃ = new EntityTippedArrow(☃, ☃.getX(), ☃.getY(), ☃.getZ());
            ☃.setPotionEffect(☃);
            ☃.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
            return ☃;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPECTRAL_ARROW, new BehaviorProjectileDispense() {
         @Override
         protected IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3) {
            EntityArrow ☃ = new EntitySpectralArrow(☃, ☃.getX(), ☃.getY(), ☃.getZ());
            ☃.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
            return ☃;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EGG, new BehaviorProjectileDispense() {
         @Override
         protected IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3) {
            return new EntityEgg(☃, ☃.getX(), ☃.getY(), ☃.getZ());
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SNOWBALL, new BehaviorProjectileDispense() {
         @Override
         protected IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3) {
            return new EntitySnowball(☃, ☃.getX(), ☃.getY(), ☃.getZ());
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EXPERIENCE_BOTTLE, new BehaviorProjectileDispense() {
         @Override
         protected IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3) {
            return new EntityExpBottle(☃, ☃.getX(), ☃.getY(), ☃.getZ());
         }

         @Override
         protected float getProjectileInaccuracy() {
            return super.getProjectileInaccuracy() * 0.5F;
         }

         @Override
         protected float getProjectileVelocity() {
            return super.getProjectileVelocity() * 1.25F;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPLASH_POTION, new IBehaviorDispenseItem() {
         @Override
         public ItemStack dispense(IBlockSource var1, final ItemStack var2) {
            return (new BehaviorProjectileDispense() {
               @Override
               protected IProjectile getProjectileEntity(World var1, IPosition var2x, ItemStack var3) {
                  return new EntityPotion(☃, ☃.getX(), ☃.getY(), ☃.getZ(), ☃.copy());
               }

               @Override
               protected float getProjectileInaccuracy() {
                  return super.getProjectileInaccuracy() * 0.5F;
               }

               @Override
               protected float getProjectileVelocity() {
                  return super.getProjectileVelocity() * 1.25F;
               }
            }).dispense(☃, ☃);
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LINGERING_POTION, new IBehaviorDispenseItem() {
         @Override
         public ItemStack dispense(IBlockSource var1, final ItemStack var2) {
            return (new BehaviorProjectileDispense() {
               @Override
               protected IProjectile getProjectileEntity(World var1, IPosition var2x, ItemStack var3) {
                  return new EntityPotion(☃, ☃.getX(), ☃.getY(), ☃.getZ(), ☃.copy());
               }

               @Override
               protected float getProjectileInaccuracy() {
                  return super.getProjectileInaccuracy() * 0.5F;
               }

               @Override
               protected float getProjectileVelocity() {
                  return super.getProjectileVelocity() * 1.25F;
               }
            }).dispense(☃, ☃);
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPAWN_EGG, new BehaviorDefaultDispenseItem() {
         @Override
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            EnumFacing ☃ = ☃.getBlockState().getValue(BlockDispenser.FACING);
            double ☃x = ☃.getX() + ☃.getXOffset();
            double ☃xx = ☃.getBlockPos().getY() + ☃.getYOffset() + 0.2F;
            double ☃xxx = ☃.getZ() + ☃.getZOffset();
            Entity ☃xxxx = ItemMonsterPlacer.spawnCreature(☃.getWorld(), ItemMonsterPlacer.getNamedIdFrom(☃), ☃x, ☃xx, ☃xxx);
            if (☃xxxx instanceof EntityLivingBase && ☃.hasDisplayName()) {
               ☃xxxx.setCustomNameTag(☃.getDisplayName());
            }

            ItemMonsterPlacer.applyItemEntityDataToEntity(☃.getWorld(), null, ☃, ☃xxxx);
            ☃.shrink(1);
            return ☃;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIREWORKS, new BehaviorDefaultDispenseItem() {
         @Override
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            EnumFacing ☃ = ☃.getBlockState().getValue(BlockDispenser.FACING);
            double ☃x = ☃.getX() + ☃.getXOffset();
            double ☃xx = ☃.getBlockPos().getY() + 0.2F;
            double ☃xxx = ☃.getZ() + ☃.getZOffset();
            EntityFireworkRocket ☃xxxx = new EntityFireworkRocket(☃.getWorld(), ☃x, ☃xx, ☃xxx, ☃);
            ☃.getWorld().spawnEntity(☃xxxx);
            ☃.shrink(1);
            return ☃;
         }

         @Override
         protected void playDispenseSound(IBlockSource var1) {
            ☃.getWorld().playEvent(1004, ☃.getBlockPos(), 0);
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIRE_CHARGE, new BehaviorDefaultDispenseItem() {
         @Override
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            EnumFacing ☃ = ☃.getBlockState().getValue(BlockDispenser.FACING);
            IPosition ☃x = BlockDispenser.getDispensePosition(☃);
            double ☃xx = ☃x.getX() + ☃.getXOffset() * 0.3F;
            double ☃xxx = ☃x.getY() + ☃.getYOffset() * 0.3F;
            double ☃xxxx = ☃x.getZ() + ☃.getZOffset() * 0.3F;
            World ☃xxxxx = ☃.getWorld();
            Random ☃xxxxxx = ☃xxxxx.rand;
            double ☃xxxxxxx = ☃xxxxxx.nextGaussian() * 0.05 + ☃.getXOffset();
            double ☃xxxxxxxx = ☃xxxxxx.nextGaussian() * 0.05 + ☃.getYOffset();
            double ☃xxxxxxxxx = ☃xxxxxx.nextGaussian() * 0.05 + ☃.getZOffset();
            ☃xxxxx.spawnEntity(new EntitySmallFireball(☃xxxxx, ☃xx, ☃xxx, ☃xxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx));
            ☃.shrink(1);
            return ☃;
         }

         @Override
         protected void playDispenseSound(IBlockSource var1) {
            ☃.getWorld().playEvent(1018, ☃.getBlockPos(), 0);
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BOAT, new Bootstrap.BehaviorDispenseBoat(EntityBoat.Type.OAK));
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPRUCE_BOAT, new Bootstrap.BehaviorDispenseBoat(EntityBoat.Type.SPRUCE));
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BIRCH_BOAT, new Bootstrap.BehaviorDispenseBoat(EntityBoat.Type.BIRCH));
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.JUNGLE_BOAT, new Bootstrap.BehaviorDispenseBoat(EntityBoat.Type.JUNGLE));
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DARK_OAK_BOAT, new Bootstrap.BehaviorDispenseBoat(EntityBoat.Type.DARK_OAK));
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ACACIA_BOAT, new Bootstrap.BehaviorDispenseBoat(EntityBoat.Type.ACACIA));
      IBehaviorDispenseItem ☃ = new BehaviorDefaultDispenseItem() {
         private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();

         @Override
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            ItemBucket ☃ = (ItemBucket)☃.getItem();
            BlockPos ☃x = ☃.getBlockPos().offset(☃.getBlockState().getValue(BlockDispenser.FACING));
            return ☃.tryPlaceContainedLiquid(null, ☃.getWorld(), ☃x) ? new ItemStack(Items.BUCKET) : this.dispenseBehavior.dispense(☃, ☃);
         }
      };
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LAVA_BUCKET, ☃);
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, ☃);
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BUCKET, new BehaviorDefaultDispenseItem() {
         private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();

         @Override
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World ☃ = ☃.getWorld();
            BlockPos ☃x = ☃.getBlockPos().offset(☃.getBlockState().getValue(BlockDispenser.FACING));
            IBlockState ☃xx = ☃.getBlockState(☃x);
            Block ☃xxx = ☃xx.getBlock();
            Material ☃xxxx = ☃xx.getMaterial();
            Item ☃xxxxx;
            if (Material.WATER.equals(☃xxxx) && ☃xxx instanceof BlockLiquid && ☃xx.getValue(BlockLiquid.LEVEL) == 0) {
               ☃xxxxx = Items.WATER_BUCKET;
            } else {
               if (!Material.LAVA.equals(☃xxxx) || !(☃xxx instanceof BlockLiquid) || ☃xx.getValue(BlockLiquid.LEVEL) != 0) {
                  return super.dispenseStack(☃, ☃);
               }

               ☃xxxxx = Items.LAVA_BUCKET;
            }

            ☃.setBlockToAir(☃x);
            ☃.shrink(1);
            if (☃.isEmpty()) {
               return new ItemStack(☃xxxxx);
            } else {
               if (☃.<TileEntityDispenser>getBlockTileEntity().addItemStack(new ItemStack(☃xxxxx)) < 0) {
                  this.dispenseBehavior.dispense(☃, new ItemStack(☃xxxxx));
               }

               return ☃;
            }
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FLINT_AND_STEEL, new Bootstrap.BehaviorDispenseOptional() {
         @Override
         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World ☃ = ☃.getWorld();
            this.successful = true;
            BlockPos ☃x = ☃.getBlockPos().offset(☃.getBlockState().getValue(BlockDispenser.FACING));
            if (☃.isAirBlock(☃x)) {
               ☃.setBlockState(☃x, Blocks.FIRE.getDefaultState());
               if (☃.attemptDamageItem(1, ☃.rand, null)) {
                  ☃.setCount(0);
               }
            } else if (☃.getBlockState(☃x).getBlock() == Blocks.TNT) {
               Blocks.TNT.onPlayerDestroy(☃, ☃x, Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, true));
               ☃.setBlockToAir(☃x);
            } else {
               this.successful = false;
            }

            return ☃;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DYE, new Bootstrap.BehaviorDispenseOptional() {
         @Override
         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            this.successful = true;
            if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(☃.getMetadata())) {
               World ☃ = ☃.getWorld();
               BlockPos ☃x = ☃.getBlockPos().offset(☃.getBlockState().getValue(BlockDispenser.FACING));
               if (ItemDye.applyBonemeal(☃, ☃, ☃x)) {
                  if (!☃.isRemote) {
                     ☃.playEvent(2005, ☃x, 0);
                  }
               } else {
                  this.successful = false;
               }

               return ☃;
            } else {
               return super.dispenseStack(☃, ☃);
            }
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.TNT), new BehaviorDefaultDispenseItem() {
         @Override
         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World ☃ = ☃.getWorld();
            BlockPos ☃x = ☃.getBlockPos().offset(☃.getBlockState().getValue(BlockDispenser.FACING));
            EntityTNTPrimed ☃xx = new EntityTNTPrimed(☃, ☃x.getX() + 0.5, ☃x.getY(), ☃x.getZ() + 0.5, null);
            ☃.spawnEntity(☃xx);
            ☃.playSound(null, ☃xx.posX, ☃xx.posY, ☃xx.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            ☃.shrink(1);
            return ☃;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SKULL, new Bootstrap.BehaviorDispenseOptional() {
         @Override
         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World ☃ = ☃.getWorld();
            EnumFacing ☃x = ☃.getBlockState().getValue(BlockDispenser.FACING);
            BlockPos ☃xx = ☃.getBlockPos().offset(☃x);
            BlockSkull ☃xxx = Blocks.SKULL;
            this.successful = true;
            if (☃.isAirBlock(☃xx) && ☃xxx.canDispenserPlace(☃, ☃xx, ☃)) {
               if (!☃.isRemote) {
                  ☃.setBlockState(☃xx, ☃xxx.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP), 3);
                  TileEntity ☃xxxx = ☃.getTileEntity(☃xx);
                  if (☃xxxx instanceof TileEntitySkull) {
                     if (☃.getMetadata() == 3) {
                        GameProfile ☃xxxxx = null;
                        if (☃.hasTagCompound()) {
                           NBTTagCompound ☃xxxxxx = ☃.getTagCompound();
                           if (☃xxxxxx.hasKey("SkullOwner", 10)) {
                              ☃xxxxx = NBTUtil.readGameProfileFromNBT(☃xxxxxx.getCompoundTag("SkullOwner"));
                           } else if (☃xxxxxx.hasKey("SkullOwner", 8)) {
                              String ☃xxxxxxx = ☃xxxxxx.getString("SkullOwner");
                              if (!StringUtils.isNullOrEmpty(☃xxxxxxx)) {
                                 ☃xxxxx = new GameProfile(null, ☃xxxxxxx);
                              }
                           }
                        }

                        ((TileEntitySkull)☃xxxx).setPlayerProfile(☃xxxxx);
                     } else {
                        ((TileEntitySkull)☃xxxx).setType(☃.getMetadata());
                     }

                     ((TileEntitySkull)☃xxxx).setSkullRotation(☃x.getOpposite().getHorizontalIndex() * 4);
                     Blocks.SKULL.checkWitherSpawn(☃, ☃xx, (TileEntitySkull)☃xxxx);
                  }

                  ☃.shrink(1);
               }
            } else if (ItemArmor.dispenseArmor(☃, ☃).isEmpty()) {
               this.successful = false;
            }

            return ☃;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.PUMPKIN), new Bootstrap.BehaviorDispenseOptional() {
         @Override
         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World ☃ = ☃.getWorld();
            BlockPos ☃x = ☃.getBlockPos().offset(☃.getBlockState().getValue(BlockDispenser.FACING));
            BlockPumpkin ☃xx = (BlockPumpkin)Blocks.PUMPKIN;
            this.successful = true;
            if (☃.isAirBlock(☃x) && ☃xx.canDispenserPlace(☃, ☃x)) {
               if (!☃.isRemote) {
                  ☃.setBlockState(☃x, ☃xx.getDefaultState(), 3);
               }

               ☃.shrink(1);
            } else {
               ItemStack ☃xxx = ItemArmor.dispenseArmor(☃, ☃);
               if (☃xxx.isEmpty()) {
                  this.successful = false;
               }
            }

            return ☃;
         }
      });

      for (EnumDyeColor ☃x : EnumDyeColor.values()) {
         BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY
            .putObject(Item.getItemFromBlock(BlockShulkerBox.getBlockByColor(☃x)), new Bootstrap.BehaviorDispenseShulkerBox());
      }
   }

   public static void register() {
      if (!alreadyRegistered) {
         alreadyRegistered = true;
         redirectOutputToLog();
         SoundEvent.registerSounds();
         Block.registerBlocks();
         BlockFire.init();
         Potion.registerPotions();
         Enchantment.registerEnchantments();
         Item.registerItems();
         PotionType.registerPotionTypes();
         PotionHelper.init();
         EntityList.init();
         Biome.registerBiomes();
         registerDispenserBehaviors();
         if (!CraftingManager.init()) {
            hasErrored = true;
            LOGGER.error("Errors with built-in recipes!");
         }

         StatList.init();
         if (LOGGER.isDebugEnabled()) {
            if (new AdvancementManager(null).hasErrored()) {
               hasErrored = true;
               LOGGER.error("Errors with built-in advancements!");
            }

            if (!LootTableList.test()) {
               hasErrored = true;
               LOGGER.error("Errors with built-in loot tables");
            }
         }
      }
   }

   private static void redirectOutputToLog() {
      if (LOGGER.isDebugEnabled()) {
         System.setErr(new DebugLoggingPrintStream("STDERR", System.err));
         System.setOut(new DebugLoggingPrintStream("STDOUT", SYSOUT));
      } else {
         System.setErr(new LoggingPrintStream("STDERR", System.err));
         System.setOut(new LoggingPrintStream("STDOUT", SYSOUT));
      }
   }

   public static void printToSYSOUT(String var0) {
      SYSOUT.println(☃);
   }

   public static class BehaviorDispenseBoat extends BehaviorDefaultDispenseItem {
      private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();
      private final EntityBoat.Type boatType;

      public BehaviorDispenseBoat(EntityBoat.Type var1) {
         this.boatType = ☃;
      }

      @Override
      public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
         EnumFacing ☃ = ☃.getBlockState().getValue(BlockDispenser.FACING);
         World ☃x = ☃.getWorld();
         double ☃xx = ☃.getX() + ☃.getXOffset() * 1.125F;
         double ☃xxx = ☃.getY() + ☃.getYOffset() * 1.125F;
         double ☃xxxx = ☃.getZ() + ☃.getZOffset() * 1.125F;
         BlockPos ☃xxxxx = ☃.getBlockPos().offset(☃);
         Material ☃xxxxxx = ☃x.getBlockState(☃xxxxx).getMaterial();
         double ☃xxxxxxx;
         if (Material.WATER.equals(☃xxxxxx)) {
            ☃xxxxxxx = 1.0;
         } else {
            if (!Material.AIR.equals(☃xxxxxx) || !Material.WATER.equals(☃x.getBlockState(☃xxxxx.down()).getMaterial())) {
               return this.dispenseBehavior.dispense(☃, ☃);
            }

            ☃xxxxxxx = 0.0;
         }

         EntityBoat ☃xxxxxxxx = new EntityBoat(☃x, ☃xx, ☃xxx + ☃xxxxxxx, ☃xxxx);
         ☃xxxxxxxx.setBoatType(this.boatType);
         ☃xxxxxxxx.rotationYaw = ☃.getHorizontalAngle();
         ☃x.spawnEntity(☃xxxxxxxx);
         ☃.shrink(1);
         return ☃;
      }

      @Override
      protected void playDispenseSound(IBlockSource var1) {
         ☃.getWorld().playEvent(1000, ☃.getBlockPos(), 0);
      }
   }

   public abstract static class BehaviorDispenseOptional extends BehaviorDefaultDispenseItem {
      protected boolean successful = true;

      @Override
      protected void playDispenseSound(IBlockSource var1) {
         ☃.getWorld().playEvent(this.successful ? 1000 : 1001, ☃.getBlockPos(), 0);
      }
   }

   static class BehaviorDispenseShulkerBox extends Bootstrap.BehaviorDispenseOptional {
      private BehaviorDispenseShulkerBox() {
      }

      @Override
      protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
         Block ☃ = Block.getBlockFromItem(☃.getItem());
         World ☃x = ☃.getWorld();
         EnumFacing ☃xx = ☃.getBlockState().getValue(BlockDispenser.FACING);
         BlockPos ☃xxx = ☃.getBlockPos().offset(☃xx);
         this.successful = ☃x.mayPlace(☃, ☃xxx, false, EnumFacing.DOWN, null);
         if (this.successful) {
            EnumFacing ☃xxxx = ☃x.isAirBlock(☃xxx.down()) ? ☃xx : EnumFacing.UP;
            IBlockState ☃xxxxx = ☃.getDefaultState().withProperty(BlockShulkerBox.FACING, ☃xxxx);
            ☃x.setBlockState(☃xxx, ☃xxxxx);
            TileEntity ☃xxxxxx = ☃x.getTileEntity(☃xxx);
            ItemStack ☃xxxxxxx = ☃.splitStack(1);
            if (☃xxxxxxx.hasTagCompound()) {
               ((TileEntityShulkerBox)☃xxxxxx).loadFromNbt(☃xxxxxxx.getTagCompound().getCompoundTag("BlockEntityTag"));
            }

            if (☃xxxxxxx.hasDisplayName()) {
               ((TileEntityShulkerBox)☃xxxxxx).setCustomName(☃xxxxxxx.getDisplayName());
            }

            ☃x.updateComparatorOutputLevel(☃xxx, ☃xxxxx.getBlock());
         }

         return ☃;
      }
   }
}
