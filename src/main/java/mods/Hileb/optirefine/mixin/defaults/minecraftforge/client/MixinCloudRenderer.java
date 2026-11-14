package mods.Hileb.optirefine.mixin.defaults.minecraftforge.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL20;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * [AUDIT 2026-08-09] OF cloud semantics rebuilt on Forge's default CloudRenderer.
 *
 * WHY (see MixinRenderGlobal.renderClouds doc): OF's renderClouds override fought the cleanroom
 * Forge hook chain (FMLClientHandler.renderClouds -&gt; WorldProvider.getCloudRenderer() OR Forge
 * default CloudRenderer). OF only honors WorldProvider.getCloudRenderer(); when it is null OF
 * continues with its own cloud pipeline (fancy/fast split, Shaders.beginClouds/endClouds,
 * ofCloudsHeight). Forge's default CloudRenderer, however, is ALWAYS built when the vanilla
 * cloud setting is on, so calling FMLClientHandler.renderClouds() from a RenderGlobal wrap
 * either suppressed the OF branch entirely or double-rendered. Keeping RenderGlobal vanilla
 * and layering the OF deltas onto net.minecraftforge.client.CloudRenderer here gives the same
 * net behavior with no hook-chain conflict:
 *   - ofClouds=OFF  -&gt; skip render (return false)
 *   - shaders on    -&gt; Shaders.beginClouds()/endClouds() around the draw
 *   - ofCloudsHeight-&gt; cloud plane Y offset (getCloudHeight() + ofCloudsHeight * 128.0F)
 *   - fancy/fast    -&gt; use Forge's existing cloudMode geometry branches with OF's effective setting
 */
@Mixin(targets = "net.minecraftforge.client.CloudRenderer")
public abstract class MixinCloudRenderer {

    @Shadow
    private native boolean isBuilt();

    @ModifyExpressionValue(method = "checkSettings", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;shouldRenderClouds()I"))
    private int optiRefine$cloudMode(int original) {
        return Config.isCloudsOff() ? 0 : Config.isCloudsFancy() ? 2 : 1;
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofCloudsHeight F")
// [AUDIT-OK] OF member ofCloudsHeight, not in baseline (MixinGameSettings provides); local bridge per cross-mixin rule
    private static native float optiRefine$getCloudHeightOffset(net.minecraft.client.settings.GameSettings gameSettings);

    @WrapMethod(method = "render")
    private boolean optiRefine$render(int cloudTicks, float partialTicks, Operation<Boolean> original) {
        if (Config.isCloudsOff() || !isBuilt()) {
            return false;
        }
        boolean shaders = Config.isShaders();
        if (shaders) {
            // Forge clouds use POSITION_TEX_COLOR, including their direct VBO draw path.
            GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
            GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
            GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
            Shaders.beginClouds();
        }
        boolean rendered;
        try {
            rendered = original.call(cloudTicks, partialTicks);
        } finally {
            if (shaders) {
                Shaders.endClouds();
            }
        }
        return rendered;
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getCloudHeight()F"))
    private float optiRefine$cloudHeight(float original) {
        return original + optiRefine$getCloudHeightOffset(Config.getGameSettings()) * 128.0F;
    }
}
