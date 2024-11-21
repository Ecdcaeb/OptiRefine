package rml.loader.api.world.function;

import rml.loader.ResourceModLoader;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.HashSet;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/25 16:31
 **/
public class FunctionExecutor {
    public ResourceLocation function;
    public static final RMLCommandSender SENDER = new RMLCommandSender();
    public static final HashSet<ResourceLocation> invalidFunctions = new HashSet<>();
    public FunctionExecutor(ResourceLocation function){
        this.function = function;
    }
    public static void execute(WorldServer worldServer, ResourceLocation resourceLocation){
        if (!invalidFunctions.contains(resourceLocation)){
            FunctionManager manager = worldServer.getFunctionManager();
            RMLCommandSender sender = SENDER;
            sender.worldServer = worldServer;
            FunctionObject functionObject = manager.getFunction(resourceLocation);
            if (functionObject != null)manager.execute(functionObject, sender);
            else {
                ResourceModLoader.LOGGER.error("Invalid FunctionExecutor for Invalid Function {}, PLZ report to resource-mod {} authors", resourceLocation, resourceLocation.getNamespace());
                invalidFunctions.add(resourceLocation);
            }
            sender.worldServer = null;
        }
    }
    public static class RMLCommandSender implements ICommandSender{
        public WorldServer worldServer = null;

        @Override
        public String getName() {
            return "rml super command sender";
        }

        @Override
        public boolean canUseCommand(int permLevel, String commandName) {
            return true;
        }

        @Override
        public World getEntityWorld() {
            return worldServer;
        }

        @Nullable
        @Override
        public MinecraftServer getServer() {
            return worldServer.getMinecraftServer();
        }
    }
}
