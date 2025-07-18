package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemRecord extends Item {
   private static final Map<SoundEvent, ItemRecord> RECORDS = Maps.newHashMap();
   private final SoundEvent sound;
   private final String displayName;

   protected ItemRecord(String var1, SoundEvent var2) {
      this.displayName = "item.record." + ☃ + ".desc";
      this.sound = ☃;
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.MISC);
      RECORDS.put(this.sound, this);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() == Blocks.JUKEBOX && !☃.getValue(BlockJukebox.HAS_RECORD)) {
         if (!☃.isRemote) {
            ItemStack ☃x = ☃.getHeldItem(☃);
            ((BlockJukebox)Blocks.JUKEBOX).insertRecord(☃, ☃, ☃, ☃x);
            ☃.playEvent(null, 1010, ☃, Item.getIdFromItem(this));
            ☃x.shrink(1);
            ☃.addStat(StatList.RECORD_PLAYED);
         }

         return EnumActionResult.SUCCESS;
      } else {
         return EnumActionResult.PASS;
      }
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      ☃.add(this.getRecordNameLocal());
   }

   public String getRecordNameLocal() {
      return I18n.translateToLocal(this.displayName);
   }

   @Override
   public EnumRarity getRarity(ItemStack var1) {
      return EnumRarity.RARE;
   }

   @Nullable
   public static ItemRecord getBySound(SoundEvent var0) {
      return RECORDS.get(☃);
   }

   public SoundEvent getSound() {
      return this.sound;
   }
}
