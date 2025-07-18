package net.minecraft.client.renderer.color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.world.ColorizerGrass;

public class ItemColors {
   private final ObjectIntIdentityMap<IItemColor> mapItemColors = new ObjectIntIdentityMap<>(32);

   public static ItemColors init(final BlockColors var0) {
      ItemColors ☃ = new ItemColors();
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            return ☃ > 0 ? -1 : ((ItemArmor)☃.getItem()).getColor(☃);
         }
      }, Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS);
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            BlockDoublePlant.EnumPlantType ☃ = BlockDoublePlant.EnumPlantType.byMetadata(☃.getMetadata());
            return ☃ != BlockDoublePlant.EnumPlantType.GRASS && ☃ != BlockDoublePlant.EnumPlantType.FERN ? -1 : ColorizerGrass.getGrassColor(0.5, 1.0);
         }
      }, Blocks.DOUBLE_PLANT);
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            if (☃ != 1) {
               return -1;
            } else {
               NBTBase ☃ = ItemFireworkCharge.getExplosionTag(☃, "Colors");
               if (!(☃ instanceof NBTTagIntArray)) {
                  return 9079434;
               } else {
                  int[] ☃x = ((NBTTagIntArray)☃).getIntArray();
                  if (☃x.length == 1) {
                     return ☃x[0];
                  } else {
                     int ☃xx = 0;
                     int ☃xxx = 0;
                     int ☃xxxx = 0;

                     for (int ☃xxxxx : ☃x) {
                        ☃xx += (☃xxxxx & 0xFF0000) >> 16;
                        ☃xxx += (☃xxxxx & 0xFF00) >> 8;
                        ☃xxxx += (☃xxxxx & 0xFF) >> 0;
                     }

                     ☃xx /= ☃x.length;
                     ☃xxx /= ☃x.length;
                     ☃xxxx /= ☃x.length;
                     return ☃xx << 16 | ☃xxx << 8 | ☃xxxx;
                  }
               }
            }
         }
      }, Items.FIREWORK_CHARGE);
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            return ☃ > 0 ? -1 : PotionUtils.getColor(☃);
         }
      }, Items.POTIONITEM, Items.SPLASH_POTION, Items.LINGERING_POTION);
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            EntityList.EntityEggInfo ☃ = EntityList.ENTITY_EGGS.get(ItemMonsterPlacer.getNamedIdFrom(☃));
            if (☃ == null) {
               return -1;
            } else {
               return ☃ == 0 ? ☃.primaryColor : ☃.secondaryColor;
            }
         }
      }, Items.SPAWN_EGG);
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            IBlockState ☃ = ((ItemBlock)☃.getItem()).getBlock().getStateFromMeta(☃.getMetadata());
            return ☃.colorMultiplier(☃, null, null, ☃);
         }
      }, Blocks.GRASS, Blocks.TALLGRASS, Blocks.VINE, Blocks.LEAVES, Blocks.LEAVES2, Blocks.WATERLILY);
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            return ☃ == 0 ? PotionUtils.getColor(☃) : -1;
         }
      }, Items.TIPPED_ARROW);
      ☃.registerItemColorHandler(new IItemColor() {
         @Override
         public int colorMultiplier(ItemStack var1, int var2) {
            return ☃ == 0 ? -1 : ItemMap.getColor(☃);
         }
      }, Items.FILLED_MAP);
      return ☃;
   }

   public int colorMultiplier(ItemStack var1, int var2) {
      IItemColor ☃ = this.mapItemColors.getByValue(Item.REGISTRY.getIDForObject(☃.getItem()));
      return ☃ == null ? -1 : ☃.colorMultiplier(☃, ☃);
   }

   public void registerItemColorHandler(IItemColor var1, Block... var2) {
      for (Block ☃ : ☃) {
         this.mapItemColors.put(☃, Item.getIdFromItem(Item.getItemFromBlock(☃)));
      }
   }

   public void registerItemColorHandler(IItemColor var1, Item... var2) {
      for (Item ☃ : ☃) {
         this.mapItemColors.put(☃, Item.getIdFromItem(☃));
      }
   }
}
