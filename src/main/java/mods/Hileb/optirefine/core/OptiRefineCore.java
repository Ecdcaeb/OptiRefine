package mods.Hileb.optirefine.core;

import com.google.common.eventbus.EventBus;
import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.OptiRefine;
import mods.Hileb.optirefine.core.transformer.dev.OptifineDevTweakerTransformer;
import mods.Hileb.optirefine.core.transformer.dev.OptifineDevUtilTransformer;
import mods.Hileb.optirefine.core.transformer.OptifineTransformerTransformer;
import mods.Hileb.optirefine.library.fmlmodhacker.MetaDataDecoder;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@IFMLLoadingPlugin.SortingIndex(1000)
@IFMLLoadingPlugin.Name(OptiRefine.NAME)
@IFMLLoadingPlugin.MCVersion(net.minecraftforge.common.ForgeVersion.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions({
        "mods.Hileb.optirefine.core.",
        "mods.Hileb.optirefine.library.foundationx."
})
public class OptiRefineCore implements IFMLLoadingPlugin {

    public static final Logger LOGGER;

    static {
        LOGGER = OptiRefineLog.log;
        setupTransformers();
    }

    public static void setupTransformers(){
        TransformerHelper.registerTransformer(new OptifineTransformerTransformer());
        //TransformerHelper.registerTransformer(new OptiRefineRuntimePublicTransformer());

        TransformerHelper.registerTransformer(new OptifineDevUtilTransformer());
        TransformerHelper.registerTransformer(new OptifineDevTweakerTransformer());
    }

    @Nullable
    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getModContainerClass() {
        return "mods.Hileb.optirefine.core.OptiRefineCore$Container";
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    public static File coremodLocation = null;
    @Override
    public void injectData(Map<String, Object> map) {
        coremodLocation = (File) map.get("coremodLocation");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @SuppressWarnings("unused")
    public static class Container extends DummyModContainer{
        public Container(){
            super(decodeData());
        }

        private static ModMetadata decodeData() {
            File source = coremodLocation;
            if (source.isFile()) {
                try (FileSystem fs = FileSystems.newFileSystem(source.toPath(), (ClassLoader)null)){
                    try (InputStream inputStream = Files.newInputStream(Objects.requireNonNull(fs.getPath("/mcmod.info")))) {
                        return MetaDataDecoder.decodeMcModInfo(inputStream).get("optirefine");
                    } catch (Throwable t) {
                        OptiRefineLog.log.error("Error loading metadata from jar: ", t);
                    }
                } catch (IOException e) {
                    OptiRefineLog.log.error("Error loading FileSystem from jar: ", e);
                }
            } else if (source.isDirectory()) {
                try (InputStream inputStream = Files.newInputStream(Objects.requireNonNull(source.toPath().resolve("mcmod.info")))) {
                    return MetaDataDecoder.decodeMcModInfo(inputStream).get("optirefine");
                } catch (Throwable t) {
                    OptiRefineLog.log.error("Error loading metadata from jar: ", t);
                }
            }
            ModMetadata modMetadata = new ModMetadata();
            modMetadata.name = "OptiRefine";
            modMetadata.modId = "optirefine";
            modMetadata.authorList.add("Hileb");
            modMetadata.version = "Unknown";
            return modMetadata;
        }

        @Override
        public File getSource() {
            return coremodLocation;
        }

        @Override
        public Class<?> getCustomResourcePackClass() {
            try {
                return this.getSource().isDirectory() ? Class.forName("net.minecraftforge.fml.client.FMLFolderResourcePack", true, this.getClass().getClassLoader()) : Class.forName("net.minecraftforge.fml.client.FMLFileResourcePack", true, this.getClass().getClassLoader());
            } catch (Throwable var2) {
                return null;
            }
        }

        @Override
        public boolean registerBus(EventBus bus, LoadController controller) {
            return true;
        }
    }
}