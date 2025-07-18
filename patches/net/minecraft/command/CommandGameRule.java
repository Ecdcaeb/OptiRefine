package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase {
   @Override
   public String getName() {
      return "gamerule";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.gamerule.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      GameRules ☃ = this.getOverWorldGameRules(☃);
      String ☃x = ☃.length > 0 ? ☃[0] : "";
      String ☃xx = ☃.length > 1 ? buildString(☃, 1) : "";
      switch (☃.length) {
         case 0:
            ☃.sendMessage(new TextComponentString(joinNiceString(☃.getRules())));
            break;
         case 1:
            if (!☃.hasRule(☃x)) {
               throw new CommandException("commands.gamerule.norule", ☃x);
            }

            String ☃xxx = ☃.getString(☃x);
            ☃.sendMessage(new TextComponentString(☃x).appendText(" = ").appendText(☃xxx));
            ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃.getInt(☃x));
            break;
         default:
            if (☃.areSameType(☃x, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(☃xx) && !"false".equals(☃xx)) {
               throw new CommandException("commands.generic.boolean.invalid", ☃xx);
            }

            ☃.setOrCreateGameRule(☃x, ☃xx);
            notifyGameRuleChange(☃, ☃x, ☃);
            notifyCommandListener(☃, this, "commands.gamerule.success", new Object[]{☃x, ☃xx});
      }
   }

   public static void notifyGameRuleChange(GameRules var0, String var1, MinecraftServer var2) {
      if ("reducedDebugInfo".equals(☃)) {
         byte ☃ = (byte)(☃.getBoolean(☃) ? 22 : 23);

         for (EntityPlayerMP ☃x : ☃.getPlayerList().getPlayers()) {
            ☃x.connection.sendPacket(new SPacketEntityStatus(☃x, ☃));
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, this.getOverWorldGameRules(☃).getRules());
      } else {
         if (☃.length == 2) {
            GameRules ☃ = this.getOverWorldGameRules(☃);
            if (☃.areSameType(☃[0], GameRules.ValueType.BOOLEAN_VALUE)) {
               return getListOfStringsMatchingLastWord(☃, new String[]{"true", "false"});
            }

            if (☃.areSameType(☃[0], GameRules.ValueType.FUNCTION)) {
               return getListOfStringsMatchingLastWord(☃, ☃.getFunctionManager().getFunctions().keySet());
            }
         }

         return Collections.emptyList();
      }
   }

   private GameRules getOverWorldGameRules(MinecraftServer var1) {
      return ☃.getWorld(0).getGameRules();
   }
}
