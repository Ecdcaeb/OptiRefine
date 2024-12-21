package mods.Hileb.optirefine.core;

import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.OptiRefine;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import top.outlands.foundation.TransformerDelegate;

import java.util.Map;

@IFMLLoadingPlugin.Name(OptiRefine.NAME)
@IFMLLoadingPlugin.MCVersion(net.minecraftforge.common.ForgeVersion.mcVersion)
public class OptiRefineCore implements IFMLLoadingPlugin{

    static {
        TransformerDelegate.registerExplicitTransformerByInstance(
                new OptifineTransformerTransformer(),
                "optifine.OptiFineClassTransformer"
        );
        TransformerDelegate.registerExplicitTransformerByInstance(
                new OptiRefineRuntimePublicTransformer(),
                OptiRefineBlackboard.CLASSES.toArray(String[]::new)
        );
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
}