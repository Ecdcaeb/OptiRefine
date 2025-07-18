package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase {
   @Override
   public boolean checkPermission(MinecraftServer var1, ICommandSender var2) {
      return ☃.isSinglePlayer() || super.checkPermission(☃, ☃);
   }

   @Override
   public String getName() {
      return "seed";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.seed.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      World ☃ = (World)(☃ instanceof EntityPlayer ? ((EntityPlayer)☃).world : ☃.getWorld(0));
      ☃.sendMessage(new TextComponentTranslation("commands.seed.success", ☃.getSeed()));
   }
}
