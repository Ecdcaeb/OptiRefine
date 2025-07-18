package net.minecraft.client.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ElytraSound;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditCommandBlockMinecart;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiEditStructure;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class EntityPlayerSP extends AbstractClientPlayer {
   public final NetHandlerPlayClient connection;
   private final StatisticsManager statWriter;
   private final RecipeBook recipeBook;
   private int permissionLevel = 0;
   private double lastReportedPosX;
   private double lastReportedPosY;
   private double lastReportedPosZ;
   private float lastReportedYaw;
   private float lastReportedPitch;
   private boolean prevOnGround;
   private boolean serverSneakState;
   private boolean serverSprintState;
   private int positionUpdateTicks;
   private boolean hasValidHealth;
   private String serverBrand;
   public MovementInput movementInput;
   protected Minecraft mc;
   protected int sprintToggleTimer;
   public int sprintingTicksLeft;
   public float renderArmYaw;
   public float renderArmPitch;
   public float prevRenderArmYaw;
   public float prevRenderArmPitch;
   private int horseJumpPowerCounter;
   private float horseJumpPower;
   public float timeInPortal;
   public float prevTimeInPortal;
   private boolean handActive;
   private EnumHand activeHand;
   private boolean rowingBoat;
   private boolean autoJumpEnabled = true;
   private int autoJumpTime;
   private boolean wasFallFlying;

   public EntityPlayerSP(Minecraft var1, World var2, NetHandlerPlayClient var3, StatisticsManager var4, RecipeBook var5) {
      super(☃, ☃.getGameProfile());
      this.connection = ☃;
      this.statWriter = ☃;
      this.recipeBook = ☃;
      this.mc = ☃;
      this.dimension = 0;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return false;
   }

   @Override
   public void heal(float var1) {
   }

   @Override
   public boolean startRiding(Entity var1, boolean var2) {
      if (!super.startRiding(☃, ☃)) {
         return false;
      } else {
         if (☃ instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)☃));
         }

         if (☃ instanceof EntityBoat) {
            this.prevRotationYaw = ☃.rotationYaw;
            this.rotationYaw = ☃.rotationYaw;
            this.setRotationYawHead(☃.rotationYaw);
         }

         return true;
      }
   }

   @Override
   public void dismountRidingEntity() {
      super.dismountRidingEntity();
      this.rowingBoat = false;
   }

   @Override
   public Vec3d getLook(float var1) {
      return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
   }

   @Override
   public void onUpdate() {
      if (this.world.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
         super.onUpdate();
         if (this.isRiding()) {
            this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
            this.connection.sendPacket(new CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            Entity ☃ = this.getLowestRidingEntity();
            if (☃ != this && ☃.canPassengerSteer()) {
               this.connection.sendPacket(new CPacketVehicleMove(☃));
            }
         } else {
            this.onUpdateWalkingPlayer();
         }
      }
   }

   private void onUpdateWalkingPlayer() {
      boolean ☃ = this.isSprinting();
      if (☃ != this.serverSprintState) {
         if (☃) {
            this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SPRINTING));
         } else {
            this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SPRINTING));
         }

         this.serverSprintState = ☃;
      }

      boolean ☃x = this.isSneaking();
      if (☃x != this.serverSneakState) {
         if (☃x) {
            this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SNEAKING));
         } else {
            this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SNEAKING));
         }

         this.serverSneakState = ☃x;
      }

      if (this.isCurrentViewEntity()) {
         AxisAlignedBB ☃xx = this.getEntityBoundingBox();
         double ☃xxx = this.posX - this.lastReportedPosX;
         double ☃xxxx = ☃xx.minY - this.lastReportedPosY;
         double ☃xxxxx = this.posZ - this.lastReportedPosZ;
         double ☃xxxxxx = this.rotationYaw - this.lastReportedYaw;
         double ☃xxxxxxx = this.rotationPitch - this.lastReportedPitch;
         this.positionUpdateTicks++;
         boolean ☃xxxxxxxx = ☃xxx * ☃xxx + ☃xxxx * ☃xxxx + ☃xxxxx * ☃xxxxx > 9.0E-4 || this.positionUpdateTicks >= 20;
         boolean ☃xxxxxxxxx = ☃xxxxxx != 0.0 || ☃xxxxxxx != 0.0;
         if (this.isRiding()) {
            this.connection
               .sendPacket(new CPacketPlayer.PositionRotation(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
            ☃xxxxxxxx = false;
         } else if (☃xxxxxxxx && ☃xxxxxxxxx) {
            this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.posX, ☃xx.minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
         } else if (☃xxxxxxxx) {
            this.connection.sendPacket(new CPacketPlayer.Position(this.posX, ☃xx.minY, this.posZ, this.onGround));
         } else if (☃xxxxxxxxx) {
            this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
         } else if (this.prevOnGround != this.onGround) {
            this.connection.sendPacket(new CPacketPlayer(this.onGround));
         }

         if (☃xxxxxxxx) {
            this.lastReportedPosX = this.posX;
            this.lastReportedPosY = ☃xx.minY;
            this.lastReportedPosZ = this.posZ;
            this.positionUpdateTicks = 0;
         }

         if (☃xxxxxxxxx) {
            this.lastReportedYaw = this.rotationYaw;
            this.lastReportedPitch = this.rotationPitch;
         }

         this.prevOnGround = this.onGround;
         this.autoJumpEnabled = this.mc.gameSettings.autoJump;
      }
   }

   @Nullable
   @Override
   public EntityItem dropItem(boolean var1) {
      CPacketPlayerDigging.Action ☃ = ☃ ? CPacketPlayerDigging.Action.DROP_ALL_ITEMS : CPacketPlayerDigging.Action.DROP_ITEM;
      this.connection.sendPacket(new CPacketPlayerDigging(☃, BlockPos.ORIGIN, EnumFacing.DOWN));
      return null;
   }

   @Override
   protected ItemStack dropItemAndGetStack(EntityItem var1) {
      return ItemStack.EMPTY;
   }

   public void sendChatMessage(String var1) {
      this.connection.sendPacket(new CPacketChatMessage(☃));
   }

   @Override
   public void swingArm(EnumHand var1) {
      super.swingArm(☃);
      this.connection.sendPacket(new CPacketAnimation(☃));
   }

   @Override
   public void respawnPlayer() {
      this.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
   }

   @Override
   protected void damageEntity(DamageSource var1, float var2) {
      if (!this.isEntityInvulnerable(☃)) {
         this.setHealth(this.getHealth() - ☃);
      }
   }

   @Override
   public void closeScreen() {
      this.connection.sendPacket(new CPacketCloseWindow(this.openContainer.windowId));
      this.closeScreenAndDropStack();
   }

   public void closeScreenAndDropStack() {
      this.inventory.setItemStack(ItemStack.EMPTY);
      super.closeScreen();
      this.mc.displayGuiScreen(null);
   }

   public void setPlayerSPHealth(float var1) {
      if (this.hasValidHealth) {
         float ☃ = this.getHealth() - ☃;
         if (☃ <= 0.0F) {
            this.setHealth(☃);
            if (☃ < 0.0F) {
               this.hurtResistantTime = this.maxHurtResistantTime / 2;
            }
         } else {
            this.lastDamage = ☃;
            this.setHealth(this.getHealth());
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(DamageSource.GENERIC, ☃);
            this.maxHurtTime = 10;
            this.hurtTime = this.maxHurtTime;
         }
      } else {
         this.setHealth(☃);
         this.hasValidHealth = true;
      }
   }

   @Override
   public void addStat(StatBase var1, int var2) {
      if (☃ != null) {
         if (☃.isIndependent) {
            super.addStat(☃, ☃);
         }
      }
   }

   @Override
   public void sendPlayerAbilities() {
      this.connection.sendPacket(new CPacketPlayerAbilities(this.capabilities));
   }

   @Override
   public boolean isUser() {
      return true;
   }

   protected void sendHorseJump() {
      this.connection
         .sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_RIDING_JUMP, MathHelper.floor(this.getHorseJumpPower() * 100.0F)));
   }

   public void sendHorseInventory() {
      this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.OPEN_INVENTORY));
   }

   public void setServerBrand(String var1) {
      this.serverBrand = ☃;
   }

   public String getServerBrand() {
      return this.serverBrand;
   }

   public StatisticsManager getStatFileWriter() {
      return this.statWriter;
   }

   public RecipeBook getRecipeBook() {
      return this.recipeBook;
   }

   public void removeRecipeHighlight(IRecipe var1) {
      if (this.recipeBook.isNew(☃)) {
         this.recipeBook.markSeen(☃);
         this.connection.sendPacket(new CPacketRecipeInfo(☃));
      }
   }

   public int getPermissionLevel() {
      return this.permissionLevel;
   }

   public void setPermissionLevel(int var1) {
      this.permissionLevel = ☃;
   }

   @Override
   public void sendStatusMessage(ITextComponent var1, boolean var2) {
      if (☃) {
         this.mc.ingameGUI.setOverlayMessage(☃, false);
      } else {
         this.mc.ingameGUI.getChatGUI().printChatMessage(☃);
      }
   }

   @Override
   protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
      if (this.noClip) {
         return false;
      } else {
         BlockPos ☃ = new BlockPos(☃, ☃, ☃);
         double ☃x = ☃ - ☃.getX();
         double ☃xx = ☃ - ☃.getZ();
         if (!this.isOpenBlockSpace(☃)) {
            int ☃xxx = -1;
            double ☃xxxx = 9999.0;
            if (this.isOpenBlockSpace(☃.west()) && ☃x < ☃xxxx) {
               ☃xxxx = ☃x;
               ☃xxx = 0;
            }

            if (this.isOpenBlockSpace(☃.east()) && 1.0 - ☃x < ☃xxxx) {
               ☃xxxx = 1.0 - ☃x;
               ☃xxx = 1;
            }

            if (this.isOpenBlockSpace(☃.north()) && ☃xx < ☃xxxx) {
               ☃xxxx = ☃xx;
               ☃xxx = 4;
            }

            if (this.isOpenBlockSpace(☃.south()) && 1.0 - ☃xx < ☃xxxx) {
               ☃xxxx = 1.0 - ☃xx;
               ☃xxx = 5;
            }

            float ☃xxxxx = 0.1F;
            if (☃xxx == 0) {
               this.motionX = -0.1F;
            }

            if (☃xxx == 1) {
               this.motionX = 0.1F;
            }

            if (☃xxx == 4) {
               this.motionZ = -0.1F;
            }

            if (☃xxx == 5) {
               this.motionZ = 0.1F;
            }
         }

         return false;
      }
   }

   private boolean isOpenBlockSpace(BlockPos var1) {
      return !this.world.getBlockState(☃).isNormalCube() && !this.world.getBlockState(☃.up()).isNormalCube();
   }

   @Override
   public void setSprinting(boolean var1) {
      super.setSprinting(☃);
      this.sprintingTicksLeft = 0;
   }

   public void setXPStats(float var1, int var2, int var3) {
      this.experience = ☃;
      this.experienceTotal = ☃;
      this.experienceLevel = ☃;
   }

   @Override
   public void sendMessage(ITextComponent var1) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(☃);
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      return ☃ <= this.getPermissionLevel();
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ >= 24 && ☃ <= 28) {
         this.setPermissionLevel(☃ - 24);
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   @Override
   public BlockPos getPosition() {
      return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
   }

   @Override
   public void playSound(SoundEvent var1, float var2, float var3) {
      this.world.playSound(this.posX, this.posY, this.posZ, ☃, this.getSoundCategory(), ☃, ☃, false);
   }

   @Override
   public boolean isServerWorld() {
      return true;
   }

   @Override
   public void setActiveHand(EnumHand var1) {
      ItemStack ☃ = this.getHeldItem(☃);
      if (!☃.isEmpty() && !this.isHandActive()) {
         super.setActiveHand(☃);
         this.handActive = true;
         this.activeHand = ☃;
      }
   }

   @Override
   public boolean isHandActive() {
      return this.handActive;
   }

   @Override
   public void resetActiveHand() {
      super.resetActiveHand();
      this.handActive = false;
   }

   @Override
   public EnumHand getActiveHand() {
      return this.activeHand;
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      super.notifyDataManagerChange(☃);
      if (HAND_STATES.equals(☃)) {
         boolean ☃ = (this.dataManager.get(HAND_STATES) & 1) > 0;
         EnumHand ☃x = (this.dataManager.get(HAND_STATES) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
         if (☃ && !this.handActive) {
            this.setActiveHand(☃x);
         } else if (!☃ && this.handActive) {
            this.resetActiveHand();
         }
      }

      if (FLAGS.equals(☃) && this.isElytraFlying() && !this.wasFallFlying) {
         this.mc.getSoundHandler().playSound(new ElytraSound(this));
      }
   }

   public boolean isRidingHorse() {
      Entity ☃ = this.getRidingEntity();
      return this.isRiding() && ☃ instanceof IJumpingMount && ((IJumpingMount)☃).canJump();
   }

   public float getHorseJumpPower() {
      return this.horseJumpPower;
   }

   @Override
   public void openEditSign(TileEntitySign var1) {
      this.mc.displayGuiScreen(new GuiEditSign(☃));
   }

   @Override
   public void displayGuiEditCommandCart(CommandBlockBaseLogic var1) {
      this.mc.displayGuiScreen(new GuiEditCommandBlockMinecart(☃));
   }

   @Override
   public void displayGuiCommandBlock(TileEntityCommandBlock var1) {
      this.mc.displayGuiScreen(new GuiCommandBlock(☃));
   }

   @Override
   public void openEditStructure(TileEntityStructure var1) {
      this.mc.displayGuiScreen(new GuiEditStructure(☃));
   }

   @Override
   public void openBook(ItemStack var1, EnumHand var2) {
      Item ☃ = ☃.getItem();
      if (☃ == Items.WRITABLE_BOOK) {
         this.mc.displayGuiScreen(new GuiScreenBook(this, ☃, true));
      }
   }

   @Override
   public void displayGUIChest(IInventory var1) {
      String ☃ = ☃ instanceof IInteractionObject ? ((IInteractionObject)☃).getGuiID() : "minecraft:container";
      if ("minecraft:chest".equals(☃)) {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, ☃));
      } else if ("minecraft:hopper".equals(☃)) {
         this.mc.displayGuiScreen(new GuiHopper(this.inventory, ☃));
      } else if ("minecraft:furnace".equals(☃)) {
         this.mc.displayGuiScreen(new GuiFurnace(this.inventory, ☃));
      } else if ("minecraft:brewing_stand".equals(☃)) {
         this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, ☃));
      } else if ("minecraft:beacon".equals(☃)) {
         this.mc.displayGuiScreen(new GuiBeacon(this.inventory, ☃));
      } else if ("minecraft:dispenser".equals(☃) || "minecraft:dropper".equals(☃)) {
         this.mc.displayGuiScreen(new GuiDispenser(this.inventory, ☃));
      } else if ("minecraft:shulker_box".equals(☃)) {
         this.mc.displayGuiScreen(new GuiShulkerBox(this.inventory, ☃));
      } else {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, ☃));
      }
   }

   @Override
   public void openGuiHorseInventory(AbstractHorse var1, IInventory var2) {
      this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, ☃, ☃));
   }

   @Override
   public void displayGui(IInteractionObject var1) {
      String ☃ = ☃.getGuiID();
      if ("minecraft:crafting_table".equals(☃)) {
         this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.world));
      } else if ("minecraft:enchanting_table".equals(☃)) {
         this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.world, ☃));
      } else if ("minecraft:anvil".equals(☃)) {
         this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.world));
      }
   }

   @Override
   public void displayVillagerTradeGui(IMerchant var1) {
      this.mc.displayGuiScreen(new GuiMerchant(this.inventory, ☃, this.world));
   }

   @Override
   public void onCriticalHit(Entity var1) {
      this.mc.effectRenderer.emitParticleAtEntity(☃, EnumParticleTypes.CRIT);
   }

   @Override
   public void onEnchantmentCritical(Entity var1) {
      this.mc.effectRenderer.emitParticleAtEntity(☃, EnumParticleTypes.CRIT_MAGIC);
   }

   @Override
   public boolean isSneaking() {
      boolean ☃ = this.movementInput != null && this.movementInput.sneak;
      return ☃ && !this.sleeping;
   }

   @Override
   public void updateEntityActionState() {
      super.updateEntityActionState();
      if (this.isCurrentViewEntity()) {
         this.moveStrafing = this.movementInput.moveStrafe;
         this.moveForward = this.movementInput.moveForward;
         this.isJumping = this.movementInput.jump;
         this.prevRenderArmYaw = this.renderArmYaw;
         this.prevRenderArmPitch = this.renderArmPitch;
         this.renderArmPitch = (float)(this.renderArmPitch + (this.rotationPitch - this.renderArmPitch) * 0.5);
         this.renderArmYaw = (float)(this.renderArmYaw + (this.rotationYaw - this.renderArmYaw) * 0.5);
      }
   }

   protected boolean isCurrentViewEntity() {
      return this.mc.getRenderViewEntity() == this;
   }

   @Override
   public void onLivingUpdate() {
      this.sprintingTicksLeft++;
      if (this.sprintToggleTimer > 0) {
         this.sprintToggleTimer--;
      }

      this.prevTimeInPortal = this.timeInPortal;
      if (this.inPortal) {
         if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
            if (this.mc.currentScreen instanceof GuiContainer) {
               this.closeScreen();
            }

            this.mc.displayGuiScreen(null);
         }

         if (this.timeInPortal == 0.0F) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRIGGER, this.rand.nextFloat() * 0.4F + 0.8F));
         }

         this.timeInPortal += 0.0125F;
         if (this.timeInPortal >= 1.0F) {
            this.timeInPortal = 1.0F;
         }

         this.inPortal = false;
      } else if (this.isPotionActive(MobEffects.NAUSEA) && this.getActivePotionEffect(MobEffects.NAUSEA).getDuration() > 60) {
         this.timeInPortal += 0.006666667F;
         if (this.timeInPortal > 1.0F) {
            this.timeInPortal = 1.0F;
         }
      } else {
         if (this.timeInPortal > 0.0F) {
            this.timeInPortal -= 0.05F;
         }

         if (this.timeInPortal < 0.0F) {
            this.timeInPortal = 0.0F;
         }
      }

      if (this.timeUntilPortal > 0) {
         this.timeUntilPortal--;
      }

      boolean ☃ = this.movementInput.jump;
      boolean ☃x = this.movementInput.sneak;
      float ☃xx = 0.8F;
      boolean ☃xxx = this.movementInput.moveForward >= 0.8F;
      this.movementInput.updatePlayerMoveState();
      this.mc.getTutorial().handleMovement(this.movementInput);
      if (this.isHandActive() && !this.isRiding()) {
         this.movementInput.moveStrafe *= 0.2F;
         this.movementInput.moveForward *= 0.2F;
         this.sprintToggleTimer = 0;
      }

      boolean ☃xxxx = false;
      if (this.autoJumpTime > 0) {
         this.autoJumpTime--;
         ☃xxxx = true;
         this.movementInput.jump = true;
      }

      AxisAlignedBB ☃xxxxx = this.getEntityBoundingBox();
      this.pushOutOfBlocks(this.posX - this.width * 0.35, ☃xxxxx.minY + 0.5, this.posZ + this.width * 0.35);
      this.pushOutOfBlocks(this.posX - this.width * 0.35, ☃xxxxx.minY + 0.5, this.posZ - this.width * 0.35);
      this.pushOutOfBlocks(this.posX + this.width * 0.35, ☃xxxxx.minY + 0.5, this.posZ - this.width * 0.35);
      this.pushOutOfBlocks(this.posX + this.width * 0.35, ☃xxxxx.minY + 0.5, this.posZ + this.width * 0.35);
      boolean ☃xxxxxx = this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
      if (this.onGround
         && !☃x
         && !☃xxx
         && this.movementInput.moveForward >= 0.8F
         && !this.isSprinting()
         && ☃xxxxxx
         && !this.isHandActive()
         && !this.isPotionActive(MobEffects.BLINDNESS)) {
         if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.sprintToggleTimer = 7;
         } else {
            this.setSprinting(true);
         }
      }

      if (!this.isSprinting()
         && this.movementInput.moveForward >= 0.8F
         && ☃xxxxxx
         && !this.isHandActive()
         && !this.isPotionActive(MobEffects.BLINDNESS)
         && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
         this.setSprinting(true);
      }

      if (this.isSprinting() && (this.movementInput.moveForward < 0.8F || this.collidedHorizontally || !☃xxxxxx)) {
         this.setSprinting(false);
      }

      if (this.capabilities.allowFlying) {
         if (this.mc.playerController.isSpectatorMode()) {
            if (!this.capabilities.isFlying) {
               this.capabilities.isFlying = true;
               this.sendPlayerAbilities();
            }
         } else if (!☃ && this.movementInput.jump && !☃xxxx) {
            if (this.flyToggleTimer == 0) {
               this.flyToggleTimer = 7;
            } else {
               this.capabilities.isFlying = !this.capabilities.isFlying;
               this.sendPlayerAbilities();
               this.flyToggleTimer = 0;
            }
         }
      }

      if (this.movementInput.jump && !☃ && !this.onGround && this.motionY < 0.0 && !this.isElytraFlying() && !this.capabilities.isFlying) {
         ItemStack ☃xxxxxxx = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
         if (☃xxxxxxx.getItem() == Items.ELYTRA && ItemElytra.isUsable(☃xxxxxxx)) {
            this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_FALL_FLYING));
         }
      }

      this.wasFallFlying = this.isElytraFlying();
      if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
         if (this.movementInput.sneak) {
            this.movementInput.moveStrafe = (float)(this.movementInput.moveStrafe / 0.3);
            this.movementInput.moveForward = (float)(this.movementInput.moveForward / 0.3);
            this.motionY = this.motionY - this.capabilities.getFlySpeed() * 3.0F;
         }

         if (this.movementInput.jump) {
            this.motionY = this.motionY + this.capabilities.getFlySpeed() * 3.0F;
         }
      }

      if (this.isRidingHorse()) {
         IJumpingMount ☃xxxxxxx = (IJumpingMount)this.getRidingEntity();
         if (this.horseJumpPowerCounter < 0) {
            this.horseJumpPowerCounter++;
            if (this.horseJumpPowerCounter == 0) {
               this.horseJumpPower = 0.0F;
            }
         }

         if (☃ && !this.movementInput.jump) {
            this.horseJumpPowerCounter = -10;
            ☃xxxxxxx.setJumpPower(MathHelper.floor(this.getHorseJumpPower() * 100.0F));
            this.sendHorseJump();
         } else if (!☃ && this.movementInput.jump) {
            this.horseJumpPowerCounter = 0;
            this.horseJumpPower = 0.0F;
         } else if (☃) {
            this.horseJumpPowerCounter++;
            if (this.horseJumpPowerCounter < 10) {
               this.horseJumpPower = this.horseJumpPowerCounter * 0.1F;
            } else {
               this.horseJumpPower = 0.8F + 2.0F / (this.horseJumpPowerCounter - 9) * 0.1F;
            }
         }
      } else {
         this.horseJumpPower = 0.0F;
      }

      super.onLivingUpdate();
      if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
         this.capabilities.isFlying = false;
         this.sendPlayerAbilities();
      }
   }

   @Override
   public void updateRidden() {
      super.updateRidden();
      this.rowingBoat = false;
      if (this.getRidingEntity() instanceof EntityBoat) {
         EntityBoat ☃ = (EntityBoat)this.getRidingEntity();
         ☃.updateInputs(this.movementInput.leftKeyDown, this.movementInput.rightKeyDown, this.movementInput.forwardKeyDown, this.movementInput.backKeyDown);
         this.rowingBoat = this.rowingBoat
            | (this.movementInput.leftKeyDown || this.movementInput.rightKeyDown || this.movementInput.forwardKeyDown || this.movementInput.backKeyDown);
      }
   }

   public boolean isRowingBoat() {
      return this.rowingBoat;
   }

   @Nullable
   @Override
   public PotionEffect removeActivePotionEffect(@Nullable Potion var1) {
      if (☃ == MobEffects.NAUSEA) {
         this.prevTimeInPortal = 0.0F;
         this.timeInPortal = 0.0F;
      }

      return super.removeActivePotionEffect(☃);
   }

   @Override
   public void move(MoverType var1, double var2, double var4, double var6) {
      double ☃ = this.posX;
      double ☃x = this.posZ;
      super.move(☃, ☃, ☃, ☃);
      this.updateAutoJump((float)(this.posX - ☃), (float)(this.posZ - ☃x));
   }

   public boolean isAutoJumpEnabled() {
      return this.autoJumpEnabled;
   }

   protected void updateAutoJump(float var1, float var2) {
      if (this.isAutoJumpEnabled()) {
         if (this.autoJumpTime <= 0 && this.onGround && !this.isSneaking() && !this.isRiding()) {
            Vec2f ☃ = this.movementInput.getMoveVector();
            if (☃.x != 0.0F || ☃.y != 0.0F) {
               Vec3d ☃x = new Vec3d(this.posX, this.getEntityBoundingBox().minY, this.posZ);
               Vec3d ☃xx = new Vec3d(this.posX + ☃, this.getEntityBoundingBox().minY, this.posZ + ☃);
               Vec3d ☃xxx = new Vec3d(☃, 0.0, ☃);
               float ☃xxxx = this.getAIMoveSpeed();
               float ☃xxxxx = (float)☃xxx.lengthSquared();
               if (☃xxxxx <= 0.001F) {
                  float ☃xxxxxx = ☃xxxx * ☃.x;
                  float ☃xxxxxxx = ☃xxxx * ☃.y;
                  float ☃xxxxxxxx = MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0));
                  float ☃xxxxxxxxx = MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0));
                  ☃xxx = new Vec3d(☃xxxxxx * ☃xxxxxxxxx - ☃xxxxxxx * ☃xxxxxxxx, ☃xxx.y, ☃xxxxxxx * ☃xxxxxxxxx + ☃xxxxxx * ☃xxxxxxxx);
                  ☃xxxxx = (float)☃xxx.lengthSquared();
                  if (☃xxxxx <= 0.001F) {
                     return;
                  }
               }

               float ☃xxxxxx = (float)MathHelper.fastInvSqrt(☃xxxxx);
               Vec3d ☃xxxxxxx = ☃xxx.scale(☃xxxxxx);
               Vec3d ☃xxxxxxxx = this.getForward();
               float ☃xxxxxxxxx = (float)(☃xxxxxxxx.x * ☃xxxxxxx.x + ☃xxxxxxxx.z * ☃xxxxxxx.z);
               if (!(☃xxxxxxxxx < -0.15F)) {
                  BlockPos ☃xxxxxxxxxx = new BlockPos(this.posX, this.getEntityBoundingBox().maxY, this.posZ);
                  IBlockState ☃xxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxx);
                  if (☃xxxxxxxxxxx.getCollisionBoundingBox(this.world, ☃xxxxxxxxxx) == null) {
                     ☃xxxxxxxxxx = ☃xxxxxxxxxx.up();
                     IBlockState ☃xxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxx);
                     if (☃xxxxxxxxxxxx.getCollisionBoundingBox(this.world, ☃xxxxxxxxxx) == null) {
                        float ☃xxxxxxxxxxxxx = 7.0F;
                        float ☃xxxxxxxxxxxxxx = 1.2F;
                        if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                           ☃xxxxxxxxxxxxxx += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75F;
                        }

                        float ☃xxxxxxxxxxxxxxx = Math.max(☃xxxx * 7.0F, 1.0F / ☃xxxxxx);
                        Vec3d ☃xxxxxxxxxxxxxxxx = ☃xx.add(☃xxxxxxx.scale(☃xxxxxxxxxxxxxxx));
                        float ☃xxxxxxxxxxxxxxxxx = this.width;
                        float ☃xxxxxxxxxxxxxxxxxx = this.height;
                        AxisAlignedBB ☃xxxxxxxxxxxxxxxxxxx = new AxisAlignedBB(☃x, ☃xxxxxxxxxxxxxxxx.add(0.0, ☃xxxxxxxxxxxxxxxxxx, 0.0))
                           .grow(☃xxxxxxxxxxxxxxxxx, 0.0, ☃xxxxxxxxxxxxxxxxx);
                        Vec3d var19 = ☃x.add(0.0, 0.51F, 0.0);
                        ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.add(0.0, 0.51F, 0.0);
                        Vec3d ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxx.crossProduct(new Vec3d(0.0, 1.0, 0.0));
                        Vec3d ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx.scale(☃xxxxxxxxxxxxxxxxx * 0.5F);
                        Vec3d ☃xxxxxxxxxxxxxxxxxxxxxx = var19.subtract(☃xxxxxxxxxxxxxxxxxxxxx);
                        Vec3d ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.subtract(☃xxxxxxxxxxxxxxxxxxxxx);
                        Vec3d ☃xxxxxxxxxxxxxxxxxxxxxxxx = var19.add(☃xxxxxxxxxxxxxxxxxxxxx);
                        Vec3d ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.add(☃xxxxxxxxxxxxxxxxxxxxx);
                        List<AxisAlignedBB> ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = this.world.getCollisionBoxes(this, ☃xxxxxxxxxxxxxxxxxxx);
                        if (!☃xxxxxxxxxxxxxxxxxxxxxxxxxx.isEmpty()) {
                        }

                        float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = Float.MIN_VALUE;

                        for (AxisAlignedBB ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxxxxxxxxxxxx) {
                           if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx.intersects(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx)
                              || ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx.intersects(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx)) {
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = (float)☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx.maxY;
                              Vec3d ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx.getCenter();
                              BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx);

                              for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1;
                                 ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxx;
                                 ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++
                              ) {
                                 BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.up(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
                                 IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
                                 AxisAlignedBB ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
                                 if ((
                                       ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getCollisionBoundingBox(
                                          this.world, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                       )
                                    )
                                    != null) {
                                    ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = (float)☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.maxY + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getY();
                                    if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx - this.getEntityBoundingBox().minY > ☃xxxxxxxxxxxxxx) {
                                       return;
                                    }
                                 }

                                 if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > 1) {
                                    ☃xxxxxxxxxx = ☃xxxxxxxxxx.up();
                                    IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxx);
                                    if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getCollisionBoundingBox(this.world, ☃xxxxxxxxxx) != null) {
                                       return;
                                    }
                                 }
                              }
                              break;
                           }
                        }

                        if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx != Float.MIN_VALUE) {
                           float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (float)(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx - this.getEntityBoundingBox().minY);
                           if (!(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx <= 0.5F) && !(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx > ☃xxxxxxxxxxxxxx)) {
                              this.autoJumpTime = 1;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
