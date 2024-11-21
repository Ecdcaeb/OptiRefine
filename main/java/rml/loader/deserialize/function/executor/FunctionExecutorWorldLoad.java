package rml.loader.deserialize.function.executor;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import rml.jrx.announces.BeDiscovered;
import rml.loader.api.world.function.FunctionExecutor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rml.deserializer.AbstractDeserializer;
import rml.loader.deserialize.Deserializer;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/25 16:56
 **/

@BeDiscovered(BeDiscovered.PRE_INIT)
public class FunctionExecutorWorldLoad extends FunctionExecutor {

    public static final AbstractDeserializer<FunctionExecutor> DESERIALIZER = Deserializer.named(FunctionExecutor.class, new ResourceLocation("rml", "load"))
            .require(ResourceLocation.class, "function")
            .require(IntArrayList.class, "world")
            .decode(context -> new FunctionExecutorWorldLoad(context.get(ResourceLocation.class,"function"), context.get(IntArrayList.class, "world")))
            .build();
    public IntArrayList worlds;
    public FunctionExecutorWorldLoad(ResourceLocation function, IntArrayList worlds) {
        super(function);
        this.worlds = worlds;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldLoop(WorldEvent.Load event){
        if (event.getWorld() != null){
            if (!event.getWorld().isRemote){
                if (worlds.contains(event.getWorld().provider.getDimension())){
                    execute((WorldServer) event.getWorld(), this.function);
                }
            }
        }
    }
}
