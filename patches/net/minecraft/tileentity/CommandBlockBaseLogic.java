package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class CommandBlockBaseLogic implements ICommandSender {
   private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss");
   private long lastExecution = -1L;
   private boolean updateLastExecution = true;
   private int successCount;
   private boolean trackOutput = true;
   private ITextComponent lastOutput;
   private String commandStored = "";
   private String customName = "@";
   private final CommandResultStats resultStats = new CommandResultStats();

   public int getSuccessCount() {
      return this.successCount;
   }

   public void setSuccessCount(int var1) {
      this.successCount = ☃;
   }

   public ITextComponent getLastOutput() {
      return (ITextComponent)(this.lastOutput == null ? new TextComponentString("") : this.lastOutput);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      ☃.setString("Command", this.commandStored);
      ☃.setInteger("SuccessCount", this.successCount);
      ☃.setString("CustomName", this.customName);
      ☃.setBoolean("TrackOutput", this.trackOutput);
      if (this.lastOutput != null && this.trackOutput) {
         ☃.setString("LastOutput", ITextComponent.Serializer.componentToJson(this.lastOutput));
      }

      ☃.setBoolean("UpdateLastExecution", this.updateLastExecution);
      if (this.updateLastExecution && this.lastExecution > 0L) {
         ☃.setLong("LastExecution", this.lastExecution);
      }

      this.resultStats.writeStatsToNBT(☃);
      return ☃;
   }

   public void readDataFromNBT(NBTTagCompound var1) {
      this.commandStored = ☃.getString("Command");
      this.successCount = ☃.getInteger("SuccessCount");
      if (☃.hasKey("CustomName", 8)) {
         this.customName = ☃.getString("CustomName");
      }

      if (☃.hasKey("TrackOutput", 1)) {
         this.trackOutput = ☃.getBoolean("TrackOutput");
      }

      if (☃.hasKey("LastOutput", 8) && this.trackOutput) {
         try {
            this.lastOutput = ITextComponent.Serializer.jsonToComponent(☃.getString("LastOutput"));
         } catch (Throwable var3) {
            this.lastOutput = new TextComponentString(var3.getMessage());
         }
      } else {
         this.lastOutput = null;
      }

      if (☃.hasKey("UpdateLastExecution")) {
         this.updateLastExecution = ☃.getBoolean("UpdateLastExecution");
      }

      if (this.updateLastExecution && ☃.hasKey("LastExecution")) {
         this.lastExecution = ☃.getLong("LastExecution");
      } else {
         this.lastExecution = -1L;
      }

      this.resultStats.readStatsFromNBT(☃);
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      return ☃ <= 2;
   }

   public void setCommand(String var1) {
      this.commandStored = ☃;
      this.successCount = 0;
   }

   public String getCommand() {
      return this.commandStored;
   }

   public boolean trigger(World var1) {
      if (☃.isRemote || ☃.getTotalWorldTime() == this.lastExecution) {
         return false;
      } else if ("Searge".equalsIgnoreCase(this.commandStored)) {
         this.lastOutput = new TextComponentString("#itzlipofutzli");
         this.successCount = 1;
         return true;
      } else {
         MinecraftServer ☃ = this.getServer();
         if (☃ != null && ☃.isAnvilFileSet() && ☃.isCommandBlockEnabled()) {
            try {
               this.lastOutput = null;
               this.successCount = ☃.getCommandManager().executeCommand(this, this.commandStored);
            } catch (Throwable var6) {
               CrashReport ☃x = CrashReport.makeCrashReport(var6, "Executing command block");
               CrashReportCategory ☃xx = ☃x.makeCategory("Command to be executed");
               ☃xx.addDetail("Command", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     return CommandBlockBaseLogic.this.getCommand();
                  }
               });
               ☃xx.addDetail("Name", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     return CommandBlockBaseLogic.this.getName();
                  }
               });
               throw new ReportedException(☃x);
            }
         } else {
            this.successCount = 0;
         }

         if (this.updateLastExecution) {
            this.lastExecution = ☃.getTotalWorldTime();
         } else {
            this.lastExecution = -1L;
         }

         return true;
      }
   }

   @Override
   public String getName() {
      return this.customName;
   }

   public void setName(String var1) {
      this.customName = ☃;
   }

   @Override
   public void sendMessage(ITextComponent var1) {
      if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote) {
         this.lastOutput = new TextComponentString("[" + TIMESTAMP_FORMAT.format(new Date()) + "] ").appendSibling(☃);
         this.updateCommand();
      }
   }

   @Override
   public boolean sendCommandFeedback() {
      MinecraftServer ☃ = this.getServer();
      return ☃ == null || !☃.isAnvilFileSet() || ☃.worlds[0].getGameRules().getBoolean("commandBlockOutput");
   }

   @Override
   public void setCommandStat(CommandResultStats.Type var1, int var2) {
      this.resultStats.setCommandStatForSender(this.getServer(), this, ☃, ☃);
   }

   public abstract void updateCommand();

   public abstract int getCommandBlockType();

   public abstract void fillInInfo(ByteBuf var1);

   public void setLastOutput(@Nullable ITextComponent var1) {
      this.lastOutput = ☃;
   }

   public void setTrackOutput(boolean var1) {
      this.trackOutput = ☃;
   }

   public boolean shouldTrackOutput() {
      return this.trackOutput;
   }

   public boolean tryOpenEditCommandBlock(EntityPlayer var1) {
      if (!☃.canUseCommandBlock()) {
         return false;
      } else {
         if (☃.getEntityWorld().isRemote) {
            ☃.displayGuiEditCommandCart(this);
         }

         return true;
      }
   }

   public CommandResultStats getCommandResultStats() {
      return this.resultStats;
   }
}
