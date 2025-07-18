package net.minecraft.command;

public class InvalidBlockStateException extends CommandException {
   public InvalidBlockStateException() {
      this("commands.generic.blockstate.invalid");
   }

   public InvalidBlockStateException(String var1, Object... var2) {
      super(☃, ☃);
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }
}
