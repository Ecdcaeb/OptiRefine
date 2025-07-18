package net.minecraft.command;

public class NumberInvalidException extends CommandException {
   public NumberInvalidException() {
      this("commands.generic.num.invalid");
   }

   public NumberInvalidException(String var1, Object... var2) {
      super(☃, ☃);
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }
}
