package net.minecraft.client.multiplayer;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class PlayerControllerMP {
   private final Minecraft mc;
   private final NetHandlerPlayClient connection;
   private BlockPos currentBlock = new BlockPos(-1, -1, -1);
   private ItemStack currentItemHittingBlock = ItemStack.EMPTY;
   private float curBlockDamageMP;
   private float stepSoundTickCounter;
   private int blockHitDelay;
   private boolean isHittingBlock;
   private GameType currentGameType = GameType.SURVIVAL;
   private int currentPlayerItem;

   public PlayerControllerMP(Minecraft var1, NetHandlerPlayClient var2) {
      this.mc = ☃;
      this.connection = ☃;
   }

   public static void clickBlockCreative(Minecraft var0, PlayerControllerMP var1, BlockPos var2, EnumFacing var3) {
      if (!☃.world.extinguishFire(☃.player, ☃, ☃)) {
         ☃.onPlayerDestroyBlock(☃);
      }
   }

   public void setPlayerCapabilities(EntityPlayer var1) {
      this.currentGameType.configurePlayerCapabilities(☃.capabilities);
   }

   public boolean isSpectator() {
      return this.currentGameType == GameType.SPECTATOR;
   }

   public void setGameType(GameType var1) {
      this.currentGameType = ☃;
      this.currentGameType.configurePlayerCapabilities(this.mc.player.capabilities);
   }

   public void flipPlayer(EntityPlayer var1) {
      ☃.rotationYaw = -180.0F;
   }

   public boolean shouldDrawHUD() {
      return this.currentGameType.isSurvivalOrAdventure();
   }

   public boolean onPlayerDestroyBlock(BlockPos var1) {
      if (this.currentGameType.hasLimitedInteractions()) {
         if (this.currentGameType == GameType.SPECTATOR) {
            return false;
         }

         if (!this.mc.player.isAllowEdit()) {
            ItemStack ☃ = this.mc.player.getHeldItemMainhand();
            if (☃.isEmpty()) {
               return false;
            }

            if (!☃.canDestroy(this.mc.world.getBlockState(☃).getBlock())) {
               return false;
            }
         }
      }

      if (this.currentGameType.isCreative()
         && !this.mc.player.getHeldItemMainhand().isEmpty()
         && this.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
         return false;
      } else {
         World ☃x = this.mc.world;
         IBlockState ☃xx = ☃x.getBlockState(☃);
         Block ☃xxx = ☃xx.getBlock();
         if ((☃xxx instanceof BlockCommandBlock || ☃xxx instanceof BlockStructure) && !this.mc.player.canUseCommandBlock()) {
            return false;
         } else if (☃xx.getMaterial() == Material.AIR) {
            return false;
         } else {
            ☃x.playEvent(2001, ☃, Block.getStateId(☃xx));
            ☃xxx.onBlockHarvested(☃x, ☃, ☃xx, this.mc.player);
            boolean ☃xxxx = ☃x.setBlockState(☃, Blocks.AIR.getDefaultState(), 11);
            if (☃xxxx) {
               ☃xxx.onPlayerDestroy(☃x, ☃, ☃xx);
            }

            this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
            if (!this.currentGameType.isCreative()) {
               ItemStack ☃xxxxx = this.mc.player.getHeldItemMainhand();
               if (!☃xxxxx.isEmpty()) {
                  ☃xxxxx.onBlockDestroyed(☃x, ☃xx, ☃, this.mc.player);
                  if (☃xxxxx.isEmpty()) {
                     this.mc.player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                  }
               }
            }

            return ☃xxxx;
         }
      }
   }

   public boolean clickBlock(BlockPos var1, EnumFacing var2) {
      if (this.currentGameType.hasLimitedInteractions()) {
         if (this.currentGameType == GameType.SPECTATOR) {
            return false;
         }

         if (!this.mc.player.isAllowEdit()) {
            ItemStack ☃ = this.mc.player.getHeldItemMainhand();
            if (☃.isEmpty()) {
               return false;
            }

            if (!☃.canDestroy(this.mc.world.getBlockState(☃).getBlock())) {
               return false;
            }
         }
      }

      if (!this.mc.world.getWorldBorder().contains(☃)) {
         return false;
      } else {
         if (this.currentGameType.isCreative()) {
            this.mc.getTutorial().onHitBlock(this.mc.world, ☃, this.mc.world.getBlockState(☃), 1.0F);
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, ☃, ☃));
            clickBlockCreative(this.mc, this, ☃, ☃);
            this.blockHitDelay = 5;
         } else if (!this.isHittingBlock || !this.isHittingPosition(☃)) {
            if (this.isHittingBlock) {
               this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, ☃));
            }

            IBlockState ☃x = this.mc.world.getBlockState(☃);
            this.mc.getTutorial().onHitBlock(this.mc.world, ☃, ☃x, 0.0F);
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, ☃, ☃));
            boolean ☃xx = ☃x.getMaterial() != Material.AIR;
            if (☃xx && this.curBlockDamageMP == 0.0F) {
               ☃x.getBlock().onBlockClicked(this.mc.world, ☃, this.mc.player);
            }

            if (☃xx && ☃x.getPlayerRelativeBlockHardness(this.mc.player, this.mc.player.world, ☃) >= 1.0F) {
               this.onPlayerDestroyBlock(☃);
            } else {
               this.isHittingBlock = true;
               this.currentBlock = ☃;
               this.currentItemHittingBlock = this.mc.player.getHeldItemMainhand();
               this.curBlockDamageMP = 0.0F;
               this.stepSoundTickCounter = 0.0F;
               this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
            }
         }

         return true;
      }
   }

   public void resetBlockRemoving() {
      if (this.isHittingBlock) {
         this.mc.getTutorial().onHitBlock(this.mc.world, this.currentBlock, this.mc.world.getBlockState(this.currentBlock), -1.0F);
         this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
         this.isHittingBlock = false;
         this.curBlockDamageMP = 0.0F;
         this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityId(), this.currentBlock, -1);
         this.mc.player.resetCooldown();
      }
   }

   public boolean onPlayerDamageBlock(BlockPos var1, EnumFacing var2) {
      this.syncCurrentPlayItem();
      if (this.blockHitDelay > 0) {
         this.blockHitDelay--;
         return true;
      } else if (this.currentGameType.isCreative() && this.mc.world.getWorldBorder().contains(☃)) {
         this.blockHitDelay = 5;
         this.mc.getTutorial().onHitBlock(this.mc.world, ☃, this.mc.world.getBlockState(☃), 1.0F);
         this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, ☃, ☃));
         clickBlockCreative(this.mc, this, ☃, ☃);
         return true;
      } else if (this.isHittingPosition(☃)) {
         IBlockState ☃ = this.mc.world.getBlockState(☃);
         Block ☃x = ☃.getBlock();
         if (☃.getMaterial() == Material.AIR) {
            this.isHittingBlock = false;
            return false;
         } else {
            this.curBlockDamageMP = this.curBlockDamageMP + ☃.getPlayerRelativeBlockHardness(this.mc.player, this.mc.player.world, ☃);
            if (this.stepSoundTickCounter % 4.0F == 0.0F) {
               SoundType ☃xx = ☃x.getSoundType();
               this.mc
                  .getSoundHandler()
                  .playSound(new PositionedSoundRecord(☃xx.getHitSound(), SoundCategory.NEUTRAL, (☃xx.getVolume() + 1.0F) / 8.0F, ☃xx.getPitch() * 0.5F, ☃));
            }

            this.stepSoundTickCounter++;
            this.mc.getTutorial().onHitBlock(this.mc.world, ☃, ☃, MathHelper.clamp(this.curBlockDamageMP, 0.0F, 1.0F));
            if (this.curBlockDamageMP >= 1.0F) {
               this.isHittingBlock = false;
               this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, ☃, ☃));
               this.onPlayerDestroyBlock(☃);
               this.curBlockDamageMP = 0.0F;
               this.stepSoundTickCounter = 0.0F;
               this.blockHitDelay = 5;
            }

            this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
            return true;
         }
      } else {
         return this.clickBlock(☃, ☃);
      }
   }

   public float getBlockReachDistance() {
      return this.currentGameType.isCreative() ? 5.0F : 4.5F;
   }

   public void updateController() {
      this.syncCurrentPlayItem();
      if (this.connection.getNetworkManager().isChannelOpen()) {
         this.connection.getNetworkManager().processReceivedPackets();
      } else {
         this.connection.getNetworkManager().handleDisconnection();
      }
   }

   private boolean isHittingPosition(BlockPos var1) {
      ItemStack ☃ = this.mc.player.getHeldItemMainhand();
      boolean ☃x = this.currentItemHittingBlock.isEmpty() && ☃.isEmpty();
      if (!this.currentItemHittingBlock.isEmpty() && !☃.isEmpty()) {
         ☃x = ☃.getItem() == this.currentItemHittingBlock.getItem()
            && ItemStack.areItemStackTagsEqual(☃, this.currentItemHittingBlock)
            && (☃.isItemStackDamageable() || ☃.getMetadata() == this.currentItemHittingBlock.getMetadata());
      }

      return ☃.equals(this.currentBlock) && ☃x;
   }

   private void syncCurrentPlayItem() {
      int ☃ = this.mc.player.inventory.currentItem;
      if (☃ != this.currentPlayerItem) {
         this.currentPlayerItem = ☃;
         this.connection.sendPacket(new CPacketHeldItemChange(this.currentPlayerItem));
      }
   }

   public EnumActionResult processRightClickBlock(EntityPlayerSP var1, WorldClient var2, BlockPos var3, EnumFacing var4, Vec3d var5, EnumHand var6) {
      this.syncCurrentPlayItem();
      ItemStack ☃ = ☃.getHeldItem(☃);
      float ☃x = (float)(☃.x - ☃.getX());
      float ☃xx = (float)(☃.y - ☃.getY());
      float ☃xxx = (float)(☃.z - ☃.getZ());
      boolean ☃xxxx = false;
      if (!this.mc.world.getWorldBorder().contains(☃)) {
         return EnumActionResult.FAIL;
      } else {
         if (this.currentGameType != GameType.SPECTATOR) {
            IBlockState ☃xxxxx = ☃.getBlockState(☃);
            if ((!☃.isSneaking() || ☃.getHeldItemMainhand().isEmpty() && ☃.getHeldItemOffhand().isEmpty())
               && ☃xxxxx.getBlock().onBlockActivated(☃, ☃, ☃xxxxx, ☃, ☃, ☃, ☃x, ☃xx, ☃xxx)) {
               ☃xxxx = true;
            }

            if (!☃xxxx && ☃.getItem() instanceof ItemBlock) {
               ItemBlock ☃xxxxxx = (ItemBlock)☃.getItem();
               if (!☃xxxxxx.canPlaceBlockOnSide(☃, ☃, ☃, ☃, ☃)) {
                  return EnumActionResult.FAIL;
               }
            }
         }

         this.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(☃, ☃, ☃, ☃x, ☃xx, ☃xxx));
         if (☃xxxx || this.currentGameType == GameType.SPECTATOR) {
            return EnumActionResult.SUCCESS;
         } else if (☃.isEmpty()) {
            return EnumActionResult.PASS;
         } else if (☃.getCooldownTracker().hasCooldown(☃.getItem())) {
            return EnumActionResult.PASS;
         } else {
            if (☃.getItem() instanceof ItemBlock && !☃.canUseCommandBlock()) {
               Block ☃xxxxxx = ((ItemBlock)☃.getItem()).getBlock();
               if (☃xxxxxx instanceof BlockCommandBlock || ☃xxxxxx instanceof BlockStructure) {
                  return EnumActionResult.FAIL;
               }
            }

            if (this.currentGameType.isCreative()) {
               int ☃xxxxxx = ☃.getMetadata();
               int ☃xxxxxxx = ☃.getCount();
               EnumActionResult ☃xxxxxxxx = ☃.onItemUse(☃, ☃, ☃, ☃, ☃, ☃x, ☃xx, ☃xxx);
               ☃.setItemDamage(☃xxxxxx);
               ☃.setCount(☃xxxxxxx);
               return ☃xxxxxxxx;
            } else {
               return ☃.onItemUse(☃, ☃, ☃, ☃, ☃, ☃x, ☃xx, ☃xxx);
            }
         }
      }
   }

   public EnumActionResult processRightClick(EntityPlayer var1, World var2, EnumHand var3) {
      if (this.currentGameType == GameType.SPECTATOR) {
         return EnumActionResult.PASS;
      } else {
         this.syncCurrentPlayItem();
         this.connection.sendPacket(new CPacketPlayerTryUseItem(☃));
         ItemStack ☃ = ☃.getHeldItem(☃);
         if (☃.getCooldownTracker().hasCooldown(☃.getItem())) {
            return EnumActionResult.PASS;
         } else {
            int ☃x = ☃.getCount();
            ActionResult<ItemStack> ☃xx = ☃.useItemRightClick(☃, ☃, ☃);
            ItemStack ☃xxx = ☃xx.getResult();
            if (☃xxx != ☃ || ☃xxx.getCount() != ☃x) {
               ☃.setHeldItem(☃, ☃xxx);
            }

            return ☃xx.getType();
         }
      }
   }

   public EntityPlayerSP createPlayer(World var1, StatisticsManager var2, RecipeBook var3) {
      return new EntityPlayerSP(this.mc, ☃, this.connection, ☃, ☃);
   }

   public void attackEntity(EntityPlayer var1, Entity var2) {
      this.syncCurrentPlayItem();
      this.connection.sendPacket(new CPacketUseEntity(☃));
      if (this.currentGameType != GameType.SPECTATOR) {
         ☃.attackTargetEntityWithCurrentItem(☃);
         ☃.resetCooldown();
      }
   }

   public EnumActionResult interactWithEntity(EntityPlayer var1, Entity var2, EnumHand var3) {
      this.syncCurrentPlayItem();
      this.connection.sendPacket(new CPacketUseEntity(☃, ☃));
      return this.currentGameType == GameType.SPECTATOR ? EnumActionResult.PASS : ☃.interactOn(☃, ☃);
   }

   public EnumActionResult interactWithEntity(EntityPlayer var1, Entity var2, RayTraceResult var3, EnumHand var4) {
      this.syncCurrentPlayItem();
      Vec3d ☃ = new Vec3d(☃.hitVec.x - ☃.posX, ☃.hitVec.y - ☃.posY, ☃.hitVec.z - ☃.posZ);
      this.connection.sendPacket(new CPacketUseEntity(☃, ☃, ☃));
      return this.currentGameType == GameType.SPECTATOR ? EnumActionResult.PASS : ☃.applyPlayerInteraction(☃, ☃, ☃);
   }

   public ItemStack windowClick(int var1, int var2, int var3, ClickType var4, EntityPlayer var5) {
      short ☃ = ☃.openContainer.getNextTransactionID(☃.inventory);
      ItemStack ☃x = ☃.openContainer.slotClick(☃, ☃, ☃, ☃);
      this.connection.sendPacket(new CPacketClickWindow(☃, ☃, ☃, ☃, ☃x, ☃));
      return ☃x;
   }

   public void func_194338_a(int var1, IRecipe var2, boolean var3, EntityPlayer var4) {
      this.connection.sendPacket(new CPacketPlaceRecipe(☃, ☃, ☃));
   }

   public void sendEnchantPacket(int var1, int var2) {
      this.connection.sendPacket(new CPacketEnchantItem(☃, ☃));
   }

   public void sendSlotPacket(ItemStack var1, int var2) {
      if (this.currentGameType.isCreative()) {
         this.connection.sendPacket(new CPacketCreativeInventoryAction(☃, ☃));
      }
   }

   public void sendPacketDropItem(ItemStack var1) {
      if (this.currentGameType.isCreative() && !☃.isEmpty()) {
         this.connection.sendPacket(new CPacketCreativeInventoryAction(-1, ☃));
      }
   }

   public void onStoppedUsingItem(EntityPlayer var1) {
      this.syncCurrentPlayItem();
      this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      ☃.stopActiveHand();
   }

   public boolean gameIsSurvivalOrAdventure() {
      return this.currentGameType.isSurvivalOrAdventure();
   }

   public boolean isNotCreative() {
      return !this.currentGameType.isCreative();
   }

   public boolean isInCreativeMode() {
      return this.currentGameType.isCreative();
   }

   public boolean extendedReach() {
      return this.currentGameType.isCreative();
   }

   public boolean isRidingHorse() {
      return this.mc.player.isRiding() && this.mc.player.getRidingEntity() instanceof AbstractHorse;
   }

   public boolean isSpectatorMode() {
      return this.currentGameType == GameType.SPECTATOR;
   }

   public GameType getCurrentGameType() {
      return this.currentGameType;
   }

   public boolean getIsHittingBlock() {
      return this.isHittingBlock;
   }

   public void pickItem(int var1) {
      this.connection.sendPacket(new CPacketCustomPayload("MC|PickItem", new PacketBuffer(Unpooled.buffer()).writeVarInt(☃)));
   }
}
