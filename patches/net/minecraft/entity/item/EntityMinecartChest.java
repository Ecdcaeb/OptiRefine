package net.minecraft.entity.item;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartChest extends EntityMinecartContainer {
   public EntityMinecartChest(World var1) {
      super(☃);
   }

   public EntityMinecartChest(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesMinecartChest(DataFixer var0) {
      EntityMinecartContainer.addDataFixers(☃, EntityMinecartChest.class);
   }

   @Override
   public void killMinecart(DamageSource var1) {
      super.killMinecart(☃);
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
         this.dropItemWithOffset(Item.getItemFromBlock(Blocks.CHEST), 1, 0.0F);
      }
   }

   @Override
   public int getSizeInventory() {
      return 27;
   }

   @Override
   public EntityMinecart.Type getType() {
      return EntityMinecart.Type.CHEST;
   }

   @Override
   public IBlockState getDefaultDisplayTile() {
      return Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH);
   }

   @Override
   public int getDefaultDisplayTileOffset() {
      return 8;
   }

   @Override
   public String getGuiID() {
      return "minecraft:chest";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      this.addLoot(☃);
      return new ContainerChest(☃, this, ☃);
   }
}
