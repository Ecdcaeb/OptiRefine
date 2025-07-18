package net.minecraft.command;

public class PlayerNotFoundException extends CommandException {
   public PlayerNotFoundException(String var1) {
      super(☃);
   }

   public PlayerNotFoundException(String var1, Object... var2) {
      super(☃, ☃);
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }
}
