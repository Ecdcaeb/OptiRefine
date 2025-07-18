package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;

public class TileEntityBanner extends TileEntity implements IWorldNameable {
   private String name;
   private EnumDyeColor baseColor = EnumDyeColor.BLACK;
   private NBTTagList patterns;
   private boolean patternDataSet;
   private List<BannerPattern> patternList;
   private List<EnumDyeColor> colorList;
   private String patternResourceLocation;

   public void setItemValues(ItemStack var1, boolean var2) {
      this.patterns = null;
      NBTTagCompound ☃ = ☃.getSubCompound("BlockEntityTag");
      if (☃ != null && ☃.hasKey("Patterns", 9)) {
         this.patterns = ☃.getTagList("Patterns", 10).copy();
      }

      this.baseColor = ☃ ? getColor(☃) : ItemBanner.getBaseColor(☃);
      this.patternList = null;
      this.colorList = null;
      this.patternResourceLocation = "";
      this.patternDataSet = true;
      this.name = ☃.hasDisplayName() ? ☃.getDisplayName() : null;
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.name : "banner";
   }

   @Override
   public boolean hasCustomName() {
      return this.name != null && !this.name.isEmpty();
   }

   @Override
   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName()));
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setInteger("Base", this.baseColor.getDyeDamage());
      if (this.patterns != null) {
         ☃.setTag("Patterns", this.patterns);
      }

      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.name);
      }

      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      if (☃.hasKey("CustomName", 8)) {
         this.name = ☃.getString("CustomName");
      }

      this.baseColor = EnumDyeColor.byDyeDamage(☃.getInteger("Base"));
      this.patterns = ☃.getTagList("Patterns", 10);
      this.patternList = null;
      this.colorList = null;
      this.patternResourceLocation = null;
      this.patternDataSet = true;
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 6, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   public static int getPatterns(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getSubCompound("BlockEntityTag");
      return ☃ != null && ☃.hasKey("Patterns") ? ☃.getTagList("Patterns", 10).tagCount() : 0;
   }

   public List<BannerPattern> getPatternList() {
      this.initializeBannerData();
      return this.patternList;
   }

   public List<EnumDyeColor> getColorList() {
      this.initializeBannerData();
      return this.colorList;
   }

   public String getPatternResourceLocation() {
      this.initializeBannerData();
      return this.patternResourceLocation;
   }

   private void initializeBannerData() {
      if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {
         if (!this.patternDataSet) {
            this.patternResourceLocation = "";
         } else {
            this.patternList = Lists.newArrayList();
            this.colorList = Lists.newArrayList();
            this.patternList.add(BannerPattern.BASE);
            this.colorList.add(this.baseColor);
            this.patternResourceLocation = "b" + this.baseColor.getDyeDamage();
            if (this.patterns != null) {
               for (int ☃ = 0; ☃ < this.patterns.tagCount(); ☃++) {
                  NBTTagCompound ☃x = this.patterns.getCompoundTagAt(☃);
                  BannerPattern ☃xx = BannerPattern.byHash(☃x.getString("Pattern"));
                  if (☃xx != null) {
                     this.patternList.add(☃xx);
                     int ☃xxx = ☃x.getInteger("Color");
                     this.colorList.add(EnumDyeColor.byDyeDamage(☃xxx));
                     this.patternResourceLocation = this.patternResourceLocation + ☃xx.getHashname() + ☃xxx;
                  }
               }
            }
         }
      }
   }

   public static void removeBannerData(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getSubCompound("BlockEntityTag");
      if (☃ != null && ☃.hasKey("Patterns", 9)) {
         NBTTagList ☃x = ☃.getTagList("Patterns", 10);
         if (!☃x.isEmpty()) {
            ☃x.removeTag(☃x.tagCount() - 1);
            if (☃x.isEmpty()) {
               ☃.getTagCompound().removeTag("BlockEntityTag");
               if (☃.getTagCompound().isEmpty()) {
                  ☃.setTagCompound(null);
               }
            }
         }
      }
   }

   public ItemStack getItem() {
      ItemStack ☃ = ItemBanner.makeBanner(this.baseColor, this.patterns);
      if (this.hasCustomName()) {
         ☃.setStackDisplayName(this.getName());
      }

      return ☃;
   }

   public static EnumDyeColor getColor(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getSubCompound("BlockEntityTag");
      return ☃ != null && ☃.hasKey("Base") ? EnumDyeColor.byDyeDamage(☃.getInteger("Base")) : EnumDyeColor.BLACK;
   }
}
