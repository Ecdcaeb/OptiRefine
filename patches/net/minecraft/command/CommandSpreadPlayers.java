package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CommandSpreadPlayers extends CommandBase {
   @Override
   public String getName() {
      return "spreadplayers";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.spreadplayers.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 6) {
         throw new WrongUsageException("commands.spreadplayers.usage");
      } else {
         int ☃ = 0;
         BlockPos ☃x = ☃.getPosition();
         double ☃xx = parseDouble(☃x.getX(), ☃[☃++], true);
         double ☃xxx = parseDouble(☃x.getZ(), ☃[☃++], true);
         double ☃xxxx = parseDouble(☃[☃++], 0.0);
         double ☃xxxxx = parseDouble(☃[☃++], ☃xxxx + 1.0);
         boolean ☃xxxxxx = parseBoolean(☃[☃++]);
         List<Entity> ☃xxxxxxx = Lists.newArrayList();

         while (☃ < ☃.length) {
            String ☃xxxxxxxx = ☃[☃++];
            if (EntitySelector.isSelector(☃xxxxxxxx)) {
               List<Entity> ☃xxxxxxxxx = EntitySelector.matchEntities(☃, ☃xxxxxxxx, Entity.class);
               if (☃xxxxxxxxx.isEmpty()) {
                  throw new EntityNotFoundException("commands.generic.selector.notFound", ☃xxxxxxxx);
               }

               ☃xxxxxxx.addAll(☃xxxxxxxxx);
            } else {
               EntityPlayer ☃xxxxxxxxx = ☃.getPlayerList().getPlayerByUsername(☃xxxxxxxx);
               if (☃xxxxxxxxx == null) {
                  throw new PlayerNotFoundException("commands.generic.player.notFound", ☃xxxxxxxx);
               }

               ☃xxxxxxx.add(☃xxxxxxxxx);
            }
         }

         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, ☃xxxxxxx.size());
         if (☃xxxxxxx.isEmpty()) {
            throw new EntityNotFoundException("commands.spreadplayers.noop");
         } else {
            ☃.sendMessage(
               new TextComponentTranslation("commands.spreadplayers.spreading." + (☃xxxxxx ? "teams" : "players"), ☃xxxxxxx.size(), ☃xxxxx, ☃xx, ☃xxx, ☃xxxx)
            );
            this.spread(☃, ☃xxxxxxx, new CommandSpreadPlayers.Position(☃xx, ☃xxx), ☃xxxx, ☃xxxxx, ☃xxxxxxx.get(0).world, ☃xxxxxx);
         }
      }
   }

   private void spread(ICommandSender var1, List<Entity> var2, CommandSpreadPlayers.Position var3, double var4, double var6, World var8, boolean var9) throws CommandException {
      Random ☃ = new Random();
      double ☃x = ☃.x - ☃;
      double ☃xx = ☃.z - ☃;
      double ☃xxx = ☃.x + ☃;
      double ☃xxxx = ☃.z + ☃;
      CommandSpreadPlayers.Position[] ☃xxxxx = this.createInitialPositions(☃, ☃ ? this.getNumberOfTeams(☃) : ☃.size(), ☃x, ☃xx, ☃xxx, ☃xxxx);
      int ☃xxxxxx = this.spreadPositions(☃, ☃, ☃, ☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, ☃);
      double ☃xxxxxxx = this.setPlayerPositions(☃, ☃, ☃xxxxx, ☃);
      notifyCommandListener(☃, this, "commands.spreadplayers.success." + (☃ ? "teams" : "players"), new Object[]{☃xxxxx.length, ☃.x, ☃.z});
      if (☃xxxxx.length > 1) {
         ☃.sendMessage(new TextComponentTranslation("commands.spreadplayers.info." + (☃ ? "teams" : "players"), String.format("%.2f", ☃xxxxxxx), ☃xxxxxx));
      }
   }

   private int getNumberOfTeams(List<Entity> var1) {
      Set<Team> ☃ = Sets.newHashSet();

      for (Entity ☃x : ☃) {
         if (☃x instanceof EntityPlayer) {
            ☃.add(☃x.getTeam());
         } else {
            ☃.add(null);
         }
      }

      return ☃.size();
   }

   private int spreadPositions(
      CommandSpreadPlayers.Position var1,
      double var2,
      World var4,
      Random var5,
      double var6,
      double var8,
      double var10,
      double var12,
      CommandSpreadPlayers.Position[] var14,
      boolean var15
   ) throws CommandException {
      boolean ☃ = true;
      double ☃x = Float.MAX_VALUE;

      int ☃xx;
      for (☃xx = 0; ☃xx < 10000 && ☃; ☃xx++) {
         ☃ = false;
         ☃x = Float.MAX_VALUE;

         for (int ☃xxx = 0; ☃xxx < ☃.length; ☃xxx++) {
            CommandSpreadPlayers.Position ☃xxxx = ☃[☃xxx];
            int ☃xxxxx = 0;
            CommandSpreadPlayers.Position ☃xxxxxx = new CommandSpreadPlayers.Position();

            for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃.length; ☃xxxxxxx++) {
               if (☃xxx != ☃xxxxxxx) {
                  CommandSpreadPlayers.Position ☃xxxxxxxx = ☃[☃xxxxxxx];
                  double ☃xxxxxxxxx = ☃xxxx.dist(☃xxxxxxxx);
                  ☃x = Math.min(☃xxxxxxxxx, ☃x);
                  if (☃xxxxxxxxx < ☃) {
                     ☃xxxxx++;
                     ☃xxxxxx.x = ☃xxxxxx.x + (☃xxxxxxxx.x - ☃xxxx.x);
                     ☃xxxxxx.z = ☃xxxxxx.z + (☃xxxxxxxx.z - ☃xxxx.z);
                  }
               }
            }

            if (☃xxxxx > 0) {
               ☃xxxxxx.x /= ☃xxxxx;
               ☃xxxxxx.z /= ☃xxxxx;
               double ☃xxxxxxxx = ☃xxxxxx.getLength();
               if (☃xxxxxxxx > 0.0) {
                  ☃xxxxxx.normalize();
                  ☃xxxx.moveAway(☃xxxxxx);
               } else {
                  ☃xxxx.randomize(☃, ☃, ☃, ☃, ☃);
               }

               ☃ = true;
            }

            if (☃xxxx.clamp(☃, ☃, ☃, ☃)) {
               ☃ = true;
            }
         }

         if (!☃) {
            for (CommandSpreadPlayers.Position ☃xxx : ☃) {
               if (!☃xxx.isSafe(☃)) {
                  ☃xxx.randomize(☃, ☃, ☃, ☃, ☃);
                  ☃ = true;
               }
            }
         }
      }

      if (☃xx >= 10000) {
         throw new CommandException("commands.spreadplayers.failure." + (☃ ? "teams" : "players"), ☃.length, ☃.x, ☃.z, String.format("%.2f", ☃x));
      } else {
         return ☃xx;
      }
   }

   private double setPlayerPositions(List<Entity> var1, World var2, CommandSpreadPlayers.Position[] var3, boolean var4) {
      double ☃ = 0.0;
      int ☃x = 0;
      Map<Team, CommandSpreadPlayers.Position> ☃xx = Maps.newHashMap();

      for (int ☃xxx = 0; ☃xxx < ☃.size(); ☃xxx++) {
         Entity ☃xxxx = ☃.get(☃xxx);
         CommandSpreadPlayers.Position ☃xxxxx;
         if (☃) {
            Team ☃xxxxxx = ☃xxxx instanceof EntityPlayer ? ☃xxxx.getTeam() : null;
            if (!☃xx.containsKey(☃xxxxxx)) {
               ☃xx.put(☃xxxxxx, ☃[☃x++]);
            }

            ☃xxxxx = ☃xx.get(☃xxxxxx);
         } else {
            ☃xxxxx = ☃[☃x++];
         }

         ☃xxxx.setPositionAndUpdate(MathHelper.floor(☃xxxxx.x) + 0.5F, ☃xxxxx.getSpawnY(☃), MathHelper.floor(☃xxxxx.z) + 0.5);
         double ☃xxxxxx = Double.MAX_VALUE;

         for (CommandSpreadPlayers.Position ☃xxxxxxx : ☃) {
            if (☃xxxxx != ☃xxxxxxx) {
               double ☃xxxxxxxx = ☃xxxxx.dist(☃xxxxxxx);
               ☃xxxxxx = Math.min(☃xxxxxxxx, ☃xxxxxx);
            }
         }

         ☃ += ☃xxxxxx;
      }

      return ☃ / ☃.size();
   }

   private CommandSpreadPlayers.Position[] createInitialPositions(Random var1, int var2, double var3, double var5, double var7, double var9) {
      CommandSpreadPlayers.Position[] ☃ = new CommandSpreadPlayers.Position[☃];

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         CommandSpreadPlayers.Position ☃xx = new CommandSpreadPlayers.Position();
         ☃xx.randomize(☃, ☃, ☃, ☃, ☃);
         ☃[☃x] = ☃xx;
      }

      return ☃;
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length >= 1 && ☃.length <= 2 ? getTabCompletionCoordinateXZ(☃, 0, ☃) : Collections.emptyList();
   }

   static class Position {
      double x;
      double z;

      Position() {
      }

      Position(double var1, double var3) {
         this.x = ☃;
         this.z = ☃;
      }

      double dist(CommandSpreadPlayers.Position var1) {
         double ☃ = this.x - ☃.x;
         double ☃x = this.z - ☃.z;
         return Math.sqrt(☃ * ☃ + ☃x * ☃x);
      }

      void normalize() {
         double ☃ = this.getLength();
         this.x /= ☃;
         this.z /= ☃;
      }

      float getLength() {
         return MathHelper.sqrt(this.x * this.x + this.z * this.z);
      }

      public void moveAway(CommandSpreadPlayers.Position var1) {
         this.x = this.x - ☃.x;
         this.z = this.z - ☃.z;
      }

      public boolean clamp(double var1, double var3, double var5, double var7) {
         boolean ☃ = false;
         if (this.x < ☃) {
            this.x = ☃;
            ☃ = true;
         } else if (this.x > ☃) {
            this.x = ☃;
            ☃ = true;
         }

         if (this.z < ☃) {
            this.z = ☃;
            ☃ = true;
         } else if (this.z > ☃) {
            this.z = ☃;
            ☃ = true;
         }

         return ☃;
      }

      public int getSpawnY(World var1) {
         BlockPos ☃ = new BlockPos(this.x, 256.0, this.z);

         while (☃.getY() > 0) {
            ☃ = ☃.down();
            if (☃.getBlockState(☃).getMaterial() != Material.AIR) {
               return ☃.getY() + 1;
            }
         }

         return 257;
      }

      public boolean isSafe(World var1) {
         BlockPos ☃ = new BlockPos(this.x, 256.0, this.z);

         while (☃.getY() > 0) {
            ☃ = ☃.down();
            Material ☃x = ☃.getBlockState(☃).getMaterial();
            if (☃x != Material.AIR) {
               return !☃x.isLiquid() && ☃x != Material.FIRE;
            }
         }

         return false;
      }

      public void randomize(Random var1, double var2, double var4, double var6, double var8) {
         this.x = MathHelper.nextDouble(☃, ☃, ☃);
         this.z = MathHelper.nextDouble(☃, ☃, ☃);
      }
   }
}
