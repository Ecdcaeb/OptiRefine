package mods.Hileb.optirefine.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;

import java.io.File;

public final class OptiRefineModContainer extends DummyModContainer {
    private final File source;

    public OptiRefineModContainer() { this(OptiRefineBootstrap.source()); }

    private OptiRefineModContainer(File source) {
        super(ModMetadataLoader.load(source));
        this.source = source;
    }

    @Override public File getSource() { return source; }

    @Override public Class<?> getCustomResourcePackClass() {
        if (source == null) return null;
        String type = source.isDirectory()
                ? "net.minecraftforge.fml.client.FMLFolderResourcePack"
                : "net.minecraftforge.fml.client.FMLFileResourcePack";
        try {
            return Class.forName(type, true, getClass().getClassLoader());
        } catch (ClassNotFoundException | LinkageError exception) {
            OptiRefineLog.log.debug("Client resource pack adapter {} is unavailable", type, exception);
            return null;
        }
    }

    @Override public boolean registerBus(EventBus bus, LoadController controller) { return true; }
}
