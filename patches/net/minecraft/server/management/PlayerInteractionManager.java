package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PlayerInteractionManager {
   public World world;
   public EntityPlayerMP player;
   private GameType gameType = GameType.NOT_SET;
   private boolean isDestroyingBlock;
   private int initialDamage;
   private BlockPos destroyPos = BlockPos.ORIGIN;
   private int curblockDamage;
   private boolean receivedFinishDiggingPacket;
   private BlockPos delayedDestroyPos = BlockPos.ORIGIN;
   private int initialBlockDamage;
   private int durabilityRemainingOnBlock = -1;

   public PlayerInteractionManager(World var1) {
      this.world = ☃;
   }

   public void setGameType(GameType var1) {
      this.gameType = ☃;
      ☃.configurePlayerCapabilities(this.player.capabilities);
      this.player.sendPlayerAbilities();
      this.player.server.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, this.player));
      this.world.updateAllPlayersSleepingFlag();
   }

   public GameType getGameType() {
      return this.gameType;
   }

   public boolean survivalOrAdventure() {
      return this.gameType.isSurvivalOrAdventure();
   }

   public boolean isCreative() {
      return this.gameType.isCreative();
   }

   public void initializeGameType(GameType var1) {
      if (this.gameType == GameType.NOT_SET) {
         this.gameType = ☃;
      }

      this.setGameType(this.gameType);
   }

   public void updateBlockRemoving() {
      this.curblockDamage++;
      if (this.receivedFinishDiggingPacket) {
         int ☃ = this.curblockDamage - this.initialBlockDamage;
         IBlockState ☃x = this.world.getBlockState(this.delayedDestroyPos);
         if (☃x.getMaterial() == Material.AIR) {
            this.receivedFinishDiggingPacket = false;
         } else {
            float ☃xx = ☃x.getPlayerRelativeBlockHardness(this.player, this.player.world, this.delayedDestroyPos) * (☃ + 1);
            int ☃xxx = (int)(☃xx * 10.0F);
            if (☃xxx != this.durabilityRemainingOnBlock) {
               this.world.sendBlockBreakProgress(this.player.getEntityId(), this.delayedDestroyPos, ☃xxx);
               this.durabilityRemainingOnBlock = ☃xxx;
            }

            if (☃xx >= 1.0F) {
               this.receivedFinishDiggingPacket = false;
               this.tryHarvestBlock(this.delayedDestroyPos);
            }
         }
      } else if (this.isDestroyingBlock) {
         IBlockState ☃ = this.world.getBlockState(this.destroyPos);
         if (☃.getMaterial() == Material.AIR) {
            this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
            this.durabilityRemainingOnBlock = -1;
            this.isDestroyingBlock = false;
         } else {
            int ☃x = this.curblockDamage - this.initialDamage;
            float ☃xxxx = ☃.getPlayerRelativeBlockHardness(this.player, this.player.world, this.delayedDestroyPos) * (☃x + 1);
            int ☃xxxxx = (int)(☃xxxx * 10.0F);
            if (☃xxxxx != this.durabilityRemainingOnBlock) {
               this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, ☃xxxxx);
               this.durabilityRemainingOnBlock = ☃xxxxx;
            }
         }
      }
   }

   public void onBlockClicked(BlockPos var1, EnumFacing var2) {
      if (this.isCreative()) {
         if (!this.world.extinguishFire(null, ☃, ☃)) {
            this.tryHarvestBlock(☃);
         }
      } else {
         IBlockState ☃ = this.world.getBlockState(☃);
         Block ☃x = ☃.getBlock();
         if (this.gameType.hasLimitedInteractions()) {
            if (this.gameType == GameType.SPECTATOR) {
               return;
            }

            if (!this.player.isAllowEdit()) {
               ItemStack ☃xx = this.player.getHeldItemMainhand();
               if (☃xx.isEmpty()) {
                  return;
               }

               if (!☃xx.canDestroy(☃x)) {
                  return;
               }
            }
         }

         this.world.extinguishFire(null, ☃, ☃);
         this.initialDamage = this.curblockDamage;
         float ☃xxx = 1.0F;
         if (☃.getMaterial() != Material.AIR) {
            ☃x.onBlockClicked(this.world, ☃, this.player);
            ☃xxx = ☃.getPlayerRelativeBlockHardness(this.player, this.player.world, ☃);
         }

         if (☃.getMaterial() != Material.AIR && ☃xxx >= 1.0F) {
            this.tryHarvestBlock(☃);
         } else {
            this.isDestroyingBlock = true;
            this.destroyPos = ☃;
            int ☃xxxx = (int)(☃xxx * 10.0F);
            this.world.sendBlockBreakProgress(this.player.getEntityId(), ☃, ☃xxxx);
            this.durabilityRemainingOnBlock = ☃xxxx;
         }
      }
   }

   public void blockRemoving(BlockPos var1) {
      if (☃.equals(this.destroyPos)) {
         int ☃ = this.curblockDamage - this.initialDamage;
         IBlockState ☃x = this.world.getBlockState(☃);
         if (☃x.getMaterial() != Material.AIR) {
            float ☃xx = ☃x.getPlayerRelativeBlockHardness(this.player, this.player.world, ☃) * (☃ + 1);
            if (☃xx >= 0.7F) {
               this.isDestroyingBlock = false;
               this.world.sendBlockBreakProgress(this.player.getEntityId(), ☃, -1);
               this.tryHarvestBlock(☃);
            } else if (!this.receivedFinishDiggingPacket) {
               this.isDestroyingBlock = false;
               this.receivedFinishDiggingPacket = true;
               this.delayedDestroyPos = ☃;
               this.initialBlockDamage = this.initialDamage;
            }
         }
      }
   }

   public void cancelDestroyingBlock() {
      this.isDestroyingBlock = false;
      this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
   }

   private boolean removeBlock(BlockPos var1) {
      IBlockState ☃ = this.world.getBlockState(☃);
      ☃.getBlock().onBlockHarvested(this.world, ☃, ☃, this.player);
      boolean ☃x = this.world.setBlockToAir(☃);
      if (☃x) {
         ☃.getBlock().onPlayerDestroy(this.world, ☃, ☃);
      }

      return ☃x;
   }

   public boolean tryHarvestBlock(BlockPos var1) {
      if (this.gameType.isCreative() && !this.player.getHeldItemMainhand().isEmpty() && this.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
         return false;
      } else {
         IBlockState ☃ = this.world.getBlockState(☃);
         TileEntity ☃x = this.world.getTileEntity(☃);
         Block ☃xx = ☃.getBlock();
         if ((☃xx instanceof BlockCommandBlock || ☃xx instanceof BlockStructure) && !this.player.canUseCommandBlock()) {
            this.world.notifyBlockUpdate(☃, ☃, ☃, 3);
            return false;
         } else {
            if (this.gameType.hasLimitedInteractions()) {
               if (this.gameType == GameType.SPECTATOR) {
                  return false;
               }

               if (!this.player.isAllowEdit()) {
                  ItemStack ☃xxx = this.player.getHeldItemMainhand();
                  if (☃xxx.isEmpty()) {
                     return false;
                  }

                  if (!☃xxx.canDestroy(☃xx)) {
                     return false;
                  }
               }
            }

            this.world.playEvent(this.player, 2001, ☃, Block.getStateId(☃));
            boolean ☃xxxx = this.removeBlock(☃);
            if (this.isCreative()) {
               this.player.connection.sendPacket(new SPacketBlockChange(this.world, ☃));
            } else {
               ItemStack ☃xxxxx = this.player.getHeldItemMainhand();
               ItemStack ☃xxxxxx = ☃xxxxx.isEmpty() ? ItemStack.EMPTY : ☃xxxxx.copy();
               boolean ☃xxxxxxx = this.player.canHarvestBlock(☃);
               if (!☃xxxxx.isEmpty()) {
                  ☃xxxxx.onBlockDestroyed(this.world, ☃, ☃, this.player);
               }

               if (☃xxxx && ☃xxxxxxx) {
                  ☃.getBlock().harvestBlock(this.world, this.player, ☃, ☃, ☃x, ☃xxxxxx);
               }
            }

            return ☃xxxx;
         }
      }
   }

   public EnumActionResult processRightClick(EntityPlayer var1, World var2, ItemStack var3, EnumHand var4) {
      if (this.gameType == GameType.SPECTATOR) {
         return EnumActionResult.PASS;
      } else if (☃.getCooldownTracker().hasCooldown(☃.getItem())) {
         return EnumActionResult.PASS;
      } else {
         int ☃ = ☃.getCount();
         int ☃x = ☃.getMetadata();
         ActionResult<ItemStack> ☃xx = ☃.useItemRightClick(☃, ☃, ☃);
         ItemStack ☃xxx = ☃xx.getResult();
         if (☃xxx == ☃ && ☃xxx.getCount() == ☃ && ☃xxx.getMaxItemUseDuration() <= 0 && ☃xxx.getMetadata() == ☃x) {
            return ☃xx.getType();
         } else if (☃xx.getType() == EnumActionResult.FAIL && ☃xxx.getMaxItemUseDuration() > 0 && !☃.isHandActive()) {
            return ☃xx.getType();
         } else {
            ☃.setHeldItem(☃, ☃xxx);
            if (this.isCreative()) {
               ☃xxx.setCount(☃);
               if (☃xxx.isItemStackDamageable()) {
                  ☃xxx.setItemDamage(☃x);
               }
            }

            if (☃xxx.isEmpty()) {
               ☃.setHeldItem(☃, ItemStack.EMPTY);
            }

            if (!☃.isHandActive()) {
               ((EntityPlayerMP)☃).sendContainerToPlayer(☃.inventoryContainer);
            }

            return ☃xx.getType();
         }
      }
   }

   public EnumActionResult processRightClickBlock(
      EntityPlayer var1, World var2, ItemStack var3, EnumHand var4, BlockPos var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (this.gameType == GameType.SPECTATOR) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof ILockableContainer) {
            Block ☃x = ☃.getBlockState(☃).getBlock();
            ILockableContainer ☃xx = (ILockableContainer)☃;
            if (☃xx instanceof TileEntityChest && ☃x instanceof BlockChest) {
               ☃xx = ((BlockChest)☃x).getLockableContainer(☃, ☃);
            }

            if (☃xx != null) {
               ☃.displayGUIChest(☃xx);
               return EnumActionResult.SUCCESS;
            }
         } else if (☃ instanceof IInventory) {
            ☃.displayGUIChest((IInventory)☃);
            return EnumActionResult.SUCCESS;
         }

         return EnumActionResult.PASS;
      } else {
         if (!☃.isSneaking() || ☃.getHeldItemMainhand().isEmpty() && ☃.getHeldItemOffhand().isEmpty()) {
            IBlockState ☃ = ☃.getBlockState(☃);
            if (☃.getBlock().onBlockActivated(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃)) {
               return EnumActionResult.SUCCESS;
            }
         }

         if (☃.isEmpty()) {
            return EnumActionResult.PASS;
         } else if (☃.getCooldownTracker().hasCooldown(☃.getItem())) {
            return EnumActionResult.PASS;
         } else {
            if (☃.getItem() instanceof ItemBlock && !☃.canUseCommandBlock()) {
               Block ☃ = ((ItemBlock)☃.getItem()).getBlock();
               if (☃ instanceof BlockCommandBlock || ☃ instanceof BlockStructure) {
                  return EnumActionResult.FAIL;
               }
            }

            if (this.isCreative()) {
               int ☃ = ☃.getMetadata();
               int ☃xxx = ☃.getCount();
               EnumActionResult ☃xxxx = ☃.onItemUse(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
               ☃.setItemDamage(☃);
               ☃.setCount(☃xxx);
               return ☃xxxx;
            } else {
               return ☃.onItemUse(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
            }
         }
      }
   }

   public void setWorld(WorldServer var1) {
      this.world = ☃;
   }
}
