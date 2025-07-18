package net.minecraft.item;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemMonsterPlacer extends Item {
   public ItemMonsterPlacer() {
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      String ☃ = ("" + I18n.translateToLocal(this.getTranslationKey() + ".name")).trim();
      String ☃x = EntityList.getTranslationName(getNamedIdFrom(☃));
      if (☃x != null) {
         ☃ = ☃ + " " + I18n.translateToLocal("entity." + ☃x + ".name");
      }

      return ☃;
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.isRemote) {
         return EnumActionResult.SUCCESS;
      } else if (!☃.canPlayerEdit(☃.offset(☃), ☃, ☃)) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState ☃x = ☃.getBlockState(☃);
         Block ☃xx = ☃x.getBlock();
         if (☃xx == Blocks.MOB_SPAWNER) {
            TileEntity ☃xxx = ☃.getTileEntity(☃);
            if (☃xxx instanceof TileEntityMobSpawner) {
               MobSpawnerBaseLogic ☃xxxx = ((TileEntityMobSpawner)☃xxx).getSpawnerBaseLogic();
               ☃xxxx.setEntityId(getNamedIdFrom(☃));
               ☃xxx.markDirty();
               ☃.notifyBlockUpdate(☃, ☃x, ☃x, 3);
               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
               }

               return EnumActionResult.SUCCESS;
            }
         }

         BlockPos ☃xxx = ☃.offset(☃);
         double ☃xxxx = this.getYOffset(☃, ☃xxx);
         Entity ☃xxxxx = spawnCreature(☃, getNamedIdFrom(☃), ☃xxx.getX() + 0.5, ☃xxx.getY() + ☃xxxx, ☃xxx.getZ() + 0.5);
         if (☃xxxxx != null) {
            if (☃xxxxx instanceof EntityLivingBase && ☃.hasDisplayName()) {
               ☃xxxxx.setCustomNameTag(☃.getDisplayName());
            }

            applyItemEntityDataToEntity(☃, ☃, ☃, ☃xxxxx);
            if (!☃.capabilities.isCreativeMode) {
               ☃.shrink(1);
            }
         }

         return EnumActionResult.SUCCESS;
      }
   }

   protected double getYOffset(World var1, BlockPos var2) {
      AxisAlignedBB ☃ = new AxisAlignedBB(☃).expand(0.0, -1.0, 0.0);
      List<AxisAlignedBB> ☃x = ☃.getCollisionBoxes(null, ☃);
      if (☃x.isEmpty()) {
         return 0.0;
      } else {
         double ☃xx = ☃.minY;

         for (AxisAlignedBB ☃xxx : ☃x) {
            ☃xx = Math.max(☃xxx.maxY, ☃xx);
         }

         return ☃xx - ☃.getY();
      }
   }

   public static void applyItemEntityDataToEntity(World var0, @Nullable EntityPlayer var1, ItemStack var2, @Nullable Entity var3) {
      MinecraftServer ☃ = ☃.getMinecraftServer();
      if (☃ != null && ☃ != null) {
         NBTTagCompound ☃x = ☃.getTagCompound();
         if (☃x != null && ☃x.hasKey("EntityTag", 10)) {
            if (!☃.isRemote && ☃.ignoreItemEntityData() && (☃ == null || !☃.getPlayerList().canSendCommands(☃.getGameProfile()))) {
               return;
            }

            NBTTagCompound ☃xx = ☃.writeToNBT(new NBTTagCompound());
            UUID ☃xxx = ☃.getUniqueID();
            ☃xx.merge(☃x.getCompoundTag("EntityTag"));
            ☃.setUniqueId(☃xxx);
            ☃.readFromNBT(☃xx);
         }
      }
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.isRemote) {
         return new ActionResult<>(EnumActionResult.PASS, ☃);
      } else {
         RayTraceResult ☃x = this.rayTrace(☃, ☃, true);
         if (☃x != null && ☃x.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos ☃xx = ☃x.getBlockPos();
            if (!(☃.getBlockState(☃xx).getBlock() instanceof BlockLiquid)) {
               return new ActionResult<>(EnumActionResult.PASS, ☃);
            } else if (☃.isBlockModifiable(☃, ☃xx) && ☃.canPlayerEdit(☃xx, ☃x.sideHit, ☃)) {
               Entity ☃xxx = spawnCreature(☃, getNamedIdFrom(☃), ☃xx.getX() + 0.5, ☃xx.getY() + 0.5, ☃xx.getZ() + 0.5);
               if (☃xxx == null) {
                  return new ActionResult<>(EnumActionResult.PASS, ☃);
               } else {
                  if (☃xxx instanceof EntityLivingBase && ☃.hasDisplayName()) {
                     ☃xxx.setCustomNameTag(☃.getDisplayName());
                  }

                  applyItemEntityDataToEntity(☃, ☃, ☃, ☃xxx);
                  if (!☃.capabilities.isCreativeMode) {
                     ☃.shrink(1);
                  }

                  ☃.addStat(StatList.getObjectUseStats(this));
                  return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
               }
            } else {
               return new ActionResult<>(EnumActionResult.FAIL, ☃);
            }
         } else {
            return new ActionResult<>(EnumActionResult.PASS, ☃);
         }
      }
   }

   @Nullable
   public static Entity spawnCreature(World var0, @Nullable ResourceLocation var1, double var2, double var4, double var6) {
      if (☃ != null && EntityList.ENTITY_EGGS.containsKey(☃)) {
         Entity ☃ = null;

         for (int ☃x = 0; ☃x < 1; ☃x++) {
            ☃ = EntityList.createEntityByIDFromName(☃, ☃);
            if (☃ instanceof EntityLiving) {
               EntityLiving ☃xx = (EntityLiving)☃;
               ☃.setLocationAndAngles(☃, ☃, ☃, MathHelper.wrapDegrees(☃.rand.nextFloat() * 360.0F), 0.0F);
               ☃xx.rotationYawHead = ☃xx.rotationYaw;
               ☃xx.renderYawOffset = ☃xx.rotationYaw;
               ☃xx.onInitialSpawn(☃.getDifficultyForLocation(new BlockPos(☃xx)), null);
               ☃.spawnEntity(☃);
               ☃xx.playLivingSound();
            }
         }

         return ☃;
      } else {
         return null;
      }
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (EntityList.EntityEggInfo ☃ : EntityList.ENTITY_EGGS.values()) {
            ItemStack ☃x = new ItemStack(this, 1);
            applyEntityIdToItemStack(☃x, ☃.spawnedID);
            ☃.add(☃x);
         }
      }
   }

   public static void applyEntityIdToItemStack(ItemStack var0, ResourceLocation var1) {
      NBTTagCompound ☃ = ☃.hasTagCompound() ? ☃.getTagCompound() : new NBTTagCompound();
      NBTTagCompound ☃x = new NBTTagCompound();
      ☃x.setString("id", ☃.toString());
      ☃.setTag("EntityTag", ☃x);
      ☃.setTagCompound(☃);
   }

   @Nullable
   public static ResourceLocation getNamedIdFrom(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getTagCompound();
      if (☃ == null) {
         return null;
      } else if (!☃.hasKey("EntityTag", 10)) {
         return null;
      } else {
         NBTTagCompound ☃x = ☃.getCompoundTag("EntityTag");
         if (!☃x.hasKey("id", 8)) {
            return null;
         } else {
            String ☃xx = ☃x.getString("id");
            ResourceLocation ☃xxx = new ResourceLocation(☃xx);
            if (!☃xx.contains(":")) {
               ☃x.setString("id", ☃xxx.toString());
            }

            return ☃xxx;
         }
      }
   }
}
