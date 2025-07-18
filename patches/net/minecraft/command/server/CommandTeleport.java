package net.minecraft.command.server;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CommandTeleport extends CommandBase {
   @Override
   public String getName() {
      return "teleport";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.teleport.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 4) {
         throw new WrongUsageException("commands.teleport.usage");
      } else {
         Entity ☃ = getEntity(☃, ☃, ☃[0]);
         if (☃.world != null) {
            int ☃x = 4096;
            Vec3d ☃xx = ☃.getPositionVector();
            int ☃xxx = 1;
            CommandBase.CoordinateArg ☃xxxx = parseCoordinate(☃xx.x, ☃[☃xxx++], true);
            CommandBase.CoordinateArg ☃xxxxx = parseCoordinate(☃xx.y, ☃[☃xxx++], -4096, 4096, false);
            CommandBase.CoordinateArg ☃xxxxxx = parseCoordinate(☃xx.z, ☃[☃xxx++], true);
            Entity ☃xxxxxxx = ☃.getCommandSenderEntity() == null ? ☃ : ☃.getCommandSenderEntity();
            CommandBase.CoordinateArg ☃xxxxxxxx = parseCoordinate(
               ☃.length > ☃xxx ? ☃xxxxxxx.rotationYaw : ☃.rotationYaw, ☃.length > ☃xxx ? ☃[☃xxx] : "~", false
            );
            ☃xxx++;
            CommandBase.CoordinateArg ☃xxxxxxxxx = parseCoordinate(
               ☃.length > ☃xxx ? ☃xxxxxxx.rotationPitch : ☃.rotationPitch, ☃.length > ☃xxx ? ☃[☃xxx] : "~", false
            );
            doTeleport(☃, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx);
            notifyCommandListener(
               ☃, this, "commands.teleport.success.coordinates", new Object[]{☃.getName(), ☃xxxx.getResult(), ☃xxxxx.getResult(), ☃xxxxxx.getResult()}
            );
         }
      }
   }

   private static void doTeleport(
      Entity var0,
      CommandBase.CoordinateArg var1,
      CommandBase.CoordinateArg var2,
      CommandBase.CoordinateArg var3,
      CommandBase.CoordinateArg var4,
      CommandBase.CoordinateArg var5
   ) {
      if (☃ instanceof EntityPlayerMP) {
         Set<SPacketPlayerPosLook.EnumFlags> ☃ = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
         float ☃x = (float)☃.getAmount();
         if (☃.isRelative()) {
            ☃.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
         } else {
            ☃x = MathHelper.wrapDegrees(☃x);
         }

         float ☃xx = (float)☃.getAmount();
         if (☃.isRelative()) {
            ☃.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
         } else {
            ☃xx = MathHelper.wrapDegrees(☃xx);
         }

         ☃.dismountRidingEntity();
         ((EntityPlayerMP)☃).connection.setPlayerLocation(☃.getResult(), ☃.getResult(), ☃.getResult(), ☃x, ☃xx, ☃);
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
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else {
         return ☃.length > 1 && ☃.length <= 4 ? getTabCompletionCoordinate(☃, 1, ☃) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
