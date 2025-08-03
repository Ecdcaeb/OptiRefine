package mods.Hileb.optirefine.core;

import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.OptiRefine;
import mods.Hileb.optirefine.core.transformer.OptiRefineRuntimePublicTransformer;
import mods.Hileb.optirefine.core.transformer.dev.OptifineDevTweakerTransformer;
import mods.Hileb.optirefine.core.transformer.dev.OptifineDevUtilTransformer;
import mods.Hileb.optirefine.core.transformer.OptifineTransformerTransformer;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(1000)
@IFMLLoadingPlugin.Name(OptiRefine.NAME)
@IFMLLoadingPlugin.MCVersion(net.minecraftforge.common.ForgeVersion.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions({
        "mods.Hileb.optirefine.core.",
        "mods.Hileb.optirefine.library.foundationx."
})
public class OptiRefineCore implements IFMLLoadingPlugin{

    public static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(OptiRefine.NAME);
        setupTransformers();
    }

    public static void setupTransformers(){
        TransformerHelper.registerTransformer(new OptifineTransformerTransformer());
        TransformerHelper.registerTransformer(new OptiRefineRuntimePublicTransformer());

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
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static class Container extends DummyModContainer{
        public Container(){
            super(OptiRefine.MOD_METADATA);
        }
    }
}