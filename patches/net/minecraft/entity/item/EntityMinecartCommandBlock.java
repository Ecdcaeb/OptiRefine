package net.minecraft.entity.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityMinecartCommandBlock extends EntityMinecart {
   private static final DataParameter<String> COMMAND = EntityDataManager.createKey(EntityMinecartCommandBlock.class, DataSerializers.STRING);
   private static final DataParameter<ITextComponent> LAST_OUTPUT = EntityDataManager.createKey(
      EntityMinecartCommandBlock.class, DataSerializers.TEXT_COMPONENT
   );
   private final CommandBlockBaseLogic commandBlockLogic = new CommandBlockBaseLogic() {
      @Override
      public void updateCommand() {
         EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.COMMAND, this.getCommand());
         EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.LAST_OUTPUT, this.getLastOutput());
      }

      @Override
      public int getCommandBlockType() {
         return 1;
      }

      @Override
      public void fillInInfo(ByteBuf var1) {
         ☃.writeInt(EntityMinecartCommandBlock.this.getEntityId());
      }

      @Override
      public BlockPos getPosition() {
         return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5, EntityMinecartCommandBlock.this.posZ);
      }

      @Override
      public Vec3d getPositionVector() {
         return new Vec3d(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
      }

      @Override
      public World getEntityWorld() {
         return EntityMinecartCommandBlock.this.world;
      }

      @Override
      public Entity getCommandSenderEntity() {
         return EntityMinecartCommandBlock.this;
      }

      @Override
      public MinecraftServer getServer() {
         return EntityMinecartCommandBlock.this.world.getMinecraftServer();
      }
   };
   private int activatorRailCooldown;

   public EntityMinecartCommandBlock(World var1) {
      super(☃);
   }

   public EntityMinecartCommandBlock(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesMinecartCommand(DataFixer var0) {
      EntityMinecart.registerFixesMinecart(☃, EntityMinecartCommandBlock.class);
      ☃.registerWalker(FixTypes.ENTITY, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (TileEntity.getKey(TileEntityCommandBlock.class).equals(new ResourceLocation(☃.getString("id")))) {
               ☃.setString("id", "Control");
               ☃.process(FixTypes.BLOCK_ENTITY, ☃, ☃);
               ☃.setString("id", "MinecartCommandBlock");
            }

            return ☃;
         }
      });
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.getDataManager().register(COMMAND, "");
      this.getDataManager().register(LAST_OUTPUT, new TextComponentString(""));
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.commandBlockLogic.readDataFromNBT(☃);
      this.getDataManager().set(COMMAND, this.getCommandBlockLogic().getCommand());
      this.getDataManager().set(LAST_OUTPUT, this.getCommandBlockLogic().getLastOutput());
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      this.commandBlockLogic.writeToNBT(☃);
   }

   @Override
   public EntityMinecart.Type getType() {
      return EntityMinecart.Type.COMMAND_BLOCK;
   }

   @Override
   public IBlockState getDefaultDisplayTile() {
      return Blocks.COMMAND_BLOCK.getDefaultState();
   }

   public CommandBlockBaseLogic getCommandBlockLogic() {
      return this.commandBlockLogic;
   }

   @Override
   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      if (☃ && this.ticksExisted - this.activatorRailCooldown >= 4) {
         this.getCommandBlockLogic().trigger(this.world);
         this.activatorRailCooldown = this.ticksExisted;
      }
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      this.commandBlockLogic.tryOpenEditCommandBlock(☃);
      return false;
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      super.notifyDataManagerChange(☃);
      if (LAST_OUTPUT.equals(☃)) {
         try {
            this.commandBlockLogic.setLastOutput(this.getDataManager().get(LAST_OUTPUT));
         } catch (Throwable var3) {
         }
      } else if (COMMAND.equals(☃)) {
         this.commandBlockLogic.setCommand(this.getDataManager().get(COMMAND));
      }
   }

   @Override
   public boolean ignoreItemEntityData() {
      return true;
   }
}
