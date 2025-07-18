package net.minecraft.item;

import com.google.common.collect.Multimap;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTool extends Item {
   private final Set<Block> effectiveBlocks;
   protected float efficiency = 4.0F;
   protected float attackDamage;
   protected float attackSpeed;
   protected Item.ToolMaterial toolMaterial;

   protected ItemTool(float var1, float var2, Item.ToolMaterial var3, Set<Block> var4) {
      this.toolMaterial = ☃;
      this.effectiveBlocks = ☃;
      this.maxStackSize = 1;
      this.setMaxDamage(☃.getMaxUses());
      this.efficiency = ☃.getEfficiency();
      this.attackDamage = ☃ + ☃.getAttackDamage();
      this.attackSpeed = ☃;
      this.setCreativeTab(CreativeTabs.TOOLS);
   }

   protected ItemTool(Item.ToolMaterial var1, Set<Block> var2) {
      this(0.0F, 0.0F, ☃, ☃);
   }

   @Override
   public float getDestroySpeed(ItemStack var1, IBlockState var2) {
      return this.effectiveBlocks.contains(☃.getBlock()) ? this.efficiency : 1.0F;
   }

   @Override
   public boolean hitEntity(ItemStack var1, EntityLivingBase var2, EntityLivingBase var3) {
      ☃.damageItem(2, ☃);
      return true;
   }

   @Override
   public boolean onBlockDestroyed(ItemStack var1, World var2, IBlockState var3, BlockPos var4, EntityLivingBase var5) {
      if (!☃.isRemote && ☃.getBlockHardness(☃, ☃) != 0.0) {
         ☃.damageItem(1, ☃);
      }

      return true;
   }

   @Override
   public boolean isFull3D() {
      return true;
   }

   @Override
   public int getItemEnchantability() {
      return this.toolMaterial.getEnchantability();
   }

   public String getToolMaterialName() {
      return this.toolMaterial.toString();
   }

   @Override
   public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
      return this.toolMaterial.getRepairItem() == ☃.getItem() ? true : super.getIsRepairable(☃, ☃);
   }

   @Override
   public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot var1) {
      Multimap<String, AttributeModifier> ☃ = super.getItemAttributeModifiers(☃);
      if (☃ == EntityEquipmentSlot.MAINHAND) {
         ☃.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.attackDamage, 0));
         ☃.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0));
      }

      return ☃;
   }
}
