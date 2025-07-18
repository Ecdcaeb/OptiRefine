package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class CommandSummon extends CommandBase {
   @Override
   public String getName() {
      return "summon";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.summon.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.summon.usage");
      } else {
         String ☃ = ☃[0];
         BlockPos ☃x = ☃.getPosition();
         Vec3d ☃xx = ☃.getPositionVector();
         double ☃xxx = ☃xx.x;
         double ☃xxxx = ☃xx.y;
         double ☃xxxxx = ☃xx.z;
         if (☃.length >= 4) {
            ☃xxx = parseDouble(☃xxx, ☃[1], true);
            ☃xxxx = parseDouble(☃xxxx, ☃[2], false);
            ☃xxxxx = parseDouble(☃xxxxx, ☃[3], true);
            ☃x = new BlockPos(☃xxx, ☃xxxx, ☃xxxxx);
         }

         World ☃xxxxxx = ☃.getEntityWorld();
         if (!☃xxxxxx.isBlockLoaded(☃x)) {
            throw new CommandException("commands.summon.outOfWorld");
         } else if (EntityList.LIGHTNING_BOLT.equals(new ResourceLocation(☃))) {
            ☃xxxxxx.addWeatherEffect(new EntityLightningBolt(☃xxxxxx, ☃xxx, ☃xxxx, ☃xxxxx, false));
            notifyCommandListener(☃, this, "commands.summon.success", new Object[0]);
         } else {
            NBTTagCompound ☃xxxxxxx = new NBTTagCompound();
            boolean ☃xxxxxxxx = false;
            if (☃.length >= 5) {
               String ☃xxxxxxxxx = buildString(☃, 4);

               try {
                  ☃xxxxxxx = JsonToNBT.getTagFromJson(☃xxxxxxxxx);
                  ☃xxxxxxxx = true;
               } catch (NBTException var18) {
                  throw new CommandException("commands.summon.tagError", var18.getMessage());
               }
            }

            ☃xxxxxxx.setString("id", ☃);
            Entity ☃xxxxxxxxx = AnvilChunkLoader.readWorldEntityPos(☃xxxxxxx, ☃xxxxxx, ☃xxx, ☃xxxx, ☃xxxxx, true);
            if (☃xxxxxxxxx == null) {
               throw new CommandException("commands.summon.failed");
            } else {
               ☃xxxxxxxxx.setLocationAndAngles(☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxxxxx.rotationYaw, ☃xxxxxxxxx.rotationPitch);
               if (!☃xxxxxxxx && ☃xxxxxxxxx instanceof EntityLiving) {
                  ((EntityLiving)☃xxxxxxxxx).onInitialSpawn(☃xxxxxx.getDifficultyForLocation(new BlockPos(☃xxxxxxxxx)), null);
               }

               notifyCommandListener(☃, this, "commands.summon.success", new Object[0]);
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, EntityList.getEntityNameList());
      } else {
         return ☃.length > 1 && ☃.length <= 4 ? getTabCompletionCoordinate(☃, 1, ☃) : Collections.emptyList();
      }
   }
}
