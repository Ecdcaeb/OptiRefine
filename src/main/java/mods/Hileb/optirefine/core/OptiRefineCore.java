package mods.Hileb.optirefine.core;

import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.OptiRefine;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(1000)
@IFMLLoadingPlugin.Name(OptiRefine.NAME)
@IFMLLoadingPlugin.MCVersion(net.minecraftforge.common.ForgeVersion.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions({
        "mods.Hileb.optirefine.core.",
        "mods.Hileb.optirefine.library."
})
public final class OptiRefineCore implements IFMLLoadingPlugin {
    static {
        OptiRefineBootstrap.registerTransformers();
    }

    @Nullable
    @Override public String[] getASMTransformerClass() { return null; }

    @Override public String getModContainerClass() {
        return "mods.Hileb.optirefine.core.OptiRefineModContainer";
    }

    @Nullable
    @Override public String getSetupClass() { return null; }

    @Override public void injectData(Map<String, Object> data) {
        OptiRefineBootstrap.setSource((File) data.get("coremodLocation"));
    }

    @Nullable
    @Override public String getAccessTransformerClass() { return null; }
}
