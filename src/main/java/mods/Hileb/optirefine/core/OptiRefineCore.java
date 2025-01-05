package mods.Hileb.optirefine.core;

import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.OptiRefine;
import mods.Hileb.optirefine.core.transformer.TransformerHelper;
import mods.Hileb.optirefine.mixinx.cursedmixinextensions.CursedMixinExtensions;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.Name(OptiRefine.NAME)
@IFMLLoadingPlugin.MCVersion(net.minecraftforge.common.ForgeVersion.mcVersion)
public class OptiRefineCore implements IFMLLoadingPlugin{

    static {
        CursedMixinExtensions.bootstrap();
        TransformerHelper.setupOptiRefineTransformers();
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