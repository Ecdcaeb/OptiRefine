package net.minecraft.command;

public interface ICommandListener {
   void notifyListener(ICommandSender var1, ICommand var2, int var3, String var4, Object... var5);
}
