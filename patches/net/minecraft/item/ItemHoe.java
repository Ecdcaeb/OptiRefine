package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoe extends Item {
   private final float speed;
   protected Item.ToolMaterial toolMaterial;

   public ItemHoe(Item.ToolMaterial var1) {
      this.toolMaterial = ☃;
      this.maxStackSize = 1;
      this.setMaxDamage(☃.getMaxUses());
      this.setCreativeTab(CreativeTabs.TOOLS);
      this.speed = ☃.getAttackDamage() + 1.0F;
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.canPlayerEdit(☃.offset(☃), ☃, ☃)) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState ☃x = ☃.getBlockState(☃);
         Block ☃xx = ☃x.getBlock();
         if (☃ != EnumFacing.DOWN && ☃.getBlockState(☃.up()).getMaterial() == Material.AIR) {
            if (☃xx == Blocks.GRASS || ☃xx == Blocks.GRASS_PATH) {
               this.setBlock(☃, ☃, ☃, ☃, Blocks.FARMLAND.getDefaultState());
               return EnumActionResult.SUCCESS;
            }

            if (☃xx == Blocks.DIRT) {
               switch ((BlockDirt.DirtType)☃x.getValue(BlockDirt.VARIANT)) {
                  case DIRT:
                     this.setBlock(☃, ☃, ☃, ☃, Blocks.FARMLAND.getDefaultState());
                     return EnumActionResult.SUCCESS;
                  case COARSE_DIRT:
                     this.setBlock(☃, ☃, ☃, ☃, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                     return EnumActionResult.SUCCESS;
               }
            }
         }

         return EnumActionResult.PASS;
      }
   }

   @Override
   public boolean hitEntity(ItemStack var1, EntityLivingBase var2, EntityLivingBase var3) {
      ☃.damageItem(1, ☃);
      return true;
   }

   protected void setBlock(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, IBlockState var5) {
      ☃.playSound(☃, ☃, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
      if (!☃.isRemote) {
         ☃.setBlockState(☃, ☃, 11);
         ☃.damageItem(1, ☃);
      }
   }

   @Override
   public boolean isFull3D() {
      return true;
   }

   public String getMaterialName() {
      return this.toolMaterial.toString();
   }

   @Override
   public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot var1) {
      Multimap<String, AttributeModifier> ☃ = super.getItemAttributeModifiers(☃);
      if (☃ == EntityEquipmentSlot.MAINHAND) {
         ☃.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0, 0));
         ☃.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.speed - 4.0F, 0));
      }

      return ☃;
   }
}
