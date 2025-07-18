package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.BlockEntityTag;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public final class ItemStack {
   public static final ItemStack EMPTY = new ItemStack((Item)null);
   public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
   private int stackSize;
   private int animationsToGo;
   private final Item item;
   private NBTTagCompound stackTagCompound;
   private boolean isEmpty;
   private int itemDamage;
   private EntityItemFrame itemFrame;
   private Block canDestroyCacheBlock;
   private boolean canDestroyCacheResult;
   private Block canPlaceOnCacheBlock;
   private boolean canPlaceOnCacheResult;

   public ItemStack(Block var1) {
      this(☃, 1);
   }

   public ItemStack(Block var1, int var2) {
      this(☃, ☃, 0);
   }

   public ItemStack(Block var1, int var2, int var3) {
      this(Item.getItemFromBlock(☃), ☃, ☃);
   }

   public ItemStack(Item var1) {
      this(☃, 1);
   }

   public ItemStack(Item var1, int var2) {
      this(☃, ☃, 0);
   }

   public ItemStack(Item var1, int var2, int var3) {
      this.item = ☃;
      this.itemDamage = ☃;
      this.stackSize = ☃;
      if (this.itemDamage < 0) {
         this.itemDamage = 0;
      }

      this.updateEmptyState();
   }

   private void updateEmptyState() {
      this.isEmpty = this.isEmpty();
   }

   public ItemStack(NBTTagCompound var1) {
      this.item = Item.getByNameOrId(☃.getString("id"));
      this.stackSize = ☃.getByte("Count");
      this.itemDamage = Math.max(0, ☃.getShort("Damage"));
      if (☃.hasKey("tag", 10)) {
         this.stackTagCompound = ☃.getCompoundTag("tag");
         if (this.item != null) {
            this.item.updateItemStackNBT(☃);
         }
      }

      this.updateEmptyState();
   }

   public boolean isEmpty() {
      if (this == EMPTY) {
         return true;
      } else if (this.item == null || this.item == Item.getItemFromBlock(Blocks.AIR)) {
         return true;
      } else {
         return this.stackSize <= 0 ? true : this.itemDamage < -32768 || this.itemDamage > 65535;
      }
   }

   public static void registerFixes(DataFixer var0) {
      ☃.registerWalker(FixTypes.ITEM_INSTANCE, new BlockEntityTag());
      ☃.registerWalker(FixTypes.ITEM_INSTANCE, new EntityTag());
   }

   public ItemStack splitStack(int var1) {
      int ☃ = Math.min(☃, this.stackSize);
      ItemStack ☃x = this.copy();
      ☃x.setCount(☃);
      this.shrink(☃);
      return ☃x;
   }

   public Item getItem() {
      return this.isEmpty ? Item.getItemFromBlock(Blocks.AIR) : this.item;
   }

   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      EnumActionResult ☃ = this.getItem().onItemUse(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (☃ == EnumActionResult.SUCCESS) {
         ☃.addStat(StatList.getObjectUseStats(this.item));
      }

      return ☃;
   }

   public float getDestroySpeed(IBlockState var1) {
      return this.getItem().getDestroySpeed(this, ☃);
   }

   public ActionResult<ItemStack> useItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      return this.getItem().onItemRightClick(☃, ☃, ☃);
   }

   public ItemStack onItemUseFinish(World var1, EntityLivingBase var2) {
      return this.getItem().onItemUseFinish(this, ☃, ☃);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      ResourceLocation ☃ = Item.REGISTRY.getNameForObject(this.item);
      ☃.setString("id", ☃ == null ? "minecraft:air" : ☃.toString());
      ☃.setByte("Count", (byte)this.stackSize);
      ☃.setShort("Damage", (short)this.itemDamage);
      if (this.stackTagCompound != null) {
         ☃.setTag("tag", this.stackTagCompound);
      }

      return ☃;
   }

   public int getMaxStackSize() {
      return this.getItem().getItemStackLimit();
   }

   public boolean isStackable() {
      return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
   }

   public boolean isItemStackDamageable() {
      if (this.isEmpty) {
         return false;
      } else {
         return this.item.getMaxDamage() <= 0 ? false : !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable");
      }
   }

   public boolean getHasSubtypes() {
      return this.getItem().getHasSubtypes();
   }

   public boolean isItemDamaged() {
      return this.isItemStackDamageable() && this.itemDamage > 0;
   }

   public int getItemDamage() {
      return this.itemDamage;
   }

   public int getMetadata() {
      return this.itemDamage;
   }

   public void setItemDamage(int var1) {
      this.itemDamage = ☃;
      if (this.itemDamage < 0) {
         this.itemDamage = 0;
      }
   }

   public int getMaxDamage() {
      return this.getItem().getMaxDamage();
   }

   public boolean attemptDamageItem(int var1, Random var2, @Nullable EntityPlayerMP var3) {
      if (!this.isItemStackDamageable()) {
         return false;
      } else {
         if (☃ > 0) {
            int ☃ = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this);
            int ☃x = 0;

            for (int ☃xx = 0; ☃ > 0 && ☃xx < ☃; ☃xx++) {
               if (EnchantmentDurability.negateDamage(this, ☃, ☃)) {
                  ☃x++;
               }
            }

            ☃ -= ☃x;
            if (☃ <= 0) {
               return false;
            }
         }

         if (☃ != null && ☃ != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(☃, this, this.itemDamage + ☃);
         }

         this.itemDamage += ☃;
         return this.itemDamage > this.getMaxDamage();
      }
   }

   public void damageItem(int var1, EntityLivingBase var2) {
      if (!(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).capabilities.isCreativeMode) {
         if (this.isItemStackDamageable()) {
            if (this.attemptDamageItem(☃, ☃.getRNG(), ☃ instanceof EntityPlayerMP ? (EntityPlayerMP)☃ : null)) {
               ☃.renderBrokenItemStack(this);
               this.shrink(1);
               if (☃ instanceof EntityPlayer) {
                  EntityPlayer ☃ = (EntityPlayer)☃;
                  ☃.addStat(StatList.getObjectBreakStats(this.item));
               }

               this.itemDamage = 0;
            }
         }
      }
   }

   public void hitEntity(EntityLivingBase var1, EntityPlayer var2) {
      boolean ☃ = this.item.hitEntity(this, ☃, ☃);
      if (☃) {
         ☃.addStat(StatList.getObjectUseStats(this.item));
      }
   }

   public void onBlockDestroyed(World var1, IBlockState var2, BlockPos var3, EntityPlayer var4) {
      boolean ☃ = this.getItem().onBlockDestroyed(this, ☃, ☃, ☃, ☃);
      if (☃) {
         ☃.addStat(StatList.getObjectUseStats(this.item));
      }
   }

   public boolean canHarvestBlock(IBlockState var1) {
      return this.getItem().canHarvestBlock(☃);
   }

   public boolean interactWithEntity(EntityPlayer var1, EntityLivingBase var2, EnumHand var3) {
      return this.getItem().itemInteractionForEntity(this, ☃, ☃, ☃);
   }

   public ItemStack copy() {
      ItemStack ☃ = new ItemStack(this.item, this.stackSize, this.itemDamage);
      ☃.setAnimationsToGo(this.getAnimationsToGo());
      if (this.stackTagCompound != null) {
         ☃.stackTagCompound = this.stackTagCompound.copy();
      }

      return ☃;
   }

   public static boolean areItemStackTagsEqual(ItemStack var0, ItemStack var1) {
      if (☃.isEmpty() && ☃.isEmpty()) {
         return true;
      } else if (☃.isEmpty() || ☃.isEmpty()) {
         return false;
      } else {
         return ☃.stackTagCompound == null && ☃.stackTagCompound != null ? false : ☃.stackTagCompound == null || ☃.stackTagCompound.equals(☃.stackTagCompound);
      }
   }

   public static boolean areItemStacksEqual(ItemStack var0, ItemStack var1) {
      if (☃.isEmpty() && ☃.isEmpty()) {
         return true;
      } else {
         return !☃.isEmpty() && !☃.isEmpty() ? ☃.isItemStackEqual(☃) : false;
      }
   }

   private boolean isItemStackEqual(ItemStack var1) {
      if (this.stackSize != ☃.stackSize) {
         return false;
      } else if (this.getItem() != ☃.getItem()) {
         return false;
      } else if (this.itemDamage != ☃.itemDamage) {
         return false;
      } else {
         return this.stackTagCompound == null && ☃.stackTagCompound != null
            ? false
            : this.stackTagCompound == null || this.stackTagCompound.equals(☃.stackTagCompound);
      }
   }

   public static boolean areItemsEqual(ItemStack var0, ItemStack var1) {
      if (☃ == ☃) {
         return true;
      } else {
         return !☃.isEmpty() && !☃.isEmpty() ? ☃.isItemEqual(☃) : false;
      }
   }

   public static boolean areItemsEqualIgnoreDurability(ItemStack var0, ItemStack var1) {
      if (☃ == ☃) {
         return true;
      } else {
         return !☃.isEmpty() && !☃.isEmpty() ? ☃.isItemEqualIgnoreDurability(☃) : false;
      }
   }

   public boolean isItemEqual(ItemStack var1) {
      return !☃.isEmpty() && this.item == ☃.item && this.itemDamage == ☃.itemDamage;
   }

   public boolean isItemEqualIgnoreDurability(ItemStack var1) {
      return !this.isItemStackDamageable() ? this.isItemEqual(☃) : !☃.isEmpty() && this.item == ☃.item;
   }

   public String getTranslationKey() {
      return this.getItem().getTranslationKey(this);
   }

   @Override
   public String toString() {
      return this.stackSize + "x" + this.getItem().getTranslationKey() + "@" + this.itemDamage;
   }

   public void updateAnimation(World var1, Entity var2, int var3, boolean var4) {
      if (this.animationsToGo > 0) {
         this.animationsToGo--;
      }

      if (this.item != null) {
         this.item.onUpdate(this, ☃, ☃, ☃, ☃);
      }
   }

   public void onCrafting(World var1, EntityPlayer var2, int var3) {
      ☃.addStat(StatList.getCraftStats(this.item), ☃);
      this.getItem().onCreated(this, ☃, ☃);
   }

   public int getMaxItemUseDuration() {
      return this.getItem().getMaxItemUseDuration(this);
   }

   public EnumAction getItemUseAction() {
      return this.getItem().getItemUseAction(this);
   }

   public void onPlayerStoppedUsing(World var1, EntityLivingBase var2, int var3) {
      this.getItem().onPlayerStoppedUsing(this, ☃, ☃, ☃);
   }

   public boolean hasTagCompound() {
      return !this.isEmpty && this.stackTagCompound != null;
   }

   @Nullable
   public NBTTagCompound getTagCompound() {
      return this.stackTagCompound;
   }

   public NBTTagCompound getOrCreateSubCompound(String var1) {
      if (this.stackTagCompound != null && this.stackTagCompound.hasKey(☃, 10)) {
         return this.stackTagCompound.getCompoundTag(☃);
      } else {
         NBTTagCompound ☃ = new NBTTagCompound();
         this.setTagInfo(☃, ☃);
         return ☃;
      }
   }

   @Nullable
   public NBTTagCompound getSubCompound(String var1) {
      return this.stackTagCompound != null && this.stackTagCompound.hasKey(☃, 10) ? this.stackTagCompound.getCompoundTag(☃) : null;
   }

   public void removeSubCompound(String var1) {
      if (this.stackTagCompound != null && this.stackTagCompound.hasKey(☃, 10)) {
         this.stackTagCompound.removeTag(☃);
      }
   }

   public NBTTagList getEnchantmentTagList() {
      return this.stackTagCompound != null ? this.stackTagCompound.getTagList("ench", 10) : new NBTTagList();
   }

   public void setTagCompound(@Nullable NBTTagCompound var1) {
      this.stackTagCompound = ☃;
   }

   public String getDisplayName() {
      NBTTagCompound ☃ = this.getSubCompound("display");
      if (☃ != null) {
         if (☃.hasKey("Name", 8)) {
            return ☃.getString("Name");
         }

         if (☃.hasKey("LocName", 8)) {
            return I18n.translateToLocal(☃.getString("LocName"));
         }
      }

      return this.getItem().getItemStackDisplayName(this);
   }

   public ItemStack setTranslatableName(String var1) {
      this.getOrCreateSubCompound("display").setString("LocName", ☃);
      return this;
   }

   public ItemStack setStackDisplayName(String var1) {
      this.getOrCreateSubCompound("display").setString("Name", ☃);
      return this;
   }

   public void clearCustomName() {
      NBTTagCompound ☃ = this.getSubCompound("display");
      if (☃ != null) {
         ☃.removeTag("Name");
         if (☃.isEmpty()) {
            this.removeSubCompound("display");
         }
      }

      if (this.stackTagCompound != null && this.stackTagCompound.isEmpty()) {
         this.stackTagCompound = null;
      }
   }

   public boolean hasDisplayName() {
      NBTTagCompound ☃ = this.getSubCompound("display");
      return ☃ != null && ☃.hasKey("Name", 8);
   }

   public List<String> getTooltip(@Nullable EntityPlayer var1, ITooltipFlag var2) {
      List<String> ☃ = Lists.newArrayList();
      String ☃x = this.getDisplayName();
      if (this.hasDisplayName()) {
         ☃x = TextFormatting.ITALIC + ☃x;
      }

      ☃x = ☃x + TextFormatting.RESET;
      if (☃.isAdvanced()) {
         String ☃xx = "";
         if (!☃x.isEmpty()) {
            ☃x = ☃x + " (";
            ☃xx = ")";
         }

         int ☃xxx = Item.getIdFromItem(this.item);
         if (this.getHasSubtypes()) {
            ☃x = ☃x + String.format("#%04d/%d%s", ☃xxx, this.itemDamage, ☃xx);
         } else {
            ☃x = ☃x + String.format("#%04d%s", ☃xxx, ☃xx);
         }
      } else if (!this.hasDisplayName() && this.item == Items.FILLED_MAP) {
         ☃x = ☃x + " #" + this.itemDamage;
      }

      ☃.add(☃x);
      int ☃xxx = 0;
      if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
         ☃xxx = this.stackTagCompound.getInteger("HideFlags");
      }

      if ((☃xxx & 32) == 0) {
         this.getItem().addInformation(this, ☃ == null ? null : ☃.world, ☃, ☃);
      }

      if (this.hasTagCompound()) {
         if ((☃xxx & 1) == 0) {
            NBTTagList ☃xxxx = this.getEnchantmentTagList();

            for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.tagCount(); ☃xxxxx++) {
               NBTTagCompound ☃xxxxxx = ☃xxxx.getCompoundTagAt(☃xxxxx);
               int ☃xxxxxxx = ☃xxxxxx.getShort("id");
               int ☃xxxxxxxx = ☃xxxxxx.getShort("lvl");
               Enchantment ☃xxxxxxxxx = Enchantment.getEnchantmentByID(☃xxxxxxx);
               if (☃xxxxxxxxx != null) {
                  ☃.add(☃xxxxxxxxx.getTranslatedName(☃xxxxxxxx));
               }
            }
         }

         if (this.stackTagCompound.hasKey("display", 10)) {
            NBTTagCompound ☃xxxx = this.stackTagCompound.getCompoundTag("display");
            if (☃xxxx.hasKey("color", 3)) {
               if (☃.isAdvanced()) {
                  ☃.add(I18n.translateToLocalFormatted("item.color", String.format("#%06X", ☃xxxx.getInteger("color"))));
               } else {
                  ☃.add(TextFormatting.ITALIC + I18n.translateToLocal("item.dyed"));
               }
            }

            if (☃xxxx.getTagId("Lore") == 9) {
               NBTTagList ☃xxxxxx = ☃xxxx.getTagList("Lore", 8);
               if (!☃xxxxxx.isEmpty()) {
                  for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxxxxx.tagCount(); ☃xxxxxxx++) {
                     ☃.add(TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + ☃xxxxxx.getStringTagAt(☃xxxxxxx));
                  }
               }
            }
         }
      }

      for (EntityEquipmentSlot ☃xxxxxx : EntityEquipmentSlot.values()) {
         Multimap<String, AttributeModifier> ☃xxxxxxx = this.getAttributeModifiers(☃xxxxxx);
         if (!☃xxxxxxx.isEmpty() && (☃xxx & 2) == 0) {
            ☃.add("");
            ☃.add(I18n.translateToLocal("item.modifiers." + ☃xxxxxx.getName()));

            for (Entry<String, AttributeModifier> ☃xxxxxxxx : ☃xxxxxxx.entries()) {
               AttributeModifier ☃xxxxxxxxx = ☃xxxxxxxx.getValue();
               double ☃xxxxxxxxxx = ☃xxxxxxxxx.getAmount();
               boolean ☃xxxxxxxxxxx = false;
               if (☃ != null) {
                  if (☃xxxxxxxxx.getID() == Item.ATTACK_DAMAGE_MODIFIER) {
                     ☃xxxxxxxxxx += ☃.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                     ☃xxxxxxxxxx += EnchantmentHelper.getModifierForCreature(this, EnumCreatureAttribute.UNDEFINED);
                     ☃xxxxxxxxxxx = true;
                  } else if (☃xxxxxxxxx.getID() == Item.ATTACK_SPEED_MODIFIER) {
                     ☃xxxxxxxxxx += ☃.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
                     ☃xxxxxxxxxxx = true;
                  }
               }

               double ☃xxxxxxxxxxxx;
               if (☃xxxxxxxxx.getOperation() != 1 && ☃xxxxxxxxx.getOperation() != 2) {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxx;
               } else {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxx * 100.0;
               }

               if (☃xxxxxxxxxxx) {
                  ☃.add(
                     " "
                        + I18n.translateToLocalFormatted(
                           "attribute.modifier.equals." + ☃xxxxxxxxx.getOperation(),
                           DECIMALFORMAT.format(☃xxxxxxxxxxxx),
                           I18n.translateToLocal("attribute.name." + ☃xxxxxxxx.getKey())
                        )
                  );
               } else if (☃xxxxxxxxxx > 0.0) {
                  ☃.add(
                     TextFormatting.BLUE
                        + " "
                        + I18n.translateToLocalFormatted(
                           "attribute.modifier.plus." + ☃xxxxxxxxx.getOperation(),
                           DECIMALFORMAT.format(☃xxxxxxxxxxxx),
                           I18n.translateToLocal("attribute.name." + ☃xxxxxxxx.getKey())
                        )
                  );
               } else if (☃xxxxxxxxxx < 0.0) {
                  ☃xxxxxxxxxxxx *= -1.0;
                  ☃.add(
                     TextFormatting.RED
                        + " "
                        + I18n.translateToLocalFormatted(
                           "attribute.modifier.take." + ☃xxxxxxxxx.getOperation(),
                           DECIMALFORMAT.format(☃xxxxxxxxxxxx),
                           I18n.translateToLocal("attribute.name." + ☃xxxxxxxx.getKey())
                        )
                  );
               }
            }
         }
      }

      if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (☃xxx & 4) == 0) {
         ☃.add(TextFormatting.BLUE + I18n.translateToLocal("item.unbreakable"));
      }

      if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (☃xxx & 8) == 0) {
         NBTTagList ☃xxxxxxx = this.stackTagCompound.getTagList("CanDestroy", 8);
         if (!☃xxxxxxx.isEmpty()) {
            ☃.add("");
            ☃.add(TextFormatting.GRAY + I18n.translateToLocal("item.canBreak"));

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxxxxxx.tagCount(); ☃xxxxxxxx++) {
               Block ☃xxxxxxxxxxxxx = Block.getBlockFromName(☃xxxxxxx.getStringTagAt(☃xxxxxxxx));
               if (☃xxxxxxxxxxxxx != null) {
                  ☃.add(TextFormatting.DARK_GRAY + ☃xxxxxxxxxxxxx.getLocalizedName());
               } else {
                  ☃.add(TextFormatting.DARK_GRAY + "missingno");
               }
            }
         }
      }

      if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (☃xxx & 16) == 0) {
         NBTTagList ☃xxxxxxx = this.stackTagCompound.getTagList("CanPlaceOn", 8);
         if (!☃xxxxxxx.isEmpty()) {
            ☃.add("");
            ☃.add(TextFormatting.GRAY + I18n.translateToLocal("item.canPlace"));

            for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < ☃xxxxxxx.tagCount(); ☃xxxxxxxxxxxxx++) {
               Block ☃xxxxxxxxxxxxxx = Block.getBlockFromName(☃xxxxxxx.getStringTagAt(☃xxxxxxxxxxxxx));
               if (☃xxxxxxxxxxxxxx != null) {
                  ☃.add(TextFormatting.DARK_GRAY + ☃xxxxxxxxxxxxxx.getLocalizedName());
               } else {
                  ☃.add(TextFormatting.DARK_GRAY + "missingno");
               }
            }
         }
      }

      if (☃.isAdvanced()) {
         if (this.isItemDamaged()) {
            ☃.add(I18n.translateToLocalFormatted("item.durability", this.getMaxDamage() - this.getItemDamage(), this.getMaxDamage()));
         }

         ☃.add(TextFormatting.DARK_GRAY + Item.REGISTRY.getNameForObject(this.item).toString());
         if (this.hasTagCompound()) {
            ☃.add(TextFormatting.DARK_GRAY + I18n.translateToLocalFormatted("item.nbt_tags", this.getTagCompound().getKeySet().size()));
         }
      }

      return ☃;
   }

   public boolean hasEffect() {
      return this.getItem().hasEffect(this);
   }

   public EnumRarity getRarity() {
      return this.getItem().getRarity(this);
   }

   public boolean isItemEnchantable() {
      return !this.getItem().isEnchantable(this) ? false : !this.isItemEnchanted();
   }

   public void addEnchantment(Enchantment var1, int var2) {
      if (this.stackTagCompound == null) {
         this.setTagCompound(new NBTTagCompound());
      }

      if (!this.stackTagCompound.hasKey("ench", 9)) {
         this.stackTagCompound.setTag("ench", new NBTTagList());
      }

      NBTTagList ☃ = this.stackTagCompound.getTagList("ench", 10);
      NBTTagCompound ☃x = new NBTTagCompound();
      ☃x.setShort("id", (short)Enchantment.getEnchantmentID(☃));
      ☃x.setShort("lvl", (byte)☃);
      ☃.appendTag(☃x);
   }

   public boolean isItemEnchanted() {
      return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9) ? !this.stackTagCompound.getTagList("ench", 10).isEmpty() : false;
   }

   public void setTagInfo(String var1, NBTBase var2) {
      if (this.stackTagCompound == null) {
         this.setTagCompound(new NBTTagCompound());
      }

      this.stackTagCompound.setTag(☃, ☃);
   }

   public boolean canEditBlocks() {
      return this.getItem().canItemEditBlocks();
   }

   public boolean isOnItemFrame() {
      return this.itemFrame != null;
   }

   public void setItemFrame(EntityItemFrame var1) {
      this.itemFrame = ☃;
   }

   @Nullable
   public EntityItemFrame getItemFrame() {
      return this.isEmpty ? null : this.itemFrame;
   }

   public int getRepairCost() {
      return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
   }

   public void setRepairCost(int var1) {
      if (!this.hasTagCompound()) {
         this.stackTagCompound = new NBTTagCompound();
      }

      this.stackTagCompound.setInteger("RepairCost", ☃);
   }

   public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot var1) {
      Multimap<String, AttributeModifier> ☃;
      if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
         ☃ = HashMultimap.create();
         NBTTagList ☃x = this.stackTagCompound.getTagList("AttributeModifiers", 10);

         for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
            NBTTagCompound ☃xxx = ☃x.getCompoundTagAt(☃xx);
            AttributeModifier ☃xxxx = SharedMonsterAttributes.readAttributeModifierFromNBT(☃xxx);
            if (☃xxxx != null
               && (!☃xxx.hasKey("Slot", 8) || ☃xxx.getString("Slot").equals(☃.getName()))
               && ☃xxxx.getID().getLeastSignificantBits() != 0L
               && ☃xxxx.getID().getMostSignificantBits() != 0L) {
               ☃.put(☃xxx.getString("AttributeName"), ☃xxxx);
            }
         }
      } else {
         ☃ = this.getItem().getItemAttributeModifiers(☃);
      }

      return ☃;
   }

   public void addAttributeModifier(String var1, AttributeModifier var2, @Nullable EntityEquipmentSlot var3) {
      if (this.stackTagCompound == null) {
         this.stackTagCompound = new NBTTagCompound();
      }

      if (!this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
         this.stackTagCompound.setTag("AttributeModifiers", new NBTTagList());
      }

      NBTTagList ☃ = this.stackTagCompound.getTagList("AttributeModifiers", 10);
      NBTTagCompound ☃x = SharedMonsterAttributes.writeAttributeModifierToNBT(☃);
      ☃x.setString("AttributeName", ☃);
      if (☃ != null) {
         ☃x.setString("Slot", ☃.getName());
      }

      ☃.appendTag(☃x);
   }

   public ITextComponent getTextComponent() {
      TextComponentString ☃ = new TextComponentString(this.getDisplayName());
      if (this.hasDisplayName()) {
         ☃.getStyle().setItalic(true);
      }

      ITextComponent ☃x = new TextComponentString("[").appendSibling(☃).appendText("]");
      if (!this.isEmpty) {
         NBTTagCompound ☃xx = this.writeToNBT(new NBTTagCompound());
         ☃x.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(☃xx.toString())));
         ☃x.getStyle().setColor(this.getRarity().color);
      }

      return ☃x;
   }

   public boolean canDestroy(Block var1) {
      if (☃ == this.canDestroyCacheBlock) {
         return this.canDestroyCacheResult;
      } else {
         this.canDestroyCacheBlock = ☃;
         if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            NBTTagList ☃ = this.stackTagCompound.getTagList("CanDestroy", 8);

            for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
               Block ☃xx = Block.getBlockFromName(☃.getStringTagAt(☃x));
               if (☃xx == ☃) {
                  this.canDestroyCacheResult = true;
                  return true;
               }
            }
         }

         this.canDestroyCacheResult = false;
         return false;
      }
   }

   public boolean canPlaceOn(Block var1) {
      if (☃ == this.canPlaceOnCacheBlock) {
         return this.canPlaceOnCacheResult;
      } else {
         this.canPlaceOnCacheBlock = ☃;
         if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
            NBTTagList ☃ = this.stackTagCompound.getTagList("CanPlaceOn", 8);

            for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
               Block ☃xx = Block.getBlockFromName(☃.getStringTagAt(☃x));
               if (☃xx == ☃) {
                  this.canPlaceOnCacheResult = true;
                  return true;
               }
            }
         }

         this.canPlaceOnCacheResult = false;
         return false;
      }
   }

   public int getAnimationsToGo() {
      return this.animationsToGo;
   }

   public void setAnimationsToGo(int var1) {
      this.animationsToGo = ☃;
   }

   public int getCount() {
      return this.isEmpty ? 0 : this.stackSize;
   }

   public void setCount(int var1) {
      this.stackSize = ☃;
      this.updateEmptyState();
   }

   public void grow(int var1) {
      this.setCount(this.stackSize + ☃);
   }

   public void shrink(int var1) {
      this.grow(-☃);
   }
}
