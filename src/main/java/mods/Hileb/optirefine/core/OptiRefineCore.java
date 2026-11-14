package mods.Hileb.optirefine.core;

import com.google.common.eventbus.EventBus;
import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.OptiRefine;
import mods.Hileb.optirefine.core.transformer.OptiRefineConfigTransformer;
import mods.Hileb.optirefine.core.transformer.OptifineForgeTransformer;
import mods.Hileb.optirefine.core.transformer.OptifineTransformerTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.CursedMixinExtensions;
import mods.Hileb.optirefine.library.fmlmodhacker.MetaDataDecoder;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Collections;
import java.util.IdentityHashMap;

@IFMLLoadingPlugin.SortingIndex(1000)
@IFMLLoadingPlugin.Name(OptiRefine.NAME)
@IFMLLoadingPlugin.MCVersion(net.minecraftforge.common.ForgeVersion.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions({
        "mods.Hileb.optirefine.core.",
        "mods.Hileb.optirefine.library."
})
public class OptiRefineCore implements IFMLLoadingPlugin {

    private static final Logger LOGGER = LogManager.getLogger();

    static {
        setupTransformers();
    }

    public static void setupTransformers(){
        TransformerHelper.registerTransformer(new OptifineTransformerTransformer());
        TransformerHelper.registerTransformer(new OptifineForgeTransformer());
        TransformerHelper.registerTransformer(new OptiRefineConfigTransformer());

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
        public static ModMetadata DATA = null;

        public Container(){
            super(DATA = decodeData());
        }

        private static ModMetadata decodeData() {

            File source = coremodLocation;

            if (source == null) {

                LOGGER.warn("coremodLocation is null, falling back to default metadata");

                return fallbackMetadata();

            }

            if (source.isFile()) {
                try (FileSystem fs = FileSystems.newFileSystem(source.toPath(), (ClassLoader)null)){
                    try (InputStream inputStream = Files.newInputStream(Objects.requireNonNull(fs.getPath("/mcmod.info")))) {
                        return MetaDataDecoder.decodeMcModInfo(inputStream).get("optirefine");
                    } catch (Throwable t) {
                        LOGGER.error("Error loading metadata from jar: ", t);
                    }
                } catch (IOException e) {
                    LOGGER.error("Error loading FileSystem from jar: ", e);
                }
            } else if (source.isDirectory()) {

                try (InputStream inputStream = Files.newInputStream(Objects.requireNonNull(source.toPath().resolve("mcmod.info")))) {

                    return MetaDataDecoder.decodeMcModInfo(inputStream).get("optirefine");

                } catch (Throwable t) {

                    LOGGER.error("Error loading metadata from jar: ", t);

                }

            }

            return fallbackMetadata();

        }



        private static ModMetadata fallbackMetadata() {

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

    private static final Set<ClassNode> pendingMixinTransforms = Collections.newSetFromMap(new IdentityHashMap<>());
    private static boolean mixinExtensionRegistered;

    private static synchronized void registerMixinExtension() {
        if (mixinExtensionRegistered) return;
        MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
        Extensions extensions = (Extensions) ((IMixinTransformer) environment.getActiveTransformer()).getExtensions();
        extensions.add(new IExtension() {
            @Override
            public boolean checkActive(MixinEnvironment environment) { return true; }

            @Override
            public void preApply(ITargetClassContext context) {}

            @Override
            public void postApply(ITargetClassContext context) {}

            @Override
            public void export(MixinEnvironment environment, String name, boolean force, ClassNode classNode) {
                if (pendingMixinTransforms.remove(classNode)) {
                    // Export callbacks run after every extension's postApply, including MixinExtras' late injectors.
                    CursedMixinExtensions.postApply(classNode);
                    OptiRefineConfigTransformer.transform0(classNode);
                    CursedMixinExtensions.dumpFinalClass(classNode);
                }
            }
        });
        extensions.select(environment);
        mixinExtensionRegistered = true;
    }

    public static class DefaultMixinPlugin implements IMixinConfigPlugin {
        @Override
        public void onLoad(String s) {
            registerMixinExtension();
        }

        @Override
        public String getRefMapperConfig() {
            return null;
        }

        @Override
        public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
            targetClassName = targetClassName.replace('/', '.');

            boolean should = true;

            LOGGER.debug("OptiRefine ShouldApply For {} ? {}", targetClassName, should);

            return should;
        }

        @Override
        public void acceptTargets(Set<String> set, Set<String> set1) {

        }

        @Override
        public List<String> getMixins() {
            return null;
        }

        @Override
        public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

        }

        @Override
        public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
            pendingMixinTransforms.add(classNode);
        }
    }

    public static class ModMixinPlugin implements IMixinConfigPlugin {
        @Override
        public void onLoad(String s) {
            registerMixinExtension();
        }

        @Override
        public String getRefMapperConfig() {
            return "";
        }

        @Override

        public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

            //mods.Hileb.optirefine.mixin.mods.<modId>.path.MixinClass

            String[] parts = mixinClassName.split("\\.");

            if (parts.length < 6 || !"mods".equals(parts[0]) || !"Hileb".equals(parts[1]) || !"optirefine".equals(parts[2]) || !"mixin".equals(parts[3]) || !"mods".equals(parts[4])) {

                return false;

            }

            String modId = parts[5];

            return Loader.isModLoaded(modId);

        }

        @Override
        public void acceptTargets(Set<String> set, Set<String> set1) {

        }

        @Override
        public List<String> getMixins() {
            return List.of();
        }

        @Override
        public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
        }

        @Override
        public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
            pendingMixinTransforms.add(classNode);
        }
    }
}
