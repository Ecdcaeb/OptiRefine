package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FunctionManager implements ITickable {
   private static final Logger LOGGER = LogManager.getLogger();
   private final File functionDir;
   private final MinecraftServer server;
   private final Map<ResourceLocation, FunctionObject> functions = Maps.newHashMap();
   private String currentGameLoopFunctionId = "-";
   private FunctionObject gameLoopFunction;
   private final ArrayDeque<FunctionManager.QueuedCommand> commandQueue = new ArrayDeque<>();
   private boolean isExecuting = false;
   private final ICommandSender gameLoopFunctionSender = new ICommandSender() {
      @Override
      public String getName() {
         return FunctionManager.this.currentGameLoopFunctionId;
      }

      @Override
      public boolean canUseCommand(int var1, String var2) {
         return ☃ <= 2;
      }

      @Override
      public World getEntityWorld() {
         return FunctionManager.this.server.worlds[0];
      }

      @Override
      public MinecraftServer getServer() {
         return FunctionManager.this.server;
      }
   };

   public FunctionManager(@Nullable File var1, MinecraftServer var2) {
      this.functionDir = ☃;
      this.server = ☃;
      this.reload();
   }

   @Nullable
   public FunctionObject getFunction(ResourceLocation var1) {
      return this.functions.get(☃);
   }

   public ICommandManager getCommandManager() {
      return this.server.getCommandManager();
   }

   public int getMaxCommandChainLength() {
      return this.server.worlds[0].getGameRules().getInt("maxCommandChainLength");
   }

   public Map<ResourceLocation, FunctionObject> getFunctions() {
      return this.functions;
   }

   @Override
   public void update() {
      String ☃ = this.server.worlds[0].getGameRules().getString("gameLoopFunction");
      if (!☃.equals(this.currentGameLoopFunctionId)) {
         this.currentGameLoopFunctionId = ☃;
         this.gameLoopFunction = this.getFunction(new ResourceLocation(☃));
      }

      if (this.gameLoopFunction != null) {
         this.execute(this.gameLoopFunction, this.gameLoopFunctionSender);
      }
   }

   public int execute(FunctionObject var1, ICommandSender var2) {
      int ☃ = this.getMaxCommandChainLength();
      if (this.isExecuting) {
         if (this.commandQueue.size() < ☃) {
            this.commandQueue.addFirst(new FunctionManager.QueuedCommand(this, ☃, new FunctionObject.FunctionEntry(☃)));
         }

         return 0;
      } else {
         int var10;
         try {
            this.isExecuting = true;
            int ☃x = 0;
            FunctionObject.Entry[] ☃xx = ☃.getEntries();

            for (int ☃xxx = ☃xx.length - 1; ☃xxx >= 0; ☃xxx--) {
               this.commandQueue.push(new FunctionManager.QueuedCommand(this, ☃, ☃xx[☃xxx]));
            }

            do {
               if (this.commandQueue.isEmpty()) {
                  return ☃x;
               }

               this.commandQueue.removeFirst().execute(this.commandQueue, ☃);
            } while (++☃x < ☃);

            var10 = ☃x;
         } finally {
            this.commandQueue.clear();
            this.isExecuting = false;
         }

         return var10;
      }
   }

   public void reload() {
      this.functions.clear();
      this.gameLoopFunction = null;
      this.currentGameLoopFunctionId = "-";
      this.loadFunctions();
   }

   private void loadFunctions() {
      if (this.functionDir != null) {
         this.functionDir.mkdirs();

         for (File ☃ : FileUtils.listFiles(this.functionDir, new String[]{"mcfunction"}, true)) {
            String ☃x = FilenameUtils.removeExtension(this.functionDir.toURI().relativize(☃.toURI()).toString());
            String[] ☃xx = ☃x.split("/", 2);
            if (☃xx.length == 2) {
               ResourceLocation ☃xxx = new ResourceLocation(☃xx[0], ☃xx[1]);

               try {
                  this.functions.put(☃xxx, FunctionObject.create(this, Files.readLines(☃, StandardCharsets.UTF_8)));
               } catch (Throwable var7) {
                  LOGGER.error("Couldn't read custom function " + ☃xxx + " from " + ☃, var7);
               }
            }
         }

         if (!this.functions.isEmpty()) {
            LOGGER.info("Loaded " + this.functions.size() + " custom command functions");
         }
      }
   }

   public static class QueuedCommand {
      private final FunctionManager functionManager;
      private final ICommandSender sender;
      private final FunctionObject.Entry entry;

      public QueuedCommand(FunctionManager var1, ICommandSender var2, FunctionObject.Entry var3) {
         this.functionManager = ☃;
         this.sender = ☃;
         this.entry = ☃;
      }

      public void execute(ArrayDeque<FunctionManager.QueuedCommand> var1, int var2) {
         this.entry.execute(this.functionManager, this.sender, ☃, ☃);
      }

      @Override
      public String toString() {
         return this.entry.toString();
      }
   }
}
