package net.minecraft.command;

public class CommandException extends Exception {
   private final Object[] errorObjects;

   public CommandException(String var1, Object... var2) {
      super(☃);
      this.errorObjects = ☃;
   }

   public Object[] getErrorObjects() {
      return this.errorObjects;
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }
}
