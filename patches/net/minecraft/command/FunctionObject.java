package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayDeque;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.util.ResourceLocation;

public class FunctionObject {
   private final FunctionObject.Entry[] entries;

   public FunctionObject(FunctionObject.Entry[] var1) {
      this.entries = ☃;
   }

   public FunctionObject.Entry[] getEntries() {
      return this.entries;
   }

   public static FunctionObject create(FunctionManager var0, List<String> var1) {
      List<FunctionObject.Entry> ☃ = Lists.newArrayListWithCapacity(☃.size());

      for (String ☃x : ☃) {
         ☃x = ☃x.trim();
         if (!☃x.startsWith("#") && !☃x.isEmpty()) {
            String[] ☃xx = ☃x.split(" ", 2);
            String ☃xxx = ☃xx[0];
            if (!☃.getCommandManager().getCommands().containsKey(☃xxx)) {
               if (☃xxx.startsWith("//")) {
                  throw new IllegalArgumentException("Unknown or invalid command '" + ☃xxx + "' (if you intended to make a comment, use '#' not '//')");
               }

               if (☃xxx.startsWith("/") && ☃xxx.length() > 1) {
                  throw new IllegalArgumentException(
                     "Unknown or invalid command '" + ☃xxx + "' (did you mean '" + ☃xxx.substring(1) + "'? Do not use a preceding forwards slash.)"
                  );
               }

               throw new IllegalArgumentException("Unknown or invalid command '" + ☃xxx + "'");
            }

            ☃.add(new FunctionObject.CommandEntry(☃x));
         }
      }

      return new FunctionObject(☃.toArray(new FunctionObject.Entry[☃.size()]));
   }

   public static class CacheableFunction {
      public static final FunctionObject.CacheableFunction EMPTY = new FunctionObject.CacheableFunction((ResourceLocation)null);
      @Nullable
      private final ResourceLocation id;
      private boolean isValid;
      private FunctionObject function;

      public CacheableFunction(@Nullable ResourceLocation var1) {
         this.id = ☃;
      }

      public CacheableFunction(FunctionObject var1) {
         this.id = null;
         this.function = ☃;
      }

      @Nullable
      public FunctionObject get(FunctionManager var1) {
         if (!this.isValid) {
            if (this.id != null) {
               this.function = ☃.getFunction(this.id);
            }

            this.isValid = true;
         }

         return this.function;
      }

      @Override
      public String toString() {
         return String.valueOf(this.id);
      }
   }

   public static class CommandEntry implements FunctionObject.Entry {
      private final String command;

      public CommandEntry(String var1) {
         this.command = ☃;
      }

      @Override
      public void execute(FunctionManager var1, ICommandSender var2, ArrayDeque<FunctionManager.QueuedCommand> var3, int var4) {
         ☃.getCommandManager().executeCommand(☃, this.command);
      }

      @Override
      public String toString() {
         return "/" + this.command;
      }
   }

   public interface Entry {
      void execute(FunctionManager var1, ICommandSender var2, ArrayDeque<FunctionManager.QueuedCommand> var3, int var4);
   }

   public static class FunctionEntry implements FunctionObject.Entry {
      private final FunctionObject.CacheableFunction function;

      public FunctionEntry(FunctionObject var1) {
         this.function = new FunctionObject.CacheableFunction(☃);
      }

      @Override
      public void execute(FunctionManager var1, ICommandSender var2, ArrayDeque<FunctionManager.QueuedCommand> var3, int var4) {
         FunctionObject ☃ = this.function.get(☃);
         if (☃ != null) {
            FunctionObject.Entry[] ☃x = ☃.getEntries();
            int ☃xx = ☃ - ☃.size();
            int ☃xxx = Math.min(☃x.length, ☃xx);

            for (int ☃xxxx = ☃xxx - 1; ☃xxxx >= 0; ☃xxxx--) {
               ☃.addFirst(new FunctionManager.QueuedCommand(☃, ☃, ☃x[☃xxxx]));
            }
         }
      }

      @Override
      public String toString() {
         return "/function " + this.function;
      }
   }
}
