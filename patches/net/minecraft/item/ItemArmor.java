package net.minecraft.item;

import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemArmor extends Item {
   private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
   private static final UUID[] ARMOR_MODIFIERS = new UUID[]{
      UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
      UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
      UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
      UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
   };
   public static final String[] EMPTY_SLOT_NAMES = new String[]{
      "minecraft:items/empty_armor_slot_boots",
      "minecraft:items/empty_armor_slot_leggings",
      "minecraft:items/empty_armor_slot_chestplate",
      "minecraft:items/empty_armor_slot_helmet"
   };
   public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
      @Override
      protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
         ItemStack ☃ = ItemArmor.dispenseArmor(☃, ☃);
         return ☃.isEmpty() ? super.dispenseStack(☃, ☃) : ☃;
      }
   };
   public final EntityEquipmentSlot armorType;
   public final int damageReduceAmount;
   public final float toughness;
   public final int renderIndex;
   private final ItemArmor.ArmorMaterial material;

   public static ItemStack dispenseArmor(IBlockSource var0, ItemStack var1) {
      BlockPos ☃ = ☃.getBlockPos().offset(☃.getBlockState().getValue(BlockDispenser.FACING));
      List<EntityLivingBase> ☃x = ☃.getWorld()
         .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(☃), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(☃)));
      if (☃x.isEmpty()) {
         return ItemStack.EMPTY;
      } else {
         EntityLivingBase ☃xx = ☃x.get(0);
         EntityEquipmentSlot ☃xxx = EntityLiving.getSlotForItemStack(☃);
         ItemStack ☃xxxx = ☃.splitStack(1);
         ☃xx.setItemStackToSlot(☃xxx, ☃xxxx);
         if (☃xx instanceof EntityLiving) {
            ((EntityLiving)☃xx).setDropChance(☃xxx, 2.0F);
         }

         return ☃;
      }
   }

   public ItemArmor(ItemArmor.ArmorMaterial var1, int var2, EntityEquipmentSlot var3) {
      this.material = ☃;
      this.armorType = ☃;
      this.renderIndex = ☃;
      this.damageReduceAmount = ☃.getDamageReductionAmount(☃);
      this.setMaxDamage(☃.getDurability(☃));
      this.toughness = ☃.getToughness();
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.COMBAT);
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
   }

   public EntityEquipmentSlot getEquipmentSlot() {
      return this.armorType;
   }

   @Override
   public int getItemEnchantability() {
      return this.material.getEnchantability();
   }

   public ItemArmor.ArmorMaterial getArmorMaterial() {
      return this.material;
   }

   public boolean hasColor(ItemStack var1) {
      if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
         return false;
      } else {
         NBTTagCompound ☃ = ☃.getTagCompound();
         return ☃ != null && ☃.hasKey("display", 10) ? ☃.getCompoundTag("display").hasKey("color", 3) : false;
      }
   }

   public int getColor(ItemStack var1) {
      if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
         return 16777215;
      } else {
         NBTTagCompound ☃ = ☃.getTagCompound();
         if (☃ != null) {
            NBTTagCompound ☃x = ☃.getCompoundTag("display");
            if (☃x != null && ☃x.hasKey("color", 3)) {
               return ☃x.getInteger("color");
            }
         }

         return 10511680;
      }
   }

   public void removeColor(ItemStack var1) {
      if (this.material == ItemArmor.ArmorMaterial.LEATHER) {
         NBTTagCompound ☃ = ☃.getTagCompound();
         if (☃ != null) {
            NBTTagCompound ☃x = ☃.getCompoundTag("display");
            if (☃x.hasKey("color")) {
               ☃x.removeTag("color");
            }
         }
      }
   }

   public void setColor(ItemStack var1, int var2) {
      if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
         throw new UnsupportedOperationException("Can't dye non-leather!");
      } else {
         NBTTagCompound ☃ = ☃.getTagCompound();
         if (☃ == null) {
            ☃ = new NBTTagCompound();
            ☃.setTagCompound(☃);
         }

         NBTTagCompound ☃x = ☃.getCompoundTag("display");
         if (!☃.hasKey("display", 10)) {
            ☃.setTag("display", ☃x);
         }

         ☃x.setInteger("color", ☃);
      }
   }

   @Override
   public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
      return this.material.getRepairItem() == ☃.getItem() ? true : super.getIsRepairable(☃, ☃);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      EntityEquipmentSlot ☃x = EntityLiving.getSlotForItemStack(☃);
      ItemStack ☃xx = ☃.getItemStackFromSlot(☃x);
      if (☃xx.isEmpty()) {
         ☃.setItemStackToSlot(☃x, ☃.copy());
         ☃.setCount(0);
         return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
      } else {
         return new ActionResult<>(EnumActionResult.FAIL, ☃);
      }
   }

   @Override
   public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot var1) {
      Multimap<String, AttributeModifier> ☃ = super.getItemAttributeModifiers(☃);
      if (☃ == this.armorType) {
         ☃.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[☃.getIndex()], "Armor modifier", this.damageReduceAmount, 0));
         ☃.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[☃.getIndex()], "Armor toughness", this.toughness, 0));
      }

      return ☃;
   }

   public static enum ArmorMaterial {
      LEATHER("leather", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F),
      CHAIN("chainmail", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0F),
      IRON("iron", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F),
      GOLD("gold", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F),
      DIAMOND("diamond", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);

      private final String name;
      private final int maxDamageFactor;
      private final int[] damageReductionAmountArray;
      private final int enchantability;
      private final SoundEvent soundEvent;
      private final float toughness;

      private ArmorMaterial(String var3, int var4, int[] var5, int var6, SoundEvent var7, float var8) {
         this.name = ☃;
         this.maxDamageFactor = ☃;
         this.damageReductionAmountArray = ☃;
         this.enchantability = ☃;
         this.soundEvent = ☃;
         this.toughness = ☃;
      }

      public int getDurability(EntityEquipmentSlot var1) {
         return ItemArmor.MAX_DAMAGE_ARRAY[☃.getIndex()] * this.maxDamageFactor;
      }

      public int getDamageReductionAmount(EntityEquipmentSlot var1) {
         return this.damageReductionAmountArray[☃.getIndex()];
      }

      public int getEnchantability() {
         return this.enchantability;
      }

      public SoundEvent getSoundEvent() {
         return this.soundEvent;
      }

      public Item getRepairItem() {
         if (this == LEATHER) {
            return Items.LEATHER;
         } else if (this == CHAIN) {
            return Items.IRON_INGOT;
         } else if (this == GOLD) {
            return Items.GOLD_INGOT;
         } else if (this == IRON) {
            return Items.IRON_INGOT;
         } else {
            return this == DIAMOND ? Items.DIAMOND : null;
         }
      }

      public String getName() {
         return this.name;
      }

      public float getToughness() {
         return this.toughness;
      }
   }
}
