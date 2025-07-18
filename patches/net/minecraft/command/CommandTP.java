package net.minecraft.command;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class CommandTP extends CommandBase {
   @Override
   public String getName() {
      return "tp";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.tp.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.tp.usage");
      } else {
         int ☃ = 0;
         Entity ☃x;
         if (☃.length != 2 && ☃.length != 4 && ☃.length != 6) {
            ☃x = getCommandSenderAsPlayer(☃);
         } else {
            ☃x = getEntity(☃, ☃, ☃[0]);
            ☃ = 1;
         }

         if (☃.length == 1 || ☃.length == 2) {
            Entity ☃xx = getEntity(☃, ☃, ☃[☃.length - 1]);
            if (☃xx.world != ☃x.world) {
               throw new CommandException("commands.tp.notSameDimension");
            } else {
               ☃x.dismountRidingEntity();
               if (☃x instanceof EntityPlayerMP) {
                  ((EntityPlayerMP)☃x).connection.setPlayerLocation(☃xx.posX, ☃xx.posY, ☃xx.posZ, ☃xx.rotationYaw, ☃xx.rotationPitch);
               } else {
                  ☃x.setLocationAndAngles(☃xx.posX, ☃xx.posY, ☃xx.posZ, ☃xx.rotationYaw, ☃xx.rotationPitch);
               }

               notifyCommandListener(☃, this, "commands.tp.success", new Object[]{☃x.getName(), ☃xx.getName()});
            }
         } else if (☃.length < ☃ + 3) {
            throw new WrongUsageException("commands.tp.usage");
         } else if (☃x.world != null) {
            int ☃xx = 4096;
            int var6 = ☃ + 1;
            CommandBase.CoordinateArg ☃xxx = parseCoordinate(☃x.posX, ☃[☃], true);
            CommandBase.CoordinateArg ☃xxxx = parseCoordinate(☃x.posY, ☃[var6++], -4096, 4096, false);
            CommandBase.CoordinateArg ☃xxxxx = parseCoordinate(☃x.posZ, ☃[var6++], true);
            CommandBase.CoordinateArg ☃xxxxxx = parseCoordinate(☃x.rotationYaw, ☃.length > var6 ? ☃[var6++] : "~", false);
            CommandBase.CoordinateArg ☃xxxxxxx = parseCoordinate(☃x.rotationPitch, ☃.length > var6 ? ☃[var6] : "~", false);
            teleportEntityToCoordinates(☃x, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
            notifyCommandListener(
               ☃, this, "commands.tp.success.coordinates", new Object[]{☃x.getName(), ☃xxx.getResult(), ☃xxxx.getResult(), ☃xxxxx.getResult()}
            );
         }
      }
   }

   private static void teleportEntityToCoordinates(
      Entity var0,
      CommandBase.CoordinateArg var1,
      CommandBase.CoordinateArg var2,
      CommandBase.CoordinateArg var3,
      CommandBase.CoordinateArg var4,
      CommandBase.CoordinateArg var5
   ) {
      if (☃ instanceof EntityPlayerMP) {
         Set<SPacketPlayerPosLook.EnumFlags> ☃ = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
         if (☃.isRelative()) {
            ☃.add(SPacketPlayerPosLook.EnumFlags.X);
         }

         if (☃.isRelative()) {
            ☃.add(SPacketPlayerPosLook.EnumFlags.Y);
         }

         if (☃.isRelative()) {
            ☃.add(SPacketPlayerPosLook.EnumFlags.Z);
         }

         if (☃.isRelative()) {
            ☃.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
         }

         if (☃.isRelative()) {
            ☃.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
         }

         float ☃x = (float)☃.getAmount();
         if (!☃.isRelative()) {
            ☃x = MathHelper.wrapDegrees(☃x);
         }

         float ☃xx = (float)☃.getAmount();
         if (!☃.isRelative()) {
            ☃xx = MathHelper.wrapDegrees(☃xx);
         }

         ☃.dismountRidingEntity();
         ((EntityPlayerMP)☃).connection.setPlayerLocation(☃.getAmount(), ☃.getAmount(), ☃.getAmount(), ☃x, ☃xx, ☃);
         ☃.setRotationYawHead(☃x);
      } else {
         float ☃xx = (float)MathHelper.wrapDegrees(☃.getResult());
         float ☃xxx = (float)MathHelper.wrapDegrees(☃.getResult());
         ☃xxx = MathHelper.clamp(☃xxx, -90.0F, 90.0F);
         ☃.setLocationAndAngles(☃.getResult(), ☃.getResult(), ☃.getResult(), ☃xx, ☃xxx);
         ☃.setRotationYawHead(☃xx);
      }

      if (!(☃ instanceof EntityLivingBase) || !((EntityLivingBase)☃).isElytraFlying()) {
         ☃.motionY = 0.0;
         ☃.onGround = true;
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length != 1 && ☃.length != 2 ? Collections.emptyList() : getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
