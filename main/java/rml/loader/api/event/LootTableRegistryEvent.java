package rml.loader.api.event;

import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.PublicAPI;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/15 12:42
 **/
@PublicAPI
public class LootTableRegistryEvent extends Event {
    @PrivateAPI public LootTableRegistryEvent(){}
    @PublicAPI public void register(ResourceLocation name){
        LootTableList.register(name);
    }
    @SuppressWarnings("unused")
    @PrivateAPI public static void post(){
        MinecraftForge.EVENT_BUS.post(new LootTableRegistryEvent());
    }
}
