package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSword extends Item {
   private final float attackDamage;
   private final Item.ToolMaterial material;

   public ItemSword(Item.ToolMaterial var1) {
      this.material = ☃;
      this.maxStackSize = 1;
      this.setMaxDamage(☃.getMaxUses());
      this.setCreativeTab(CreativeTabs.COMBAT);
      this.attackDamage = 3.0F + ☃.getAttackDamage();
   }

   public float getAttackDamage() {
      return this.material.getAttackDamage();
   }

   @Override
   public float getDestroySpeed(ItemStack var1, IBlockState var2) {
      Block ☃ = ☃.getBlock();
      if (☃ == Blocks.WEB) {
         return 15.0F;
      } else {
         Material ☃x = ☃.getMaterial();
         return ☃x != Material.PLANTS && ☃x != Material.VINE && ☃x != Material.CORAL && ☃x != Material.LEAVES && ☃x != Material.GOURD ? 1.0F : 1.5F;
      }
   }

   @Override
   public boolean hitEntity(ItemStack var1, EntityLivingBase var2, EntityLivingBase var3) {
      ☃.damageItem(1, ☃);
      return true;
   }

   @Override
   public boolean onBlockDestroyed(ItemStack var1, World var2, IBlockState var3, BlockPos var4, EntityLivingBase var5) {
      if (☃.getBlockHardness(☃, ☃) != 0.0) {
         ☃.damageItem(2, ☃);
      }

      return true;
   }

   @Override
   public boolean isFull3D() {
      return true;
   }

   @Override
   public boolean canHarvestBlock(IBlockState var1) {
      return ☃.getBlock() == Blocks.WEB;
   }

   @Override
   public int getItemEnchantability() {
      return this.material.getEnchantability();
   }

   public String getToolMaterialName() {
      return this.material.toString();
   }

   @Override
   public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
      return this.material.getRepairItem() == ☃.getItem() ? true : super.getIsRepairable(☃, ☃);
   }

   @Override
   public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot var1) {
      Multimap<String, AttributeModifier> ☃ = super.getItemAttributeModifiers(☃);
      if (☃ == EntityEquipmentSlot.MAINHAND) {
         ☃.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, 0));
         ☃.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4F, 0));
      }

      return ☃;
   }
}
