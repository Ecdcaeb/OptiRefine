package net.minecraft.command;

public class EntityNotFoundException extends CommandException {
   public EntityNotFoundException(String var1) {
      this("commands.generic.entity.notFound", ☃);
   }

   public EntityNotFoundException(String var1, Object... var2) {
      super(☃, ☃);
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }
}
