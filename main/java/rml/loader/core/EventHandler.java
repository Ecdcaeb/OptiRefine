package rml.loader.core;

import com.google.common.eventbus.Subscribe;
import rml.jrx.announces.BeDiscovered;
import rml.loader.api.config.ConfigPatcher;
import rml.loader.api.event.early.FMLBeforeStageEvent;
import rml.loader.api.mods.module.ModuleType;
import rml.layer.compat.crt.CrTZenClassRegisterEvent;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/6 23:24
 **/
@SuppressWarnings("unused")
public enum EventHandler {
    INSTANCE;
    @Subscribe
    public void beforePreInitializationEvent(FMLBeforeStageEvent event){
        if (event.stage == LoaderState.PREINITIALIZATION){
            ConfigPatcher.Json.handleOverride();
        }
        if (event.stage == LoaderState.CONSTRUCTING){
            RMLTransformer.Transformers.Late.initModTransformers(event.event);
        }
    }

    @Subscribe
    public void registerZenClass(CrTZenClassRegisterEvent event){

    }
}
