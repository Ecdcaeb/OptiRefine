package net.minecraft.command;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandResultStats {
   private static final int NUM_RESULT_TYPES = CommandResultStats.Type.values().length;
   private static final String[] STRING_RESULT_TYPES = new String[NUM_RESULT_TYPES];
   private String[] entitiesID = STRING_RESULT_TYPES;
   private String[] objectives = STRING_RESULT_TYPES;

   public void setCommandStatForSender(MinecraftServer var1, final ICommandSender var2, CommandResultStats.Type var3, int var4) {
      String ☃ = this.entitiesID[☃.getTypeID()];
      if (☃ != null) {
         ICommandSender ☃x = new ICommandSender() {
            @Override
            public String getName() {
               return ☃.getName();
            }

            @Override
            public ITextComponent getDisplayName() {
               return ☃.getDisplayName();
            }

            @Override
            public void sendMessage(ITextComponent var1) {
               ☃.sendMessage(☃);
            }

            @Override
            public boolean canUseCommand(int var1, String var2x) {
               return true;
            }

            @Override
            public BlockPos getPosition() {
               return ☃.getPosition();
            }

            @Override
            public Vec3d getPositionVector() {
               return ☃.getPositionVector();
            }

            @Override
            public World getEntityWorld() {
               return ☃.getEntityWorld();
            }

            @Override
            public Entity getCommandSenderEntity() {
               return ☃.getCommandSenderEntity();
            }

            @Override
            public boolean sendCommandFeedback() {
               return ☃.sendCommandFeedback();
            }

            @Override
            public void setCommandStat(CommandResultStats.Type var1, int var2x) {
               ☃.setCommandStat(☃, ☃);
            }

            @Override
            public MinecraftServer getServer() {
               return ☃.getServer();
            }
         };

         String ☃xx;
         try {
            ☃xx = CommandBase.getEntityName(☃, ☃x, ☃);
         } catch (CommandException var12) {
            return;
         }

         String ☃xxx = this.objectives[☃.getTypeID()];
         if (☃xxx != null) {
            Scoreboard ☃xxxx = ☃.getEntityWorld().getScoreboard();
            ScoreObjective ☃xxxxx = ☃xxxx.getObjective(☃xxx);
            if (☃xxxxx != null) {
               if (☃xxxx.entityHasObjective(☃xx, ☃xxxxx)) {
                  Score ☃xxxxxx = ☃xxxx.getOrCreateScore(☃xx, ☃xxxxx);
                  ☃xxxxxx.setScorePoints(☃);
               }
            }
         }
      }
   }

   public void readStatsFromNBT(NBTTagCompound var1) {
      if (☃.hasKey("CommandStats", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("CommandStats");

         for (CommandResultStats.Type ☃x : CommandResultStats.Type.values()) {
            String ☃xx = ☃x.getTypeName() + "Name";
            String ☃xxx = ☃x.getTypeName() + "Objective";
            if (☃.hasKey(☃xx, 8) && ☃.hasKey(☃xxx, 8)) {
               String ☃xxxx = ☃.getString(☃xx);
               String ☃xxxxx = ☃.getString(☃xxx);
               setScoreBoardStat(this, ☃x, ☃xxxx, ☃xxxxx);
            }
         }
      }
   }

   public void writeStatsToNBT(NBTTagCompound var1) {
      NBTTagCompound ☃ = new NBTTagCompound();

      for (CommandResultStats.Type ☃x : CommandResultStats.Type.values()) {
         String ☃xx = this.entitiesID[☃x.getTypeID()];
         String ☃xxx = this.objectives[☃x.getTypeID()];
         if (☃xx != null && ☃xxx != null) {
            ☃.setString(☃x.getTypeName() + "Name", ☃xx);
            ☃.setString(☃x.getTypeName() + "Objective", ☃xxx);
         }
      }

      if (!☃.isEmpty()) {
         ☃.setTag("CommandStats", ☃);
      }
   }

   public static void setScoreBoardStat(CommandResultStats var0, CommandResultStats.Type var1, @Nullable String var2, @Nullable String var3) {
      if (☃ != null && !☃.isEmpty() && ☃ != null && !☃.isEmpty()) {
         if (☃.entitiesID == STRING_RESULT_TYPES || ☃.objectives == STRING_RESULT_TYPES) {
            ☃.entitiesID = new String[NUM_RESULT_TYPES];
            ☃.objectives = new String[NUM_RESULT_TYPES];
         }

         ☃.entitiesID[☃.getTypeID()] = ☃;
         ☃.objectives[☃.getTypeID()] = ☃;
      } else {
         removeScoreBoardStat(☃, ☃);
      }
   }

   private static void removeScoreBoardStat(CommandResultStats var0, CommandResultStats.Type var1) {
      if (☃.entitiesID != STRING_RESULT_TYPES && ☃.objectives != STRING_RESULT_TYPES) {
         ☃.entitiesID[☃.getTypeID()] = null;
         ☃.objectives[☃.getTypeID()] = null;
         boolean ☃ = true;

         for (CommandResultStats.Type ☃x : CommandResultStats.Type.values()) {
            if (☃.entitiesID[☃x.getTypeID()] != null && ☃.objectives[☃x.getTypeID()] != null) {
               ☃ = false;
               break;
            }
         }

         if (☃) {
            ☃.entitiesID = STRING_RESULT_TYPES;
            ☃.objectives = STRING_RESULT_TYPES;
         }
      }
   }

   public void addAllStats(CommandResultStats var1) {
      for (CommandResultStats.Type ☃ : CommandResultStats.Type.values()) {
         setScoreBoardStat(this, ☃, ☃.entitiesID[☃.getTypeID()], ☃.objectives[☃.getTypeID()]);
      }
   }

   public static enum Type {
      SUCCESS_COUNT(0, "SuccessCount"),
      AFFECTED_BLOCKS(1, "AffectedBlocks"),
      AFFECTED_ENTITIES(2, "AffectedEntities"),
      AFFECTED_ITEMS(3, "AffectedItems"),
      QUERY_RESULT(4, "QueryResult");

      final int typeID;
      final String typeName;

      private Type(int var3, String var4) {
         this.typeID = ☃;
         this.typeName = ☃;
      }

      public int getTypeID() {
         return this.typeID;
      }

      public String getTypeName() {
         return this.typeName;
      }

      public static String[] getTypeNames() {
         String[] ☃ = new String[values().length];
         int ☃x = 0;

         for (CommandResultStats.Type ☃xx : values()) {
            ☃[☃x++] = ☃xx.getTypeName();
         }

         return ☃;
      }

      @Nullable
      public static CommandResultStats.Type getTypeByName(String var0) {
         for (CommandResultStats.Type ☃ : values()) {
            if (☃.getTypeName().equals(☃)) {
               return ☃;
            }
         }

         return null;
      }
   }
}
