package rml.loader.api.mods;

import net.minecraftforge.fml.common.FMLContainerHolder;
import rml.jrx.announces.PublicAPI;
import rml.loader.ResourceModLoader;
import rml.loader.api.mods.module.Module;
import rml.loader.api.mods.module.ModuleType;
import net.minecraftforge.fml.common.ModContainer;

import java.nio.file.Path;
import java.util.HashMap;

@PublicAPI
public class ContainerHolder implements FMLContainerHolder {
    public final ModContainer container;
    public final HashMap<ModuleType, Module> modules;
    public final int packVersion;
    public ContainerHolder(ModContainer container, Module[] modules){
        this(container, modules, 2);
    }

    public ContainerHolder(ModContainer container, Module[] modules, int packVersion){
        this.container = container;
        this.modules = new HashMap<>();
        for(Module module : modules){
            this.modules.put(module.moduleType, module);
        }
        this.packVersion = packVersion;
    }

    public ModContainer getContainer() {
        return container;
    }

    public HashMap<ModuleType,Module> getModules() {
        return modules;
    }

    public boolean hasModule(ModuleType module){
        return modules.containsKey(module);
    }

    public ContainerHolder(ModContainer container){
        this(container, ModuleType.getAllForDefault());
    }

    @Override
    public ModContainer getFMLContainer() {
        return getContainer();
    }

    @FunctionalInterface
    public interface ModuleConsumer{
        void accept(ModuleType module, ContainerHolder containerHolder);
    }

    @FunctionalInterface
    public interface FileLootModuleConsumer{
        void accept(ContainerHolder containerHolder, Module module, Path root, Path file);
    }
}
