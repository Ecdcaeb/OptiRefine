package net.minecraft.command;

public class WrongUsageException extends SyntaxErrorException {
   public WrongUsageException(String var1, Object... var2) {
      super(☃, ☃);
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }
}
